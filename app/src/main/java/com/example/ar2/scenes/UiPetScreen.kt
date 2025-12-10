package com.example.ar2.scenes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ar2.R
import com.example.ar2.acciones.EstadoPet
import com.example.ar2.scenes.scenes_ar.Scene


@Composable
fun UiPetScreen(navController: NavController) {
    var progreso by remember { mutableStateOf(0.3f) }
    var feedPressed by remember { mutableStateOf(false) }
    var petPressed by remember { mutableStateOf(false) }
    var playPressed by remember { mutableStateOf(false) }

    // Obtener el padding de la barra de navegación del sistema
    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues()

    Box(modifier = Modifier.fillMaxSize()) {
        // ESCENA AR DE FONDO (ocupa toda la pantalla)
        Scene()

        // UI ENCIMA DE LA ESCENA AR
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Estado del Pet (invisible pero funcional)
            EstadoPet(
                feedPressed = feedPressed,
                petPressed = petPressed,
                playPressed = playPressed,
                onReset = {
                    feedPressed = false
                    petPressed = false
                    playPressed = false
                }
            )

            // Card en la parte superior
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                CardPet(
                    nombre = "Firulais",
                    progreso = progreso,
                    onProgressChange = { progreso = it },
                    modifier = Modifier.fillMaxWidth(0.4f)
                )
            }

            // Espaciador que empuja los botones hacia abajo
            Spacer(modifier = Modifier.weight(1f))

            // Botones en la parte inferior CON PADDING DINÁMICO
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = navigationBarPadding.calculateBottomPadding() + 16.dp  // ⬅️ DINÁMICO
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { feedPressed = true },
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text("Dar de Comer")
                }

                Button(
                    onClick = { petPressed = true },
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text("Acariciar")
                }

                Button(
                    onClick = { playPressed = true },
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text("Jugar")
                }
            }
        }
    }
}


@Composable
fun CardPet(
    nombre: String,
    progreso: Float,
    onProgressChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val barraColor = when {
        progreso < 0.3f -> Color.Red
        progreso < 0.6f -> Color.Yellow
        else -> Color.Green
    }
    Card(
        modifier = modifier
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Imagen
            Image(
                painter = painterResource(id = R.drawable.elefante),
                contentDescription = "Imagen de Mascota",
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Nombre
            Text(
                text = nombre,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Barra de Progreso
            LinearProgressIndicator(
                progress = { progreso },
                color = barraColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Botones para modificar progreso
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { onProgressChange((progreso - 0.1f).coerceIn(0f, 1f)) }
                ) {
                    Text("-")
                }
                Button(
                    onClick = { onProgressChange((progreso + 0.1f).coerceIn(0f, 1f)) }
                ) {
                    Text("+")
                }
            }
        }
    }
}
