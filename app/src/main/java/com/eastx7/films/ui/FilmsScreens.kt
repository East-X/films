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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.material3.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.GlideLazyListPreloader
import com.eastx7.films.R
import com.eastx7.films.data.Constants.THUMBNAIL_DIMENSION
import com.eastx7.films.data.OmdbFilms
import com.eastx7.films.theme.*
import com.eastx7.films.uiutilities.DialogSearch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmListScreen(
    listLoaded: Boolean,
    itemsList: List<OmdbFilms>,
    resourceEmptyList: Int,
    onItemClick: (OmdbFilms) -> Unit,
    showDialogSearch: Boolean,
    onSearchTitleChanged: (String) -> Unit,
    onSearchQntChanged: (Int) -> Unit,
    onDismissSearchDialog: () -> Unit,
    onApplySearchDialog: () -> Unit,
    onSearch: () -> Unit,
) {


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
                listLoaded = listLoaded,
                itemsList = itemsList,
                resourceEmptyList = resourceEmptyList,
                onItemClick = onItemClick,
                innerPadding = innerPadding
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmItemScreen(
    item: OmdbFilms?,
    onOpenImdbLink: () -> Unit,
    onDismiss: () -> Unit,
) {

    Scaffold(
        topBar = { ItemTopBar(onDismiss) },
        content = { innerPadding ->
            BodyItem(
                item = item,
                innerPadding = innerPadding,
                onOpenImdbLink = onOpenImdbLink
            )
        }
    )
}

@Composable
fun BodyList(
    listLoaded: Boolean,
    itemsList: List<OmdbFilms>,
    resourceEmptyList: Int,
    onItemClick: (OmdbFilms) -> Unit,
    innerPadding: PaddingValues
) {
    if (listLoaded) {
        BodyListItems(
            itemsList = itemsList,
            innerPadding = innerPadding,
            onItemClick = onItemClick
        )
    } else {
        BodyListEmpty(resourceEmptyList = resourceEmptyList)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
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
        val state = rememberLazyListState()

        val posterWidth = LocalConfiguration.current.screenWidthDp.dp / 3
        val posterHeight = LocalConfiguration.current.screenHeightDp.dp / 3
        val posterDimensions = if (posterWidth < posterHeight) posterWidth else posterHeight

        LazyColumn(
            modifier = Modifier.padding(start = 7.dp, end = 7.dp),
            verticalArrangement = Arrangement.Center,
            state = state
        ) {
            items(itemsList)
            { item ->
                BodyListItem(
                    item = item,
                    posterDimensions = posterDimensions,
                    onItemClick = onItemClick,
                )
            }
        }
        GlideLazyListPreloader(
            state = state,
            data = itemsList,
            size = Size(THUMBNAIL_DIMENSION.toFloat(), THUMBNAIL_DIMENSION.toFloat()),
            numberOfItemsToPreload = 15,
            fixedVisibleItemCount = 5,
        ) { item, requestBuilder ->
            requestBuilder.load(item.poster)
        }
    }
}

@Composable
fun BodyListEmpty(
    resourceEmptyList: Int
) {
    Box(Modifier.fillMaxSize()) {
        Text(
            text = stringResource(resourceEmptyList).uppercase(),
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Center)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun BodyListItem(
    item: OmdbFilms,
    posterDimensions: Dp,
    onItemClick: (OmdbFilms) -> Unit,
) {
    Box(
        Modifier
            .padding(3.dp)
    ) {

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
                tint = unmarked_icon,
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
                        marked_favorite
                    } else {
                        unmarked_icon
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
    item: OmdbFilms?,
    onOpenImdbLink: () -> Unit,
    innerPadding: PaddingValues
) {

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
                        marked_favorite
                    } else {
                        unmarked_icon
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmListTopBar(
    onSearch: () -> Unit,
) {
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

fun mockFilm(): OmdbFilms {
    return OmdbFilms(
        title = "Transformers: The Last Knight",
        year = "2017",
        id = "tt3371366",
        type = "movie",
        poster = "https://m.media-amazon.com/images/M/MV5BN2YwOWM4ODgtZTMzMi00ZmFmLTk5NTEtNmY4ZDcwNzQxNDhjXkEyXkFqcGdeQXVyNTI0NzAyNjY@._V1_SX300.jpg"
    )
}

@Composable
@Preview(name = "FilmItem Light Theme")
private fun FilmItemPreviewLight() {
    FilmTheme(useDarkTheme = false) {
        FilmItemScreen(
            item = mockFilm(),
            onOpenImdbLink = {},
            onDismiss = {}
        )
    }
}

@Composable
@Preview(name = "FilmItem Dark Theme")
private fun FilmItemPreviewDark() {
    FilmTheme(useDarkTheme = true) {
        FilmItemScreen(
            item = mockFilm(),
            onOpenImdbLink = {},
            onDismiss = {}
        )
    }
}

@Composable
@Preview(name = "FilmList Light Theme")
private fun FilmListPreviewLight() {
    FilmTheme(useDarkTheme = false) {
        FilmListScreen(
            listLoaded = true,
            itemsList = listOf(mockFilm()),
            resourceEmptyList = R.string.empty_list,
            onItemClick = {},
            showDialogSearch = false,
            onSearchTitleChanged = {},
            onSearchQntChanged = {},
            onDismissSearchDialog = {},
            onApplySearchDialog = {},
            onSearch = {}
        )
    }
}

@Composable
@Preview(name = "FilmList Dark Theme")
private fun FilmListPreviewDark() {
    FilmTheme(useDarkTheme = true) {
        FilmListScreen(
            listLoaded = true,
            itemsList = listOf(mockFilm()),
            resourceEmptyList = R.string.empty_list,
            onItemClick = {},
            showDialogSearch = false,
            onSearchTitleChanged = {},
            onSearchQntChanged = {},
            onDismissSearchDialog = {},
            onApplySearchDialog = {},
            onSearch = {}
        )
    }
}
