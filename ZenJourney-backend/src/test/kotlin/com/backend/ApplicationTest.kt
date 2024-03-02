package com.backend

import com.backend.application.request.PhotoRequest
import com.backend.plugins.*
import com.google.gson.Gson
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.bson.types.ObjectId
import org.koin.core.context.GlobalContext.stopKoin
import kotlin.test.*

class ApplicationTest {

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }

    @Test
    fun testUserGet() = testApplication {
        application {
            configureRouting()
        }
        client.get("/user/65dec3b8a383267509169eaa").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(
                "{\"id\":\"65dec3b8a383267509169eaa\",\"name\":\"testing\",\"email\":\"testing@test.com\",\"password\":\"password\"}",
                bodyAsText()
            )
        }
    }

    @Test
    fun testPhotoPost() = testApplication {
        application {
            configureRouting()
        }
        val gson = Gson()
        client.post("/photo"){
            setBody(gson.toJson(PhotoRequest("65dec3b8a383267509169eaa", "arbitrarytestvalue", "2024-03-01")))
            contentType(ContentType.Application.Json)
        }.apply {
            assertEquals(HttpStatusCode.Created, status)
            val id = bodyAsText()
            println("id:$id")
        }
    }
}
