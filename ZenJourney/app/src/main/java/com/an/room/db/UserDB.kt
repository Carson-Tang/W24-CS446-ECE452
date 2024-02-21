package com.an.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.an.room.dao.UserDao
import com.an.room.model.User


@Database(entities = [User::class], version = 1)
abstract  class UserDB : RoomDatabase() {
    abstract fun userDao() : UserDao
    companion object {
        @Volatile
        private var INSTANCE: UserDB? = null

        fun getDB(context: Context): UserDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDB::class.java,
                    "user_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}