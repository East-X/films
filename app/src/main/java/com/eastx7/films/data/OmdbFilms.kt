package com.eastx7.films.data

import com.google.gson.annotations.SerializedName

class OmdbFilms {

    @SerializedName("Title")
    var title: String? = null

    @SerializedName("Year")
    var year: String? = null

    @SerializedName("imdbID")
    var id: String? = null

    @SerializedName("Type")
    var type: String? = null

    @SerializedName("Poster")
    var poster: String? = null
}