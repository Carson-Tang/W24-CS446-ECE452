package com.backend.domain.ports

import org.bson.BsonValue
import org.bson.types.ObjectId
import user.User
import user.UserRequest

interface UserRepository {
    suspend fun insertOne(user: User): BsonValue?
    suspend fun updateOne(userId: ObjectId, user: UserRequest): Long
    suspend fun deleteById(objectId: ObjectId): Long
    suspend fun findById(objectId: ObjectId): User?
    suspend fun findByEmail(email: String): User?
}