package com.eastx7.films.data

import com.google.gson.annotations.SerializedName

class OmdbSearch {
    @SerializedName("Search")
    var search: List<OmdbFilms>? = null

    @SerializedName("totalResults")
    var totalResults: String? = null

    @SerializedName("Response")
    var response: String? = null

    @SerializedName("Error")
    var error: String? = null
}