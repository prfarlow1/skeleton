package com.farlow.twitter

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test

class TwitterTest {

    private val logger = HttpLoggingInterceptor.Logger { message -> println(message) }

    @Test
    fun test() = runTest {
        val client = HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        prettyPrint = true
                        ignoreUnknownKeys = true
                    }
                )
            }
            engine {
                config {
                    addInterceptor(HttpLoggingInterceptor(logger).apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                }
            }
        }
        val twitter = Twitter(client)
        twitter.obtainAppOnlyAccessToken()
        val response = twitter.getLikes()
    }
}
