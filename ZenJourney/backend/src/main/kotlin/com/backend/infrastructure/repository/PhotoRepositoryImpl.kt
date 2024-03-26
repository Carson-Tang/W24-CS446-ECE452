package com.backend.infrastructure.repository

import com.backend.domain.ports.PhotoRepository
import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.Document
import org.bson.types.ObjectId
import photo.Photo

class PhotoRepositoryImpl(
    private val mongoDatabase: MongoDatabase
) : PhotoRepository {
    companion object {
        const val PHOTO_COLLECTION = "photos"
    }
    override suspend fun insertPhoto(photo: Photo): String? {
        try {
            val result = mongoDatabase.getCollection<Photo>(PHOTO_COLLECTION).insertOne(
                photo
            )
            return result.insertedId?.asObjectId()?.value.toString()
        } catch (e: MongoException) {
            System.err.println("Unable to insertPhoto due to an error: $e")
        }
        return null
    }
    override suspend fun deleteById(id: ObjectId): Long{
        try {
            val result = mongoDatabase.getCollection<Photo>(PHOTO_COLLECTION)
                .deleteOne(Filters.eq("_id", id))
            return result.deletedCount
        } catch (e: MongoException) {
            System.err.println("Unable to deleteById due to an error: $e")
        }
        return 0
    }
    override suspend fun deleteByUserId(userid: ObjectId): Long {
        try {
            val result = mongoDatabase.getCollection<Photo>(PHOTO_COLLECTION)
                .deleteMany(Filters.eq("userid", userid))
            return result.deletedCount
        } catch (e: MongoException) {
            System.err.println("Unable to deleteByUserId due to an error: $e")
        }
        return -1
    }
    override suspend fun findByUserId(userid: ObjectId): List<Photo> =
        mongoDatabase.getCollection<Photo>(PHOTO_COLLECTION)
            .find(Filters.eq("userid", userid))
            .sort(Document(mapOf("year" to 1, "month" to 1, "day" to 1)))
            .toList()
    override suspend fun findById(id: ObjectId): Photo? =
        mongoDatabase.getCollection<Photo>(PHOTO_COLLECTION)
            .find(Filters.eq("_id", id))
            .firstOrNull()
}