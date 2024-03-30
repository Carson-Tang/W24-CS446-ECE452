package ca.uwaterloo.cs.userstrategy

import ca.uwaterloo.cs.AppState
import journal.JournalResponse
import journal.JournalRequest
import photo.PhotoRequest
import photo.PhotoResponse

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
    suspend fun createPhoto(appState: AppState, photoRequest: PhotoRequest):Boolean
    suspend fun getAllPhotos(appState: AppState): List<PhotoResponse>?

    suspend fun updateJournal(appState: AppState, journalRequest: JournalRequest, id: String)
    fun clearJWT(appState: AppState)
}