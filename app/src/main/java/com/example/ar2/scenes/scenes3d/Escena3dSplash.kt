package com.example.ar2.scenes.scenes3d

import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberMaterialLoader
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.sceneview.Scene
import io.github.sceneview.rememberEnvironment
import io.github.sceneview.math.Position
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberMainLightNode
import io.github.sceneview.rememberRenderer
import io.github.sceneview.rememberScene
import io.github.sceneview.rememberView
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberModelLoader


@Composable
fun Escena3dSplash2() {


// Filament 3D Engine
    val engine = rememberEngine()

// Asset loaders
    val modelLoader = rememberModelLoader(engine)
    val materialLoader = rememberMaterialLoader(engine)
    val environmentLoader = rememberEnvironmentLoader(engine)

    Scene(
        modifier = Modifier.fillMaxSize(),
        engine = engine,

        // Core rendering components
        view = rememberView(engine),
        renderer = rememberRenderer(engine),
        scene = rememberScene(engine),

        // Asset loaders
        modelLoader = modelLoader,
        materialLoader = materialLoader,
        environmentLoader = environmentLoader,


        // Add a direct light source (required for shadows)
        mainLightNode = rememberMainLightNode(engine) {
            intensity = 100_000.0f
        },

        // Set up environment lighting and skybox from an HDR file
        environment = rememberEnvironment(environmentLoader) {
            environmentLoader.createHDREnvironment(
                assetFileLocation = "environments/sky_2k.hdr"
            )!!
        },

        //  posicion camara
        cameraNode = rememberCameraNode(engine) {
            position = Position(z = 4.0f)
        },
        cameraManipulator = rememberCameraManipulator(),
        onFrame = { frameTimeNanos ->
            // cada cuadro
        }
    )
}