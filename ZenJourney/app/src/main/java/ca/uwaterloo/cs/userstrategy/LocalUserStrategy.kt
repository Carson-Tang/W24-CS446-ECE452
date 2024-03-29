package ca.uwaterloo.cs.userstrategy

import ca.uwaterloo.cs.AppState
import ca.uwaterloo.cs.PageStates
import ca.uwaterloo.cs.api.JournalApiService
import com.an.room.db.JournalDB
import com.an.room.db.PhotoDB
import com.an.room.db.UserDB
import com.an.room.model.User
import journal.JournalRequest
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import journal.JournalResponse
import java.time.LocalDate
import com.an.room.model.Journal
import com.an.room.model.Photo
import photo.PhotoRequest
import photo.PhotoResponse

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

    override suspend fun getJournalByDate(appState: AppState, day: Int, month: Int, year: Int): JournalResponse? {
        return withContext(Dispatchers.IO) {
            val database = JournalDB.getDB(appState.context)
            val journalDao = database.journalDao()

            val journalRes = journalDao.findByDate(
                year = year,
                month = month,
                day = day
            )

            withContext(Dispatchers.Main) {
                if (journalRes == null) {
                    null
                } else {
                    appState.pastJournalEntry.value = journalRes.content
                    appState.pastSelectedMoods.value = journalRes.moods
                    appState.pastDate.value = LocalDate.of(journalRes.year, journalRes.month, journalRes.day)
                    appState.pageState.value = PageStates.PAST_JOURNAL

                    JournalResponse(
                        id = journalRes.id.toString(),
                        content = journalRes.content,
                        moods = journalRes.moods,
                        year = journalRes.year,
                        month = journalRes.month,
                        day = journalRes.day,
                        userId = ""
                    )
                }
            }
        }
    }

    override suspend fun getJournalByMonth(appState: AppState, month: Int, year: Int): List<JournalResponse> {
        return withContext(Dispatchers.IO) {
            val database = JournalDB.getDB(appState.context)
            val journalDao = database.journalDao()

            val journalList = journalDao.findByMonth(year, month)

            val journalResponses = journalList.map { journal ->
                JournalResponse(
                    id = journal.id.toString(),
                    content = journal.content,
                    moods = journal.moods,
                    year = journal.year,
                    month = journal.month,
                    day = journal.day,
                    userId = "local"
                )
            }

            journalResponses
        }
    }

    override suspend fun createJournal(appState: AppState, journalRequest: JournalRequest) {
        return withContext(Dispatchers.IO){
            val database = JournalDB.getDB(appState.context)
            val journalDao = database.journalDao()

            try {
                val newJournal: Journal = Journal(
                    year = journalRequest.year,
                    month = journalRequest.month,
                    day = journalRequest.day,
                    moods = journalRequest.moods,
                    content = journalRequest.content
                )

                journalDao.insert(newJournal)
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    override suspend fun createPhoto(appState: AppState, photoRequest: PhotoRequest):Boolean{
        return withContext(Dispatchers.IO){
            val database = PhotoDB.getDB(appState.context)
            val photoDao = database.photoDao()
            try {
                val photo = Photo(
                    photoBase64 = photoRequest.photoBase64,
                    year = photoRequest.year,
                    month = photoRequest.month,
                    day = photoRequest.day
                )
                photoDao.insert(photo)
                true
            } catch (e: Exception) {
                println(e.message)
                false
            }
        }
    }

    override suspend fun getAllPhotos(appState: AppState): List<PhotoResponse> {
        return withContext(Dispatchers.IO) {
            val database = PhotoDB.getDB(appState.context)
            val photoDao = database.photoDao()
            val photolist = photoDao.getAll()

            val photoResponses = photolist.map { photo ->
                PhotoResponse(
                    id = photo.id.toString(),
                    photoBase64 = photo.photoBase64,
                    year = photo.year,
                    month = photo.month,
                    day = photo.day,
                    userid = "local"
                )
            }

            photoResponses
        }
    }

    override fun deleteAccount(appState: AppState): Pair<Boolean, Boolean> { return Pair(false, false) }

    override fun clearJWT(appState: AppState) {}
}