package com.backend.domain.ports

import org.bson.BsonValue
import org.bson.types.ObjectId
import user.User

interface UserRepository {
    suspend fun insertOne(user: User): BsonValue?
    suspend fun deleteById(objectId: ObjectId): Long
    suspend fun findById(objectId: ObjectId): User?
}