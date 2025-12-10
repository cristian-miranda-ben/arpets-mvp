package com.example.ar2.permisos

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlin.apply


@Composable
fun pedirPermisoCamara():Boolean {
    val context = LocalContext.current
    Log.i("Acceso a funcion","COMIENZO")
    // 1. Estado para almacenar el resultado actual del permiso (booleano)
    var isGranted by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
    }

    // 2. Nuestro lanzador asíncrono
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isPermissionGranted ->
        // Este es el callback (el resumeN de la corrutina)
        isGranted = isPermissionGranted
    }

    // 3. Lógica para verificar la Rationale
    val showRationale = ActivityCompat.shouldShowRequestPermissionRationale(
        context.findActivity(), // Necesitamos la Actividad, no el Contexto de la app
        Manifest.permission.CAMERA
    )

    when {
        // A) PERMISO CONCEDIDO
        isGranted -> {
            Log.i("Acceso a funcion","Permiso Concedido")
            Text("✅ Permiso de cámara concedido.")
            // Aquí iría tu Composable de la cámara
        }

        // B) DENEGADO PERO NECESITA RATIONALE (Temporalmente denegado)
        showRationale -> {
            Log.i("Acceso a funcion","Permiso Temporalmente Denegado")
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Necesitamos tu permiso para usar la cámara y tomar fotos.", textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    // Volvemos a lanzar la solicitud de permiso
                    launcher.launch(Manifest.permission.CAMERA)
                }) {
                    Text("Volver a Pedir Permiso")
                }
            }
        }

        // C) DENEGADO PERMANENTEMENTE (showRationale es false y el permiso no está concedido)
        else -> {
            Log.i("Acceso a funcion","Permiso DENEGADO")
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("El permiso de la cámara fue denegado permanentemente. Debes activarlo en los ajustes.", textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    // Redirigir al usuario a la configuración de la aplicación
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                }) {
                    Text("Ir a Ajustes")
                }
            }
        }
    }
    if(isGranted){
        return true
    }else{
        return false
    }
}

// Función de extensión que ayuda a obtener la Activity
fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw kotlin.IllegalStateException("No se pudo obtener la actividad.")
}