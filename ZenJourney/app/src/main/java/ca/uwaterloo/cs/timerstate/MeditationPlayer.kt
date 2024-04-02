package ca.uwaterloo.cs.timerstate

import ca.uwaterloo.cs.AppState
import android.media.MediaPlayer as AndroidMediaPlayer

abstract class PlayerState{
    abstract fun play(appState: AppState, mediaPlayer: AndroidMediaPlayer?)
    abstract fun pause(appState: AppState, mediaPlayer: AndroidMediaPlayer?)
    abstract fun restart(appState: AppState, mediaPlayer: AndroidMediaPlayer?)
}

class IdleState: PlayerState() {
    override fun play(appState: AppState, mediaPlayer: AndroidMediaPlayer?) {
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
    }

    override fun pause(appState: AppState, mediaPlayer: AndroidMediaPlayer?) {
        // do nothing
    }

    override fun restart(appState: AppState, mediaPlayer: AndroidMediaPlayer?) {
        // do nothing
    }
}

class PausedState: PlayerState() {
    override fun play(appState: AppState, mediaPlayer: AndroidMediaPlayer?) {
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
    }

    override fun pause(appState: AppState, mediaPlayer: AndroidMediaPlayer?) {
        // do nothing
    }

    override fun restart(appState: AppState, mediaPlayer: AndroidMediaPlayer?) {
        appState.timeMs.value = appState.defaultTimeMs.value
    }
}

class PlayingState: PlayerState() {
    override fun play(appState: AppState, mediaPlayer: AndroidMediaPlayer?) {
        // do nothing
    }

    override fun pause(appState: AppState, mediaPlayer: AndroidMediaPlayer?) {
        mediaPlayer?.pause()
    }

    override fun restart(appState: AppState, mediaPlayer: AndroidMediaPlayer?) {
        mediaPlayer?.pause()
        appState.timeMs.value = appState.defaultTimeMs.value
    }
}
