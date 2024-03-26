package ca.uwaterloo.cs.userstrategy

import ca.uwaterloo.cs.AppState
import ca.uwaterloo.cs.PageStates
import com.an.room.db.JournalDB
import com.an.room.db.PhotoDB
import com.an.room.db.UserDB
import com.an.room.model.User
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class LocalUserStrategy : UserStrategy {
    override val forgotPINLabel = "Exit, clear all data"
    override val logoutLabel = "Clear data"
    override val deleteAccountLabel = ""
    override suspend fun loadUserSettings(appState: AppState) {
        val database = UserDB.getDB(appState.context)
        val userDao = database.userDao()

        val user = withContext(Dispatchers.IO) {
            userDao.getOne().getOrNull(0)
        }
        if (user == null) {
            appState.pageState.value = PageStates.WELCOME
            appState.setPageHistoryToWelcome()
        }
        user?.let {
            withContext(Dispatchers.Main) {
                appState.nameState.value = user.firstName
                appState.useJournalForAffirmations.value = user.useJournalForAffirmations
                appState.hashedPIN.value = user.pin
                if (appState.hashedPIN.value.isNotEmpty()) {
                    appState.isPINRequired.value = true
                }
                appState.pageState.value = PageStates.HOME
                appState.setPageHistoryToHome()
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun storeUserSettings(appState: AppState) {
        val pin = appState.hashedPIN.value.ifEmpty { "" }
        val user = User(
            firstName = appState.nameState.value,
            useCloud = false,
            useJournalForAffirmations = appState.useJournalForAffirmations.value,
            pin = pin
        )

        val database = UserDB.getDB(appState.context)
        val userDao = database.userDao()

        GlobalScope.launch {
            val userRes = userDao.getOne().getOrNull(0)
            if (userRes == null) {
                userDao.insert(user)
            } else {
                // when user enables PIN from the settings page
                userDao.updateUseJournalForAffirmationsById(appState.useJournalForAffirmations.value)
                userDao.updatePINById(pin)
            }
        }
    }

    override fun logout(appState: AppState) {
        runBlocking {
            appState.resetToDefault()
            withContext(Dispatchers.IO) {
                val userDB = UserDB.getDB(appState.context)
                val userDao = userDB.userDao()
                userDao.deleteAll()

                val photoDB = PhotoDB.getDB(appState.context)
                val photoDao = photoDB.photoDao()
                photoDao.deleteAll()

                val journalDB = JournalDB.getDB(appState.context)
                val journalDao = journalDB.journalDao()
                journalDao.deleteAll()
            }
        }
    }

    override fun deleteAccount(appState: AppState): Pair<Boolean, Boolean> { return Pair(false, false) }

    override fun clearJWT(appState: AppState) {}
}