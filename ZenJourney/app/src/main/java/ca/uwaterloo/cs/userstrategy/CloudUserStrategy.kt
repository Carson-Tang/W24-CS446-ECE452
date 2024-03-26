package ca.uwaterloo.cs.userstrategy

import StatusResponse
import ca.uwaterloo.cs.AppState
import ca.uwaterloo.cs.PageStates
import ca.uwaterloo.cs.api.UserApiService
import com.auth0.android.jwt.JWT
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
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
    }

    override fun deleteAccount(appState: AppState) {
        runBlocking {
            appState.dataStore.setJwt("")
        }
    }
}