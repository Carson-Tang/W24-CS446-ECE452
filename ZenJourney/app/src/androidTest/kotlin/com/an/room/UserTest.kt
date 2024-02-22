package com.an.room

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.an.room.db.UserDB
import com.an.room.model.User
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserTest {
    @Test
    fun testUserCreation() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val database = UserDB.getDB(context)
        val userDao = database.userDao()

        // Create a sample user
        val user = User(firstName = "Carson", lastName = "Tang")

        // Insert the user into the database
        userDao.insert(user)

        // Retrieve the user from the database
        val savedUser = userDao.getAll().firstOrNull()

        // Assert that the user was saved successfully
        Assert.assertNotNull(savedUser)
        Assert.assertEquals(user.firstName, savedUser?.firstName)
        Assert.assertEquals(user.lastName, savedUser?.lastName)
    }
}
