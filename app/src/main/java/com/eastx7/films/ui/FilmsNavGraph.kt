package com.eastx7.films.ui

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.eastx7.films.data.Constants
import com.eastx7.films.viewmodels.FilmItemViewModel
import com.eastx7.films.viewmodels.FilmListViewModel

@Composable
fun FilmNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String,
) {
//TODO: Remove in production
//    navController.addOnDestinationChangedListener { controller, destination, arguments ->
//        destination.route?.let { navigatedRoute ->
//            Log.d("DestinationChanged", "Route: $navigatedRoute")
//        }
//    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        composable(
            route = NavDestinations.FilmItem.itemWithArgument,
            arguments = listOf(
                navArgument(NavDestinations.FilmItem.argument0) {
                    type = NavType.StringType; defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val itemViewModel: FilmItemViewModel = hiltViewModel()
            val item by itemViewModel.liveFilm.observeAsState(initial = null)
            val idItem =
                backStackEntry.arguments?.getString(NavDestinations.FilmItem.argument0)
                    ?: return@composable
            itemViewModel.getFilmInfo(idItem)
            val intent = remember {
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(Constants.IMDB_TITLE_URL + idItem)
                )
            }
            val context = LocalContext.current
            FilmItemScreen(
                item = item,
                onOpenImdbLink = {
                    context.startActivity(intent)
                },
                onDismiss = {
                    navController.navigateUp()
                }
            )
        }

        composable(
            route = NavDestinations.FilmList.route
        ) { _ ->

            val listViewModel: FilmListViewModel = hiltViewModel()
            val showDialogSearch: Boolean by listViewModel.showDialogSearch.collectAsState()
            val listLoaded: Boolean by listViewModel.listLoaded.collectAsState()
            val itemsList by listViewModel.liveListFilms.observeAsState(initial = listOf())
            val resourceEmptyList: Int by listViewModel.textEmptyList.collectAsState()

            FilmListScreen(
                listLoaded = listLoaded,
                itemsList = itemsList,
                resourceEmptyList = resourceEmptyList,
                onSearchTitleChanged = { title ->
                    listViewModel.textFilmTitleChanged(title)
                },
                onSearchQntChanged = { qnt ->
                    listViewModel.textFilmQntChanged(qnt)
                },
                onItemClick = {
                    val savedStateHandle = navController.previousBackStackEntry
                        ?.savedStateHandle
                    savedStateHandle?.set(NavDestinations.FilmItem.argument0, it.id)
                    navController.navigate("${NavDestinations.FilmItem.route}/${it.id}")
                },
                showDialogSearch = showDialogSearch,
                onSearch = {
                    listViewModel.openDialogSearch()
                },
                onApplySearchDialog = {
                    listViewModel.populateListFilms()
                    listViewModel.closeDialogSearch()
                },
                onDismissSearchDialog = {
                    listViewModel.closeDialogSearch()
                }
            )
        }
    }
}