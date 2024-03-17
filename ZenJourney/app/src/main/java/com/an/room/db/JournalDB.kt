package com.an.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.an.room.dao.JournalDao
import com.an.room.model.Journal


@Database(entities = [Journal::class], version = 1)
abstract  class JournalDB : RoomDatabase() {
    abstract fun journalDao() : JournalDao
    companion object {
        @Volatile
        private var INSTANCE: JournalDB? = null

        fun getDB(context: Context): JournalDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    JournalDB::class.java,
                    "journal_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}