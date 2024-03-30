package ca.uwaterloo.cs

import android.media.MediaPlayer as AndroidMediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

sealed class PlayerState {
    abstract fun play(mediaPlayer: AndroidMediaPlayer)
    abstract fun pause(mediaPlayer: AndroidMediaPlayer)
    abstract fun stop(mediaPlayer: AndroidMediaPlayer)
}

// Refresh
object StoppedState : PlayerState() {
    override fun play(mediaPlayer: AndroidMediaPlayer) {
    }

    override fun pause(mediaPlayer: AndroidMediaPlayer) {
    }

    override fun stop(mediaPlayer: AndroidMediaPlayer) {
    }
}

object PausedState : PlayerState() {
    override fun play(mediaPlayer: AndroidMediaPlayer) {
    }

    override fun pause(mediaPlayer: AndroidMediaPlayer) {
    }

    override fun stop(mediaPlayer: AndroidMediaPlayer) {
    }
}

object PlayingState : PlayerState() {
    override fun play(mediaPlayer: AndroidMediaPlayer) {
    }

    override fun pause(mediaPlayer: AndroidMediaPlayer) {
    }

    override fun stop(mediaPlayer: AndroidMediaPlayer) {
    }
}


class MediaPlayer {
    var mediaPlayer: AndroidMediaPlayer? = null
    var playerState: PlayerState = StoppedState

    fun play() {
        mediaPlayer?.let { playerState.play(it) }
    }

    fun pause() {
        mediaPlayer?.let { playerState.pause(it) }
    }

    fun stop() {
        mediaPlayer?.let { playerState.stop(it) }
    }

    fun createMediaPlayer(appState: AppState) {

    }

    fun changeState(newState: PlayerState) {
        playerState = newState
    }
}

