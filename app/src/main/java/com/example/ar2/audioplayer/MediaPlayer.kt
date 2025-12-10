package com.example.ar2.audioplayer

import com.example.ar2.R
import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.ar2.scenes.SplashScreen


@Composable
fun AudioAnimacionSplash(
    isPlaying: Boolean,
    onAudioFinished: () -> Unit
): MediaPlayer {
    val context = LocalContext.current

    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.sonidointro)
    }

    // Flag para evitar liberar dos veces
    var isReleased by remember { mutableStateOf(false) }

    // Control del estado de reproducción
    LaunchedEffect(isPlaying) {
        if (!isReleased) {
            if (isPlaying) {
                if (!mediaPlayer.isPlaying) mediaPlayer.start()
            } else {
                if (mediaPlayer.isPlaying) mediaPlayer.pause()
            }
        }
    }

    // Listener para detectar fin del audio
    DisposableEffect(Unit) {
        mediaPlayer.setOnCompletionListener {
            if (!isReleased) {
                onAudioFinished()
            }
        }

        onDispose {
            try {
                if (!isReleased) {
                    if (mediaPlayer.isPlaying) mediaPlayer.stop()
                    mediaPlayer.release()
                    isReleased = true
                }
            } catch (e: IllegalStateException) {
                // Si ya está liberado, ignoramos
            }
        }
    }

    return mediaPlayer
}