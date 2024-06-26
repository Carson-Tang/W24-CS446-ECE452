package com.an.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.an.room.model.Journal

@Dao
interface JournalDao {
    @Query("SELECT * FROM journals")
    fun getAll(): List<Journal>

    @Query("SELECT * FROM journals WHERE year = :year AND month = :month AND day = :day")
    fun findByDate(year: Int, month: Int, day: Int): Journal?

    @Query("SELECT * FROM journals WHERE year = :year AND month = :month")
    fun findByMonth(year: Int, month: Int): List<Journal>
    @Insert
    fun insertAll(vararg journals: Journal)

    @Insert
    fun insert(journal: Journal)

    @Delete
    fun delete(journal: Journal)

    @Update
    fun update(journal: Journal)
    @Query("DELETE FROM journals")
    fun deleteAll()
}