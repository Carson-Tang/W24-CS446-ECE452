package ca.uwaterloo.cs.api

import com.google.gson.Gson
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import photo.PhotoRequest

object PhotoApiService {
    val gson = Gson()
    private val baseUrl = "http://10.0.2.2:8080/photo"

    suspend fun test(): HttpResponse {
        return withContext(Dispatchers.IO) {
            HttpClientSetup.httpClient.get("http://10.0.2.2:8080/") {}
        }
    }

    suspend fun createPhoto(photoRequest: PhotoRequest, jwt: String): HttpResponse {
        val photoJson = gson.toJson(photoRequest)
        return withContext(Dispatchers.IO) {
            HttpClientSetup.httpClient.post(baseUrl) {
                contentType(ContentType.Application.Json)
                setBody(photoJson)
                headers {
                    append(HttpHeaders.Authorization, "Bearer $jwt")
                }
            }
        }
    }

    suspend fun deletePhoto(id: String, jwt: String): HttpResponse {
        return withContext(Dispatchers.IO) {
            HttpClientSetup.httpClient.delete("$baseUrl/$id") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $jwt")
                }
            }
        }
    }

    suspend fun deleteUserPhotos(userid: String, jwt: String): HttpResponse {
        return withContext(Dispatchers.IO) {
            HttpClientSetup.httpClient.delete("$baseUrl/byuserid/$userid") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $jwt")
                }
            }
        }
    }

    suspend fun getPhoto(id: String, jwt: String): HttpResponse {
        return withContext(Dispatchers.IO) {
            HttpClientSetup.httpClient.get("$baseUrl/$id") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $jwt")
                }
            }
        }
    }

    suspend fun getAllUserPhotos(
        userid: String, jwt: String
    ): HttpResponse {
        return withContext(Dispatchers.IO) {
            HttpClientSetup.httpClient.get("$baseUrl/byuserid/$userid") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $jwt")
                }
            }
        }
    }
}
