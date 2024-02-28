package com.backend.domain.ports

import com.backend.domain.entity.User
import org.bson.BsonValue
import org.bson.types.ObjectId

interface UserRepository {
    suspend fun insertOne(user: User): BsonValue?
    suspend fun deleteById(objectId: ObjectId): Long
    suspend fun findById(objectId: ObjectId): User?
}