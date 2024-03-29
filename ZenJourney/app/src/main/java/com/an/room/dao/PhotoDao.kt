package com.an.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.an.room.model.Journal
import com.an.room.model.Photo

@Dao
interface PhotoDao {
    @Query("SELECT * FROM photos WHERE year = :year AND month = :month AND day = :day")
    fun findByUploadDate(year: Int, month: Int, day: Int): Photo

    @Query("SELECT * FROM photos")
    fun getAll(): List<Photo>

    @Insert
    fun insertAll(vararg photos: Photo)

    @Insert
    fun insert(photo: Photo)

    @Delete
    fun delete(photo: Photo)

    @Query("DELETE FROM photos")
    fun deleteAll()
}