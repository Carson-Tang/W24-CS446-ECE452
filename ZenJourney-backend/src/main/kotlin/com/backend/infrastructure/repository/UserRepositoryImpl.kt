package com.backend.infrastructure.repository

import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import com.backend.domain.entity.User
import com.backend.domain.ports.UserRepository
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import org.bson.BsonValue
import org.bson.types.ObjectId

class UserRepositoryImpl(
        private val mongoDatabase: MongoDatabase
) : UserRepository {

    companion object {
        const val USER_COLLECTION = "users"
    }

    override suspend fun insertOne(user: User): BsonValue? {
        try {
            val result = mongoDatabase.getCollection<User>(USER_COLLECTION).insertOne(
                    user
            )
            return result.insertedId
        } catch (e: MongoException) {
            System.err.println("Unable to insert due to an error: $e")
        }
        return null
    }

    override suspend fun deleteById(objectId: ObjectId): Long {
        try {
            val result = mongoDatabase.getCollection<User>(USER_COLLECTION).deleteOne(Filters.eq("_id", objectId))
            return result.deletedCount
        } catch (e: MongoException) {
            System.err.println("Unable to delete due to an error: $e")
        }
        return 0
    }

    override suspend fun findById(objectId: ObjectId): User? =
            mongoDatabase.getCollection<User>(USER_COLLECTION).withDocumentClass<User>()
                    .find(Filters.eq("_id", objectId))
                    .firstOrNull()
}