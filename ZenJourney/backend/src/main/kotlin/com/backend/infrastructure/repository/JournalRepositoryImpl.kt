package com.backend.infrastructure.repository

import com.backend.domain.ports.JournalRepository
import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.client.result.DeleteResult
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import journal.Journal
import journal.JournalRequest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.BsonValue
import org.bson.types.ObjectId

class JournalRepositoryImpl(
    private val mongoDatabase: MongoDatabase
) : JournalRepository {
    companion object {
        const val JOURNAL_COLLECTION = "journals"
    }

    override suspend fun insertOne(journal: Journal): BsonValue? {
        try {
            val result = mongoDatabase.getCollection<Journal>(JOURNAL_COLLECTION).insertOne(
                journal
            )
            return result.insertedId
        } catch (e: MongoException) {
            System.err.println("Unable to insert a journal due to an error: $e")
        }
        return null
    }

    override suspend fun updateOne(journalId: ObjectId, journal: JournalRequest): Long {
        try {
            val result = mongoDatabase.getCollection<Journal>(JOURNAL_COLLECTION).updateOne(
                Filters.eq("_id", journalId),
                Updates.combine(
                    Updates.set("moods", journal.moods),
                    Updates.set("content", journal.content)
                )
            )
            return result.modifiedCount
        } catch (e: MongoException) {
            System.err.println("Unable to update a journal due to an error: $e")
        }
        return 0
    }

    override suspend fun deleteById(objectId: ObjectId): Long {
        try {
            val result = mongoDatabase.getCollection<Journal>(JOURNAL_COLLECTION)
                .deleteOne(Filters.eq("_id", objectId))
            return result.deletedCount
        } catch (e: MongoException) {
            System.err.println("Unable to delete a journal due to an error: $e")
        }
        return 0
    }

    override suspend fun findByDate(year: Int, month: Int, day: Int): Journal? {
        return mongoDatabase.getCollection<Journal>(JOURNAL_COLLECTION).withDocumentClass<Journal>()
            .find(
                Filters.and(
                    Filters.eq("year", year),
                    Filters.eq("month", month),
                    Filters.eq("day", day)
                )
            )
            .firstOrNull()
    }

    override suspend fun findByDateAndUser(userId: String, year: Int, month: Int, day: Int): Journal? {
        return mongoDatabase.getCollection<Journal>(JOURNAL_COLLECTION).withDocumentClass<Journal>()
            .find(
                Filters.and(
                    Filters.eq("year", year),
                    Filters.eq("month", month),
                    Filters.eq("day", day),
                    Filters.eq("userId", ObjectId(userId))
                )
            )
            .firstOrNull()
    }

    override suspend fun findByMonth(userId: String, year: Int, month: Int): List<Journal> {
        return try {
            mongoDatabase.getCollection<Journal>(JOURNAL_COLLECTION)
                .find(
                    Filters.and(
                        Filters.eq("userId", ObjectId(userId)),
                        Filters.eq("year", year),
                        Filters.eq("month", month)
                    )
                ).toList()
        } catch (e: MongoException) {
            System.err.println("Unable to retrieve journals due to an error: $e")
            emptyList()
        }
    }
    override suspend fun deleteByUserId(userId: String): DeleteResult {
        return mongoDatabase.getCollection<Journal>(JOURNAL_COLLECTION).withDocumentClass<Journal>()
            .deleteMany(
                Filters.eq("userId", ObjectId(userId)
            )
        )
    }
}