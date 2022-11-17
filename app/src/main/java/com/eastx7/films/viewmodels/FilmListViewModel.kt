package com.eastx7.films.viewmodels

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.eastx7.films.R
import com.eastx7.films.data.OmdbFilms
import com.eastx7.films.data.OmdbRepository
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class FilmListViewModel @Inject constructor(
    private val omdbRepository: OmdbRepository
) : ViewModel() {

    private val _liveListFilms = MutableLiveData<List<OmdbFilms>>(listOf())
    val liveListFilms: LiveData<List<OmdbFilms>> = _liveListFilms

    private val _listLoaded = MutableStateFlow(false)
    val listLoaded: StateFlow<Boolean> = _listLoaded.asStateFlow()

    private val _showDialogSearch = MutableStateFlow(false)
    val showDialogSearch: StateFlow<Boolean> = _showDialogSearch.asStateFlow()

    private val _textEmptyList = MutableStateFlow(R.string.empty_list)
    val textEmptyList: StateFlow<Int> = _textEmptyList.asStateFlow()

    private var filmTitle = ""
    private var filmQnt = 1

    fun openDialogSearch() {
        _showDialogSearch.value = true
    }

    fun closeDialogSearch() {
        _showDialogSearch.value = false
    }

    fun textFilmQntChanged(qnt: Int) {
        filmQnt = qnt
    }

    fun textFilmTitleChanged(title: String) {
        filmTitle = title
    }

    fun populateListFilms() {
        viewModelScope.launch {
            try {
                val omdbSearch = omdbRepository.listFilms(filmTitle)
                omdbSearch?.let {
                    if (it.error != null) {
                        _textEmptyList.value = R.string.movie_not_found
                        _liveListFilms.value = listOf()
                        _listLoaded.value = false
                    } else {
                        val listFilms = omdbSearch.search
                        listFilms?.let { list ->
                            _liveListFilms.value = list.take(filmQnt)
                            if (list.isNotEmpty()) {
                                _listLoaded.value = true
                            }
                        }
                    }
                }
            } catch (e: UnknownHostException) {
                _textEmptyList.value = R.string.unknown_host
            } catch (e: ConnectException) {
                _textEmptyList.value = R.string.no_internet
            } catch (e: Exception) {
                _textEmptyList.value = R.string.unknown_exception
            }
        }
    }
}