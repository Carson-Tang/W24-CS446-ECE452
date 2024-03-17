package com.an.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.an.room.dao.PhotoDao
import com.an.room.model.Photo


@Database(entities = [Photo::class], version = 1)
abstract  class PhotoDB : RoomDatabase() {
    abstract fun photoDao() : PhotoDao
    companion object {
        @Volatile
        private var INSTANCE: PhotoDB? = null

        fun getDB(context: Context): PhotoDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PhotoDB::class.java,
                    "photo_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}