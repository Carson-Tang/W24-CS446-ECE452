package ca.uwaterloo.cs.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.gson.gson
object HttpClientSetup {
    val httpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            gson {
            }
        }
    }
}
