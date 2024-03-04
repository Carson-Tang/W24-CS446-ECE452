package com.backend.domain.ports

import org.bson.types.ObjectId
import photo.Photo

interface PhotoRepository {
    suspend fun insertPhoto(photo: Photo): String?
    suspend fun deleteById(id: ObjectId): Long
    suspend fun deleteByUserId(userid: ObjectId): Long
    suspend fun findByUserId(userid: ObjectId): List<Photo>?
    suspend fun findById(id: ObjectId): Photo?
}