package ca.uwaterloo.cs.userstrategy

import ca.uwaterloo.cs.AppState

interface UserStrategy {
    val forgotPINLabel: String
    val logoutLabel: String
    suspend fun loadUserSettings(appState: AppState)
    fun storeUserSettings(appState: AppState)
    fun logout(appState: AppState)
}