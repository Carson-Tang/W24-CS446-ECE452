package com.backend

import com.backend.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.jetty.*
import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoClient
import org.bson.Document
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import io.ktor.server.tomcat.EngineMain
import com.backend.domain.ports.UserRepository
import com.backend.infrastructure.repository.UserRepositoryImpl
import io.ktor.serialization.gson.gson
import io.ktor.server.plugins.contentnegotiation.*

data class User(val name: String, val num: Int)
fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
//    configureSerialization()
    install(ContentNegotiation) {
        gson {
        }
    }
    install(Koin) {
        slf4jLogger()
        modules(module {
            single { MongoClient.create(
                    "mongodb+srv://admin:admin@cluster0.m9o59vc.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0\n" +
                    "\n" ?: throw RuntimeException("Failed to access MongoDB URI.")
            ) }
            single { get<MongoClient>().getDatabase("ZenJourney") }
        }, module {
            single<UserRepository> { UserRepositoryImpl(get()) }
        })
    }
    configureMonitoring()
    configureRouting()
}
