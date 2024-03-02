package com.backend.infrastructure.repository

import com.backend.domain.entity.Photo
import com.backend.domain.ports.PhotoRepository
import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.BsonValue
import org.bson.Document
import org.bson.types.ObjectId

class PhotoRepositoryImpl(
    private val mongoDatabase: MongoDatabase
) : PhotoRepository {
    companion object {
        const val PHOTO_COLLECTION = "photos"
    }
    override suspend fun insertPhoto(photo: Photo): BsonValue?{
        try {
            val result = mongoDatabase.getCollection<Photo>(PHOTO_COLLECTION).insertOne(
                photo
            )
            return result.insertedId
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
        mongoDatabase.getCollection<Photo>(PHOTO_COLLECTION).withDocumentClass<Photo>()
            .find(Filters.eq("userid", userid))
            .sort(Document("uploadDate", 1)) // sort by uploadDate string in ascending order
            .toList()
    override suspend fun findById(id: ObjectId): Photo? =
        mongoDatabase.getCollection<Photo>(PHOTO_COLLECTION).withDocumentClass<Photo>()
            .find(Filters.eq("_id", id))
            .firstOrNull()
}