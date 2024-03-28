package ca.uwaterloo.cs.api

import com.google.gson.Gson
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import journal.JournalRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object JournalApiService {

    private val gson = Gson()
    private const val baseUrl = "http://10.0.2.2:8080/journal"

    suspend fun test(): HttpResponse {
        return withContext(Dispatchers.IO){
            HttpClientSetup.httpClient.get("http://10.0.2.2:8080/") {}
        }
    }
    suspend fun createJournal(journalRequest: JournalRequest, jwt: String): HttpResponse {
        val journalJson = gson.toJson(journalRequest)
        return withContext(Dispatchers.IO) {
            HttpClientSetup.httpClient.post(baseUrl) {
                contentType(ContentType.Application.Json)
                setBody(journalJson)
                headers {
                    append(HttpHeaders.Authorization, "Bearer $jwt")
                }
            }
        }
    }

    suspend fun updateJournal(id: String, journalRequest: JournalRequest): HttpResponse {
        val journalJson = gson.toJson(journalRequest)
        return withContext(Dispatchers.IO) {
            HttpClientSetup.httpClient.put("$baseUrl/$id") {
                contentType(ContentType.Application.Json)
                setBody(journalJson)
            }
        }
    }

    suspend fun deleteJournal(id: String): HttpResponse {
        return withContext(Dispatchers.IO) {
            HttpClientSetup.httpClient.delete("$baseUrl/$id")
        }
    }

    suspend fun getJournalByDate(year: Int, month: Int, day: Int, jwt: String): HttpResponse {
        return withContext(Dispatchers.IO) {
            val response: HttpResponse = HttpClientSetup.httpClient.get(baseUrl) {
                parameter("year", year)
                parameter("month", month)
                parameter("day", day)
                headers {
                    append(HttpHeaders.Authorization, "Bearer $jwt")
                }
            }
            response
        }
    }

    suspend fun getJournalByDateAndUser(userId: String, year: Int, month: Int, day: Int, jwt: String): HttpResponse {
        return withContext(Dispatchers.IO) {
            val response: HttpResponse = HttpClientSetup.httpClient.get("$baseUrl/user") {
                parameter("user", userId)
                parameter("year", year)
                parameter("month", month)
                parameter("day", day)
                headers {
                    append(HttpHeaders.Authorization, "Bearer $jwt")
                }
            }
            response
        }
    }

    suspend fun getJournalByMonth(userId: String, year: Int, month: Int, jwt: String) : HttpResponse {
        return withContext(Dispatchers.IO) {
            val response: HttpResponse = HttpClientSetup.httpClient.get("$baseUrl/user/month") {
                parameter("user", userId)
                parameter("year", year)
                parameter("month", month)
                headers {
                    append(HttpHeaders.Authorization, "Bearer $jwt")
                }
            }
            response
        }
    }

    suspend fun deleteJournalByUserId(userId: String, jwt: String): HttpResponse {
        return withContext(Dispatchers.IO) {
            val response: HttpResponse = HttpClientSetup.httpClient.delete("$baseUrl/user") {
                parameter("userId", userId)
                headers {
                    append(HttpHeaders.Authorization, "Bearer $jwt")
                }
            }
            response
        }
    }
}
