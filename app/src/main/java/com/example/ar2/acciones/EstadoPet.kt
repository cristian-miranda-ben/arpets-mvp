package com.example.ar2.acciones

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext


@Composable
fun EstadoPet(
    feedPressed: Boolean,
    petPressed: Boolean,
    playPressed: Boolean,
    onReset: () -> Unit          // ← para resetear los flags
) {
    val context = LocalContext.current

    var lastFeedTime by rememberSaveable { mutableLongStateOf(0L) }
    var lastPetTime by rememberSaveable { mutableLongStateOf(0L) }
    var lastPlayTime by rememberSaveable { mutableLongStateOf(0L) }

    var totalAnimo by rememberSaveable { mutableFloatStateOf(10f) }

    val now = System.currentTimeMillis()

    // ---- Dar de comer (cada 2 horas) ----
    if (feedPressed) {
        val canFeed = now - lastFeedTime >= 2 * 60 * 60 * 1000
        if (canFeed && totalAnimo <= 8f) {
            totalAnimo += 2
            lastFeedTime = now
            Toast.makeText(context, "Le diste de comer", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Todavía no tiene hambre", Toast.LENGTH_SHORT).show()
        }
        onReset()
    }

    // ---- Acariciar (cada 1 hora) ----
    if (petPressed) {
        val canPet = now - lastPetTime >= 1 * 60 * 60 * 1000
        if (canPet && totalAnimo <= 9f) {
            totalAnimo += 1
            lastPetTime = now
            Toast.makeText(context, "Lo acariciaste", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Todavía no quiere caricias", Toast.LENGTH_SHORT).show()
        }
        onReset()
    }

    // ---- Jugar (cada 1 hora) ----
    if (playPressed) {
        val canPlay = now - lastPlayTime >= 1 * 60 * 60 * 1000
        if (canPlay && totalAnimo <= 9f) {
            totalAnimo += 1
            lastPlayTime = now
            Toast.makeText(context, "Jugaste con tu mascota", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Todavía está cansado", Toast.LENGTH_SHORT).show()
        }
        onReset()
    }
}
