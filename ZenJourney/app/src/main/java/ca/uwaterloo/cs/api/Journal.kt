package ca.uwaterloo.cs.api

import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import journal.JournalRequest
import journal.JournalResponse
import io.ktor.http.*
import com.google.gson.Gson
import io.ktor.client.call.body

object ApiService {

    val gson = Gson()
    private val baseUrl = "http://10.0.2.2:8080/journal"

    suspend fun test(): HttpResponse {
        return withContext(Dispatchers.IO){
            HttpClientSetup.httpClient.get("http://10.0.2.2:8080/") {}
        }
    }
    suspend fun createJournal(journalRequest: JournalRequest): HttpResponse {
        val journalJson = gson.toJson(journalRequest)
        return withContext(Dispatchers.IO) {
            HttpClientSetup.httpClient.post("$baseUrl") {
                contentType(ContentType.Application.Json)
                setBody(journalJson)
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

    suspend fun getJournalByDate(year: Int, month: Int, day: Int): HttpResponse {
        return withContext(Dispatchers.IO) {
            HttpClientSetup.httpClient.get("$baseUrl") {
                parameter("year", year)
                parameter("month", month)
                parameter("day", day)
            }
        }
    }

    suspend fun getJournalByDateAndUser(userId: String, year: Int, month: Int, day: Int): JournalResponse? {
        return withContext(Dispatchers.IO) {
            val response: HttpResponse = HttpClientSetup.httpClient.get("$baseUrl/user") {
                parameter("user", userId)
                parameter("year", year)
                parameter("month", month)
                parameter("day", day)
            }

            if (response.status == HttpStatusCode.OK) {
                response.body()
            } else {
                null
            }
        }
    }
}
