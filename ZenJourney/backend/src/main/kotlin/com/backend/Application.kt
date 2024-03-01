package com.backend

import com.backend.domain.ports.JournalRepository
import com.backend.plugins.*
import io.ktor.server.application.*
import com.mongodb.kotlin.client.coroutine.MongoClient
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import io.ktor.server.tomcat.EngineMain
import com.backend.domain.ports.UserRepository
import com.backend.infrastructure.repository.JournalRepositoryImpl
import com.backend.infrastructure.repository.UserRepositoryImpl
import io.ktor.serialization.gson.gson
import io.ktor.server.plugins.contentnegotiation.*

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
                ("mongodb+srv://admin:admin@cluster0.m9o59vc.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0\n" +
                        "\n") ?: throw RuntimeException("Failed to access MongoDB URI.")
            ) }
            single { get<MongoClient>().getDatabase("ZenJourney") }
        }, module {
            single<UserRepository> { UserRepositoryImpl(get()) }
            single<JournalRepository> { JournalRepositoryImpl(get()) }
        })
    }
    configureMonitoring()
    configureRouting()
}
