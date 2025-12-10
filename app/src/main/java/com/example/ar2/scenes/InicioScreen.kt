package com.example.ar2.scenes

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ar2.R
import com.example.ar2.scenes.scenes3d.Escena3dInicio
import com.example.ar2.ui.navigation.UiPetScreen

@Composable
fun InicioScreen(navController: NavController) {



    // --- UI ---
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Escena 3d de fondo
        Escena3dInicio()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val painter= painterResource(id= R.drawable.ar_pets_logo)
            Image(

                painter = painter,
                contentDescription = "Logo de la aplicación",
                modifier = Modifier.size(150.dp)
            )

            Spacer(Modifier.height(24.dp))

            Button(onClick = {navController.navigate(UiPetScreen) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }}) {
                Text(text = "Iniciar")
            }

            Spacer(Modifier.height(12.dp))

            Button(onClick = {

            }) {
                Text(text = "Salir")
            }
        }
    }


}


