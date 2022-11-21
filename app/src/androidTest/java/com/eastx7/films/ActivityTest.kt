package com.eastx7.films

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.hilt.navigation.compose.hiltViewModel
import com.eastx7.films.theme.FilmTheme
import com.eastx7.films.ui.FilmListScreen
import com.eastx7.films.viewmodels.FilmListViewModel
import org.junit.Before

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class FilmListScreenTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var activity: MainActivity

    @Before
    fun init() {
        composeTestRule.activityRule.scenario.onActivity {
            activity = it
        }
    }

    @Test
    fun posterDetailsFrozenIILoadingTest() {
        composeTestRule.setContent {
            FilmTheme {

                val viewModel = hiltViewModel<FilmListViewModel>()
                viewModel.populateListFilms()

                FilmListScreen(
                    listLoaded = false,
                    itemsList = listOf(),
                    resourceEmptyList = R.string.empty_list,
                    onSearchTitleChanged = { },
                    onSearchQntChanged = { },
                    onItemClick = { },
                    showDialogSearch = false,
                    onSearch = { },
                    onApplySearchDialog = { },
                    onDismissSearchDialog = { }
                )
            }
        }

        composeTestRule
            .onNodeWithText("Nothing is here", ignoreCase = true)
            .assertIsDisplayed()
    }
}