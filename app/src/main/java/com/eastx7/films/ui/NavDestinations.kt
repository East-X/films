package com.eastx7.films.ui

sealed class NavDestinations(val route: String) {

    object FilmList : NavDestinations("filmlist")
    object FilmItem : NavDestinations("filmitem") {
        const val itemWithArgument: String = "filmitem/{id}"
        const val argument0: String = "id"
    }
}
