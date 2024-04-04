package ca.uwaterloo.cs.timerstate

import android.media.MediaPlayer
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import ca.uwaterloo.cs.AppState
import android.media.MediaPlayer as AndroidMediaPlayer

interface PlayerState {
    fun play(
        appState: AppState,
        mediaPlayer: AndroidMediaPlayer?,
        meditationPlayer: MeditationPlayer
    )

    fun pause(
        appState: AppState,
        mediaPlayer: AndroidMediaPlayer?,
        meditationPlayer: MeditationPlayer
    )

    fun restart(
        appState: AppState,
        mediaPlayer: AndroidMediaPlayer?,
        meditationPlayer: MeditationPlayer
    )
}

class IdleState : PlayerState {
    override fun play(
        appState: AppState,
        mediaPlayer: AndroidMediaPlayer?,
        meditationPlayer: MeditationPlayer
    ) {
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
        meditationPlayer.setState(PlayingState())
    }

    override fun pause(
        appState: AppState,
        mediaPlayer: AndroidMediaPlayer?,
        meditationPlayer: MeditationPlayer
    ) {
        // do nothing
    }

    override fun restart(
        appState: AppState,
        mediaPlayer: AndroidMediaPlayer?,
        meditationPlayer: MeditationPlayer
    ) {
        // do nothing
    }
}

class PausedState : PlayerState {
    override fun play(
        appState: AppState,
        mediaPlayer: AndroidMediaPlayer?,
        meditationPlayer: MeditationPlayer
    ) {
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
        meditationPlayer.setState(PlayingState())
    }

    override fun pause(
        appState: AppState,
        mediaPlayer: AndroidMediaPlayer?,
        meditationPlayer: MeditationPlayer
    ) {
        // do nothing
    }

    override fun restart(
        appState: AppState,
        mediaPlayer: AndroidMediaPlayer?,
        meditationPlayer: MeditationPlayer
    ) {
        appState.timeMs.value = appState.defaultTimeMs.value
        meditationPlayer.setState(IdleState())
    }
}

class PlayingState : PlayerState {
    override fun play(
        appState: AppState,
        mediaPlayer: AndroidMediaPlayer?,
        meditationPlayer: MeditationPlayer
    ) {
        // do nothing
    }

    override fun pause(
        appState: AppState,
        mediaPlayer: AndroidMediaPlayer?,
        meditationPlayer: MeditationPlayer
    ) {
        mediaPlayer?.pause()
        meditationPlayer.setState(PausedState())
    }

    override fun restart(
        appState: AppState,
        mediaPlayer: AndroidMediaPlayer?,
        meditationPlayer: MeditationPlayer
    ) {
        mediaPlayer?.pause()
        appState.timeMs.value = appState.defaultTimeMs.value
        meditationPlayer.setState(IdleState())
    }
}

object MeditationPlayer {
    private var state: MutableState<PlayerState> = mutableStateOf(IdleState())
    private var mediaPlayer: AndroidMediaPlayer? = null

    private fun createMediaPlayer(appState: AppState) {
        if (state.value !is PlayingState) {
            // in idle or paused we create a new media player
            mediaPlayer = MediaPlayer.create(appState.context, appState.selectedTune.value)
        }
    }

    fun play(appState: AppState) {
        createMediaPlayer(appState)
        state.value.play(appState, mediaPlayer, this)
    }

    fun pause(appState: AppState) {
        state.value.pause(appState, mediaPlayer, this)
    }

    fun restart(appState: AppState) {
        state.value.restart(appState, mediaPlayer, this)
    }

    fun setState(newState: PlayerState) {
        state.value = newState
    }

    fun onFinish() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}