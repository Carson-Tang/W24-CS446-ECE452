package com.backend.domain.ports

import com.mongodb.client.result.DeleteResult
import journal.Journal
import journal.JournalRequest
import org.bson.BsonValue
import org.bson.types.ObjectId

interface JournalRepository {
    suspend fun insertOne(journal: Journal): BsonValue?
    suspend fun updateOne(journalId: ObjectId, journal: JournalRequest): Long
    suspend fun deleteById(objectId: ObjectId): Long
    suspend fun findByDate(year: Int, month: Int, day: Int): Journal?

    suspend fun findByDateAndUser(userId: String, year: Int, month: Int, day: Int): Journal?

    suspend fun deleteByUserId(userId: String): DeleteResult
}