package com.backend.domain.ports

import com.backend.domain.entity.Photo
import org.bson.BsonValue
import org.bson.types.ObjectId

interface PhotoRepository {
    suspend fun insertPhoto(photo: Photo): BsonValue?
    suspend fun deleteById(id: ObjectId): Long
    suspend fun deleteByUserId(userid: ObjectId): Long
    suspend fun findByUserId(userid: ObjectId): List<Photo>?
    suspend fun findById(id: ObjectId): Photo?
}