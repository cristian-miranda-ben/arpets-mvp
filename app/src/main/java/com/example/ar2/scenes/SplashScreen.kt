package com.example.ar2.scenes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ar2.audioplayer.AudioAnimacionSplash
import com.example.ar2.scenes.scenes3d.Escena3dInicio
import com.example.ar2.scenes.scenes3d.Escena3dSplash
import com.example.ar2.ui.navigation.InicioScreen
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navController: NavController) {
    var isPlaying by remember { mutableStateOf(true) }
    var sceneReady by remember { mutableStateOf(false) }

    // Forzar recomposición después de un pequeño delay
    LaunchedEffect(Unit) {
        delay(100) // Pequeño delay para que la escena se inicialice
        sceneReady = true
    }

    val mediaPlayer = AudioAnimacionSplash(
        isPlaying = isPlaying,
        onAudioFinished = {
            navController.navigate(InicioScreen) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Forzar que la escena se dibuje
        if (sceneReady) {
            Escena3dSplash()
        } else {
            // Placeholder mientras carga
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            Button(onClick = { isPlaying = !isPlaying }) {
                Text(if (isPlaying) "Pausar" else "Reproducir")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                try {
                    if (mediaPlayer.isPlaying) mediaPlayer.stop()
                    mediaPlayer.release()
                } catch (_: IllegalStateException) { }
                navController.navigate(InicioScreen) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            }) {
                Text("Saltar")
            }
        }
    }
}