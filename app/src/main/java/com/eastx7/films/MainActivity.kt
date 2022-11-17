package com.eastx7.films

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import com.eastx7.films.theme.FilmTheme
import com.eastx7.films.ui.NavDestinations
import com.eastx7.films.ui.FilmNavGraph

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var arguments: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments = intent.extras
        setContent {
            FilmTheme {
                Row(
                    Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .windowInsetsPadding(
                            WindowInsets
                                .navigationBars
                                .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
                        )
                ) {
                    FilmNavGraph(
                        startDestination = NavDestinations.FilmList.route,
                    )
                }
            }
        }
    }
}
