package com.eastx7.films.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.material3.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.eastx7.films.R
import com.eastx7.films.data.OmdbFilms
import com.eastx7.films.theme.*
import com.eastx7.films.uiutilities.DialogSearch
import com.eastx7.films.viewmodels.FilmItemViewModel
import com.eastx7.films.viewmodels.FilmListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmListScreen(
    viewModel: FilmListViewModel,
    onItemClick: (OmdbFilms) -> Unit,
    onSearchTitleChanged: (String) -> Unit,
    onSearchQntChanged: (Int) -> Unit,
    onDismissSearchDialog: () -> Unit,
    onApplySearchDialog: () -> Unit,
    onSearch: () -> Unit,
) {

    val showDialogSearch: Boolean by viewModel.showDialogSearch.collectAsState()
    DialogSearch(
        show = showDialogSearch,
        title = stringResource(R.string.search),
        onSearchTitleChanged = onSearchTitleChanged,
        onSearchQntChanged = onSearchQntChanged,
        onDismiss = onDismissSearchDialog,
        onConfirm = onApplySearchDialog
    )

    Scaffold(
        topBar = {
            FilmListTopBar(
                onSearch = onSearch,
            )
        },

        content = { innerPadding ->
            BodyList(
                viewModel = viewModel,
                onItemClick = onItemClick,
                innerPadding = innerPadding
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmItemScreen(
    onOpenImdbLink: () -> Unit,
    onDismiss: () -> Unit,
    viewModel: FilmItemViewModel
) {

    Scaffold(
        topBar = { ItemTopBar(onDismiss) },
        content = { innerPadding ->
            BodyItem(
                viewModel = viewModel,
                innerPadding = innerPadding,
                onOpenImdbLink = onOpenImdbLink
            )
        }
    )
}

@Composable
fun BodyList(
    viewModel: FilmListViewModel,
    onItemClick: (OmdbFilms) -> Unit,
    innerPadding: PaddingValues
) {
    val listLoaded: Boolean by viewModel.listLoaded.collectAsState()
    if (listLoaded) {
        val itemsList by viewModel.liveListFilms.observeAsState(initial = listOf())
        BodyListItems(
            itemsList = itemsList,
            innerPadding = innerPadding,
            onItemClick = onItemClick
        )
    } else {
        BodyListEmpty(viewModel = viewModel)
    }
}

@Composable
fun BodyListItems(
    innerPadding: PaddingValues,
    itemsList: List<OmdbFilms>,
    onItemClick: (OmdbFilms) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxWidth()
            .statusBarsPadding(),
    ) {
        LazyColumn(
            modifier = Modifier.padding(start = 7.dp, end = 7.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            items(itemsList)
            { item ->
                BodyListItem(
                    item = item,
                    onItemClick = onItemClick,
                )
            }
        }
    }
}

@Composable
fun BodyListEmpty(
    viewModel: FilmListViewModel
) {
    Box(Modifier.fillMaxSize()) {
        val textEmptyList: Int by viewModel.textEmptyList.collectAsState()
        Text(
            text = stringResource(textEmptyList).uppercase(),
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Center)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun BodyListItem(
    item: OmdbFilms,
    onItemClick: (OmdbFilms) -> Unit,
) {
    Box(
        Modifier
            .padding(3.dp)
    ) {
        val posterWidth = LocalConfiguration.current.screenWidthDp.dp / 3
        val posterHeight = LocalConfiguration.current.screenHeightDp.dp / 3
        val posterDimensions = if (posterWidth < posterHeight) posterWidth else posterHeight
        Card(
            onClick = { onItemClick(item) },
            modifier = Modifier
                .fillMaxWidth()
                .height(posterDimensions)
        ) {

            Row(modifier = Modifier.fillMaxWidth()) {
                GlideImage(
                    model = item.poster,
                    contentDescription = item.title,
                    modifier = Modifier
                        .padding(5.dp)
                        //.clickable(onClick = { onItemClick(item) })
                        .width(posterDimensions)
                        .height(posterDimensions),
                )
                Column {
                    Text(
                        text = item.title ?: "",
                        style = FilmTypography.headlineMedium
                    )
                    Text(text = item.year ?: "")
                    Text(text = item.type ?: "")

                }
            }
        }
        var isFavorite by rememberSaveable { mutableStateOf(false) }
        Row(
            modifier = Modifier.align(BottomEnd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                tint = Red200,
                imageVector = Icons.Filled.ChatBubble,
                contentDescription = null
            )

            IconToggleButton(
                checked = isFavorite,
                onCheckedChange = {
                    isFavorite = !isFavorite
                }
            ) {
                Icon(
                    tint = if (isFavorite) {
                        Red900
                    } else {
                        Red200
                    },
                    imageVector = if (isFavorite) {
                        Icons.Filled.Favorite
                    } else {
                        Icons.Default.FavoriteBorder
                    },
                    contentDescription = null
                )
            }
        }

    }
}

@Composable
fun BodyItem(
    viewModel: FilmItemViewModel,
    onOpenImdbLink: () -> Unit,
    innerPadding: PaddingValues
) {
    val item by viewModel.liveFilm.observeAsState(initial = null)
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top
    ) {
        item?.let {
            BodyItemCard(
                item = it,
                onOpenImdbLink = onOpenImdbLink
            )
            VertFieldSpacer()
            BodyItemComment()
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BodyItemCard(
    item: OmdbFilms,
    onOpenImdbLink: () -> Unit,
) {
    Box(
        Modifier
            .padding(start = 8.dp, end = 8.dp)
    ) {
        val posterWidth = LocalConfiguration.current.screenWidthDp.dp / 2
        val posterHeight = LocalConfiguration.current.screenHeightDp.dp / 2
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(posterHeight)
        ) {

            Row(modifier = Modifier.fillMaxWidth()) {
                GlideImage(
                    model = item.poster,
                    contentDescription = item.title,
                    modifier = Modifier
                        .padding(5.dp)
                        .width(posterWidth)
                        .height(posterHeight),
                )
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = item.title ?: "",
                        style = FilmTypography.headlineMedium
                    )
                    Text(text = item.year ?: "")
                    Text(text = item.type ?: "")

                }
            }
        }
        var isFavorite by rememberSaveable { mutableStateOf(false) }
        Row(
            modifier = Modifier.align(BottomEnd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ClickableText(
                text = AnnotatedString(stringResource(R.string.imdb)),
                style = FilmTypography.headlineMedium,
                onClick = { onOpenImdbLink() }
            )

            IconToggleButton(
                checked = isFavorite,
                onCheckedChange = {
                    isFavorite = !isFavorite
                }
            ) {
                Icon(
                    tint = if (isFavorite) {
                        Red900
                    } else {
                        Red200
                    },
                    imageVector = if (isFavorite) {
                        Icons.Filled.Favorite
                    } else {
                        Icons.Default.FavoriteBorder
                    },
                    contentDescription = null
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyItemComment() {
    Text(
        text = stringResource(R.string.comments).uppercase(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
        style = FilmTypography.titleMedium
    )
    VertFieldSpacer()
    var textComment by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    TextField(
        value = textComment,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
        onValueChange = {
            textComment = it
        },
        placeholder = {
            Text(
                text = stringResource(R.string.write_your_comment),
                style = FilmTypography.bodySmall.copy(color = Color.DarkGray)
            )
        },
        singleLine = false,
    )
    VertFieldSpacer()
    Button(
        onClick = { },
        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
        contentPadding = ButtonDefaults.ButtonWithIconContentPadding
    ) {
        Text(stringResource(R.string.leave_comment))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemTopBar(
    onDismiss: () -> Unit
) {
    TopBarTheme {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.movie_info),
                )
            },
            navigationIcon = {
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        contentDescription = "back"
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmListTopBar(
    onSearch: () -> Unit,
) {
    TopBarTheme {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    stringResource(R.string.movies),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                IconButton(onClick = onSearch) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search"
                    )
                }
            }
        )
    }
}