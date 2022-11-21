package com.eastx7.films.network

import com.eastx7.films.api.GsonService
import com.eastx7.films.data.OmdbRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import java.net.CookieManager

class NetworkTest {

    private lateinit var repository: OmdbRepository

    @Before
    fun setUp() {
        repository = OmdbRepository(GsonService.create(CookieManager()))
    }

    @After
    fun tearDown() {

    }

    @Test
    fun filmListAsExpected() {
        runBlocking {
            val searchResponse = repository.listFilms("transformers")
            assertTrue(searchResponse!!.search!!.size != 0)
            assertTrue(searchResponse!!.error == null)

            val searchErrorResponse = repository.listFilms("ghghhdtryugk")
            assertThat(searchErrorResponse!!.error).isEqualTo("Movie not found!")
        }
    }
}