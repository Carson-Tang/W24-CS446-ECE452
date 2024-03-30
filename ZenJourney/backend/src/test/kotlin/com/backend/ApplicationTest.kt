package com.backend

import com.backend.plugins.*
import com.google.gson.Gson
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.koin.core.context.GlobalContext.stopKoin
import photo.ListResponse
import photo.PhotoRequest
import photo.PhotoResponse
import kotlin.test.*

class ApplicationTest {
    @AfterTest
    fun tearDownTest() {
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
    fun testPhoto_PostFindDelete() = testApplication {
        application {
            configureRouting()
        }
        val gson = Gson()
        val userid = "65e3652d9501eb63529692fa"
        val req = PhotoRequest(userid, "arbitrarytestvalue", 2024, 3, 1)
        client.post("/photo") {
            setBody(gson.toJson(req))
            contentType(ContentType.Application.Json)
        }.apply {
            assertEquals(HttpStatusCode.Created, status)

            var map: Map<String, String> = HashMap()
            map = gson.fromJson(bodyAsText(), map.javaClass)

            client.get("/photo/${map["id"]}").apply {
                assertEquals(HttpStatusCode.OK, status)
                val res = gson.fromJson(bodyAsText(), PhotoResponse::class.java)
                assertEquals(
                    res,
                    PhotoResponse(map["id"]!!, req.userid, req.photoBase64, req.year, req.month, req.day)
                )
            }

            client.delete("/photo/${map["id"]}").apply {
                assertEquals(HttpStatusCode.OK, status)
            }
        }
    }

    @Test
    fun testPhoto_ByUser_FindDelete() = testApplication {
        application {
            configureRouting()
        }
        val gson = Gson()
        val userid = "65e3652d9501eb63529692fa"
        val req = PhotoRequest(userid, "arbitrarytestvalue", 2024, 3, 1)
        client.post("/photo") {
            setBody(gson.toJson(req))
            contentType(ContentType.Application.Json)
        }.apply {
            assertEquals(HttpStatusCode.Created, status)

            client.get("/photo/byuserid/$userid").apply {
                assertEquals(HttpStatusCode.OK, status)
                val res = gson.fromJson(bodyAsText(), ListResponse::class.java)
                assertTrue(res.list.isNotEmpty())
            }

            client.delete("/photo/byuserid/$userid").apply {
                assertEquals(HttpStatusCode.OK, status)
            }
        }
    }
}
