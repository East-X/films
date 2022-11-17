package com.eastx7.films.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eastx7.films.R
import com.eastx7.films.data.OmdbFilms
import com.eastx7.films.data.OmdbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class FilmItemViewModel @Inject constructor(
    private val omdbRepository: OmdbRepository
) : ViewModel() {

    private val _liveFilm = MutableLiveData<OmdbFilms?>(null)
    val liveFilm: LiveData<OmdbFilms?> = _liveFilm

    private val _textEmptyFilm = MutableStateFlow(R.string.empty_list)
    val textEmptyFilm: StateFlow<Int> = _textEmptyFilm.asStateFlow()

    fun getFilmInfo(id: String) {
        viewModelScope.launch {
            try {
                val omdbFilm = omdbRepository.filmInfo(id)
                omdbFilm?.let {
                    _liveFilm.value = it
                }
            } catch (e: UnknownHostException) {
                _textEmptyFilm.value = R.string.unknown_host
            } catch (e: ConnectException) {
                _textEmptyFilm.value = R.string.no_internet
            } catch (e: Exception) {
                _textEmptyFilm.value = R.string.unknown_exception
            }
        }
    }
}