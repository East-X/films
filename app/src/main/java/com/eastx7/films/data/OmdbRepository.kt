package com.eastx7.films.data

import android.content.Context
import android.os.Build
import android.util.Log
import android.webkit.MimeTypeMap
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import com.eastx7.films.MainActivity
import com.eastx7.films.R
import com.eastx7.films.api.GsonService
import com.eastx7.films.BuildConfig
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Query
import java.io.*
import java.lang.Exception
import java.net.ConnectException
import java.net.UnknownHostException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OmdbRepository @Inject constructor(
    private var serviceGson: GsonService
) {

    suspend fun listFilms(
        title: String,
    ): OmdbSearch? {
        return serviceGson.getListFilms(BuildConfig.OmdbAPIKey, title, "json")
    }

    suspend fun filmInfo(id: String): OmdbFilms? {
        return serviceGson.filmInfo(BuildConfig.OmdbAPIKey, id)
    }
}
