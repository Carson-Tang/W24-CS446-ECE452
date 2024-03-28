package ca.uwaterloo.cs.userstrategy

import ca.uwaterloo.cs.AppState
import journal.JournalResponse
import journal.JournalRequest
interface UserStrategy {
    val forgotPINLabel: String
    val logoutLabel: String
    val deleteAccountLabel: String
    suspend fun loadUserSettings(appState: AppState)
    fun storeUserSettings(appState: AppState)
    fun logout(appState: AppState)
    fun deleteAccount(appState: AppState): Pair<Boolean, Boolean>
    suspend fun getJournalByDate(appState: AppState, day: Int, month: Int, year: Int): JournalResponse?
    suspend fun getJournalByMonth(appState: AppState, month: Int, year: Int): List<JournalResponse>
    suspend fun createJournal(appState: AppState, journalRequest: JournalRequest)
    fun clearJWT(appState: AppState)
}