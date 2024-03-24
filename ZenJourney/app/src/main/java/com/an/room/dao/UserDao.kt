package com.an.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.an.room.model.User

@Dao
interface UserDao {
    // assume if user is using local DB then it is just for persistent storage
    // otherwise they would be using the cloud
    // this means if user chooses to not use the cloud, they cannot pick a new name
    // unless they delete the app and redownload
    @Query("SELECT * FROM users")
    fun getAll(): List<User>

    @Query("SELECT * FROM users LIMIT 1")
    fun getOne(): List<User>

    @Query("SELECT * FROM users WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM users WHERE firstName LIKE :first LIMIT 1")
    fun findByName(first: String): User

    @Insert
    fun insertAll(vararg users: User)

    @Insert
    fun insert(user: User)

    @Query("UPDATE users SET pin = :newPIN")
    fun updatePINById(newPIN: String)

    @Query("UPDATE users SET useJournalForAffirmations = :useJournalForAffirmations")
    fun updateUseJournalForAffirmationsById(useJournalForAffirmations: Boolean)

    @Delete
    fun delete(user: User)

    @Query("DELETE FROM users")
    fun deleteAll()
}