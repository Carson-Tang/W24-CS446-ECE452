package com.an.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.an.room.model.Photo

@Dao
interface PhotoDao {
    @Query("SELECT * FROM photos WHERE uploadDate = :uploadDate LIMIT 1")
    fun findByUploadDate(uploadDate: String): Photo

    @Insert
    fun insertAll(vararg photos: Photo)

    @Insert
    fun insert(photo: Photo)

    @Delete
    fun delete(photo: Photo)
}