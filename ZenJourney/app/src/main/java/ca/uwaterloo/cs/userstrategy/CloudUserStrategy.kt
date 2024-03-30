package ca.uwaterloo.cs.userstrategy

import StatusResponse
import ca.uwaterloo.cs.AppState
import ca.uwaterloo.cs.PageStates
import ca.uwaterloo.cs.PhotobookPhoto
import ca.uwaterloo.cs.api.JournalApiService
import ca.uwaterloo.cs.api.JournalApiService.deleteJournalByUserId
import ca.uwaterloo.cs.api.PhotoApiService
import ca.uwaterloo.cs.api.PhotoApiService.deleteUserPhotos
import ca.uwaterloo.cs.api.UserApiService
import ca.uwaterloo.cs.api.UserApiService.deleteUser
import ca.uwaterloo.cs.capitalize
import ca.uwaterloo.cs.decodeImage
import com.auth0.android.jwt.JWT
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import journal.JournalRequest
import journal.JournalResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import photo.ListResponse
import photo.PhotoRequest
import photo.PhotoResponse
import user.UserRequest
import user.UserResponse

class CloudUserStrategy : UserStrategy {
    override val forgotPINLabel = "Log out"
    override val logoutLabel = "Log out"
    override val deleteAccountLabel = "Delete account"
    override suspend fun loadUserSettings(appState: AppState) {
        val jwt = appState.dataStore.getJwt()
        if (jwt.isNotEmpty() && !JWT(jwt).isExpired(5)) {
            appState.userId.value =
                JWT(jwt).getClaim("userId").asString().toString()
            try {
                val user: UserResponse = UserApiService.getUser(appState.userId.value).body()
                withContext(Dispatchers.Main) {
                    appState.nameState.value = user.name
                    appState.useJournalForAffirmations.value = user.useJournalForAffirmations
                    appState.hashedPIN.value = user.pin
                    if (appState.hashedPIN.value.isNotEmpty()) {
                        appState.isPINRequired.value = true
                    }
                }
            } catch (e: Exception) {
                // TODO: handle error
                println(e.message)
            }
            appState.pageState.value = PageStates.HOME
            appState.setPageHistoryToHome()
        } else {
            appState.pageState.value = PageStates.WELCOME
            appState.setPageHistoryToWelcome()
        }
    }

    override fun storeUserSettings(appState: AppState) {
        runBlocking {
            try {
                val response = UserApiService.updateUser(
                    appState.userId.value,
                    UserRequest(
                        "",
                        "",
                        "",
                        appState.hashedPIN.value,
                        appState.useJournalForAffirmations.value,
                    )
                )
                if (response.status == HttpStatusCode.BadRequest) {
                    val statusResponse: StatusResponse = response.body()
                    // TODO: handle with user id doesn't exist yet
                    println(statusResponse.body)
                }
            } catch (e: Exception) {
                // TODO: handle error
                println(e.message)
            }
        }
    }

    override fun logout(appState: AppState) {
        runBlocking {
            appState.dataStore.setJwt("")
        }
        appState.resetToDefault()
    }

    override fun deleteAccount(appState: AppState): Pair<Boolean, Boolean> {
        var successful = false
        var unsuccessful = false
        runBlocking {
            try {
                val userResponse = deleteUser(appState.userId.value, appState.dataStore.getJwt())
                val journalResponse = deleteJournalByUserId(appState.userId.value, appState.dataStore.getJwt())
                val photoResponse = deleteUserPhotos(appState.userId.value, appState.dataStore.getJwt())
                successful = true
            } catch (e: Exception) {
                unsuccessful = true
                // TODO: handle error
                println(e)
            }
        }
        return Pair(successful, unsuccessful)
    }

    override suspend fun getJournalByDate(appState: AppState, day: Int, month: Int, year: Int): JournalResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val response = JournalApiService.getJournalByDateAndUser(
                    userId = appState.userId.value,
                    year = year,
                    month = month,
                    day = day,
                    jwt = appState.dataStore.getJwt()
                )

                if (response.status == HttpStatusCode.OK) {
                    val journalResponse: JournalResponse = response.body()
                    journalResponse
                } else {
                    null
                }
            } catch (e: Exception) {
                println(e.message)
                null
            }
        }
    }

    override suspend fun getJournalByMonth(appState: AppState, month: Int, year: Int): List<JournalResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = JournalApiService.getJournalByMonth(
                    userId = appState.userId.value,
                    year = year,
                    month = month,
                    jwt = appState.dataStore.getJwt()
                )

                if (response.status == HttpStatusCode.OK) {
                    val responseBody = response.bodyAsText()
                    val gson = Gson()
                    val journalList: List<JournalResponse> = gson.fromJson(
                        responseBody,
                        object : TypeToken<List<JournalResponse>?>() {}.type
                    )
                    journalList
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                println("Error fetching journals by month: ${e.message}")
                emptyList()
            }
        }
    }

    override suspend fun createJournal(appState: AppState, journalRequest: JournalRequest) {
        return withContext(Dispatchers.IO){
            try {
                JournalApiService.createJournal(
                    journalRequest = journalRequest, jwt = appState.dataStore.getJwt()
                )
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    override suspend fun createPhoto(appState: AppState, photoRequest: PhotoRequest): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    PhotoApiService.createPhoto(photoRequest, appState.dataStore.getJwt())
                //failed to create photo successfully
                if (response.status != HttpStatusCode.Created) {
                    val statusResponse: StatusResponse = response.body()
                    println("createPhoto failed")
                    println(statusResponse.body)
                    return@withContext false
                }
                return@withContext true
                //some other error happened
            } catch (e: Exception) {
                println("createPhoto failed")
                println(e.message)
                false
            }
        }
    }

    override suspend fun getAllPhotos(appState: AppState): List<PhotoResponse>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = PhotoApiService.getAllUserPhotos(
                    appState.userId.value, appState.dataStore.getJwt()
                )
                //got photos
                if (response.status == HttpStatusCode.OK) {
                    val listResponse: ListResponse<PhotoResponse> = response.body()
                    listResponse.list
                } else { // status code is bad
                    val statusResponse: StatusResponse = response.body()
                    println("getAllUserPhotos failed due to response code")
                    println(statusResponse.body)
                    null
                }
                // some other error occured
            } catch (e: Exception) {
                println("getAllPhotos failed")
                println(e)
                null
            }
        }
    }

    override suspend fun getUserPhotosByYearMonth(
        appState: AppState,
        year: Int,
        month: Int
    ): List<PhotoResponse>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = PhotoApiService.getUserPhotosByYearMonth(
                    appState.userId.value, year, month, appState.dataStore.getJwt()
                )
                //got photos
                if (response.status == HttpStatusCode.OK) {
                    val listResponse: ListResponse<PhotoResponse> = response.body()
                    listResponse.list
                } else { // status code is bad
                    val statusResponse: StatusResponse = response.body()
                    println("getUserPhotosByYearMonth failed due to response code")
                    println(statusResponse.body)
                    null
                }
                // some other error occured
            } catch (e: Exception) {
                println("getUserPhotosByYearMonth failed")
                println(e)
                null
            }
        }
    }


    override fun clearJWT(appState: AppState) {
        runBlocking {
            appState.dataStore.setJwt("")
        }
    }
}