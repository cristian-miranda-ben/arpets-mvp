package com.example.ar2.permisos

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun PedirPermisoCamara(
    onPermissionGranted: () -> Unit, // Función a llamar si el permiso se otorga
    onPermissionDenied: () -> Unit  // Función a llamar si el permiso se niega
) {
    // 1. Obtener el Contexto actual. Lo necesitamos para verificar el estado del permiso
    val context = LocalContext.current

    /*2. Estado para saber si debemos mostrar la justificación (Rationale)
     El sistema Android lo maneja internamente, pero esta variable nos permite
      mostrar nuestro propio diálogo antes de la solicitud si es necesario.*/
    var showRationale by remember { mutableStateOf(false) }

    // 3. El LAUNCHER: ¡La magia del asincronismo en Compose!
    // Este objeto espera un resultado de una acción.
    val launcher = rememberLauncherForActivityResult(
        // Contrato: Le decimos que vamos a pedir un solo permiso (Manifest.permission.CAMERA)
        contract = ActivityResultContracts.RequestPermission(),

        // CALLBACK (EL RESULTADO ASÍNCRONO): Lo que se ejecuta cuando el usuario responde a la solicitud
        onResult = { isGranted ->
            if (isGranted) {
                // El usuario dijo SÍ
                onPermissionGranted()
            } else {
                // El usuario dijo NO
                onPermissionDenied()
                // Opcionalmente, puedes manejar aquí la lógica de 'No volver a preguntar'
                // pero por ahora solo llamaremos a la función de denegación.
            }
        }
    )

    // 4. Lógica principal para verificar el estado y decidir qué hacer
    val permissionCheckResult = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    )

    when {
        // A) PERMISO YA OTORGADO: La verificación es inmediata.
        permissionCheckResult == PackageManager.PERMISSION_GRANTED -> {
            // Si ya lo tiene, simplemente llamamos a la función de éxito.
            onPermissionGranted()
        }

        // B) DEBEMOS MOSTRAR JUSTIFICACIÓN (Rationale):
        // (Esto es raro en la primera solicitud, pero importante si el usuario lo rechazó antes)
        showRationale -> {
            // Puedes poner un Diálogo (AlertDialog) aquí explicando por qué
            // necesitas la cámara antes de llamar a launcher.launch().
            // Por simplicidad, omitiremos el diálogo por ahora, pero aquí es donde iría.
            // Para fines de este ejemplo, si showRationale es true, lanzamos la solicitud.
            SideEffect {
                launcher.launch(Manifest.permission.CAMERA)
                showRationale = false // Lanzado, no necesitamos mostrarlo de nuevo
            }
        }

        // C) NO TIENE EL PERMISO, TOCA PEDIRLO:
        else -> {
            // Esto es un SideEffect porque no queremos que la solicitud se lance
            // en cada recomposición (redibujado de la pantalla).
            SideEffect {
                Log.d("Permiso", "Lanzando solicitud de permiso de cámara...")
                launcher.launch(Manifest.permission.CAMERA)
            }
        }
    }
}