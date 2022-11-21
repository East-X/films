package com.eastx7.films.api

import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import com.eastx7.films.data.Constants.SERVER_URL
import com.eastx7.films.data.OmdbFilms
import com.eastx7.films.data.OmdbSearch
import java.net.CookieManager
import java.net.CookiePolicy

interface ApiOmdb {

    @GET(".")
    suspend fun getListFilms(
        @Query("apikey") apikey: String,
        @Query("s") title: String,
        @Query("r") typeRequest: String,
    ): OmdbSearch

    @GET(".")
    suspend fun filmInfo(
        @Query("apikey") apikey: String,
        @Query("i") id: String,
    ): OmdbFilms

    companion object {
        fun getRetrofitBuilder(cookieManager: CookieManager): Retrofit.Builder {
            //TODO: remove logger in production
//            val logger =
//                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
//            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)

            val client = OkHttpClient.Builder()
//                .addInterceptor(logger)
                .cookieJar(JavaNetCookieJar(cookieManager))
                .build()

            return Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .client(client)
        }
    }
}

interface GsonService : ApiOmdb {
    companion object {
        fun create(cookieManager: CookieManager): GsonService {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            return ApiOmdb.getRetrofitBuilder(cookieManager)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(GsonService::class.java)
        }
    }
}