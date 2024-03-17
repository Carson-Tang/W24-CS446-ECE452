package ca.uwaterloo.cs.api

import com.google.gson.Gson
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import user.UserRequest

object UserApiService {

    val gson = Gson()
    private val baseUrl = "http://10.0.2.2:8080/user"

    suspend fun test(): HttpResponse {
        return withContext(Dispatchers.IO){
            HttpClientSetup.httpClient.get("http://10.0.2.2:8080/") {}
        }
    }

    suspend fun createUser(userRequest: UserRequest): HttpResponse {
        val userJson = gson.toJson(userRequest)
        return withContext(Dispatchers.IO) {
            HttpClientSetup.httpClient.post("$baseUrl") {
                contentType(ContentType.Application.Json)
                setBody(userJson)
            }
        }
    }

    suspend fun loginUser(userRequest: UserRequest): HttpResponse {
        val userJson = gson.toJson(userRequest)
        return withContext(Dispatchers.IO) {
            HttpClientSetup.httpClient.post("$baseUrl/login") {
                contentType(ContentType.Application.Json)
                setBody(userJson)
            }
        }
    }
}
