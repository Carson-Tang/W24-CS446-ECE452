package com.backend

import com.backend.domain.ports.PhotoRepository
import com.backend.domain.ports.JournalRepository
import com.backend.plugins.*
import io.ktor.server.application.*
import com.mongodb.kotlin.client.coroutine.MongoClient
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import io.ktor.server.tomcat.EngineMain
import com.backend.domain.ports.UserRepository
import com.backend.infrastructure.repository.PhotoRepositoryImpl
import com.backend.infrastructure.repository.JournalRepositoryImpl
import com.backend.infrastructure.repository.UserRepositoryImpl
import com.backend.jwtConfig.JwtConfig
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.gson.gson
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.respond

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
//    configureSerialization()
    install(Authentication) {
        jwt("auth-jwt") {
            verifier(JwtConfig.verifier)
            validate {
                val name = it.payload.getClaim("name").asString()
                val password = it.payload.getClaim("password").asString()
                if (name != null && password != null) {
                    JWTPrincipal(it.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
    install(ContentNegotiation) {
        gson {
        }
    }
    install(Koin) {
        slf4jLogger()
        modules(module {
            single {
                MongoClient.create(
                    (System.getenv("MONGO_URI"))
                        ?: throw RuntimeException("Failed to access MongoDB URI.")
                )
            }
            single { get<MongoClient>().getDatabase("ZenJourney") }
        }, module {
            single<UserRepository> { UserRepositoryImpl(get()) }
            single<PhotoRepository> { PhotoRepositoryImpl(get()) }
            single<JournalRepository> { JournalRepositoryImpl(get()) }
        })
    }
    configureMonitoring()
    configureRouting()
}
