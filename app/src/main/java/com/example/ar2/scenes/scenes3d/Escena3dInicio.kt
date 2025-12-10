package com.example.ar2.scenes.scenes3d

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.filament.EntityManager
import com.google.android.filament.LightManager
import io.github.sceneview.Scene
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironment
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Position
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberRenderer
import io.github.sceneview.rememberScene
import io.github.sceneview.rememberView
import io.github.sceneview.node.LightNode
import io.github.sceneview.node.SphereNode
import io.github.sceneview.math.Color as SceneColor
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberModelLoader

private const val modelo1 = "models/elefante.glb"
private const val modelo2 = "models/sea_turtle.glb"
private const val modelo3 = "models/toon_cat_free.glb"
private const val modelo4 = "models/Rover.glb"

@Composable
fun Escena3dInicio(){
    val engine = rememberEngine()
    val materialLoader = rememberMaterialLoader(engine)
    val environmentLoader = rememberEnvironmentLoader(engine)
    val environment = rememberEnvironment(engine)
    val modelLoader = rememberModelLoader(engine)

    // Variables para la rotación de cada modelo
    var rotation1 by remember { mutableStateOf(0f) }
    var rotation2 by remember { mutableStateOf(0f) }
    var rotation3 by remember { mutableStateOf(0f) }
    var rotation4 by remember { mutableStateOf(0f) }

    Scene(
        modifier = Modifier.fillMaxSize(),
        engine = engine,
        view = rememberView(engine),
        renderer = rememberRenderer(engine),
        scene = rememberScene(engine),
        materialLoader = materialLoader,
        environmentLoader = environmentLoader,

        cameraNode = rememberCameraNode(engine) {
            position = Position(x = 0f, y = 3f, z = 12f)
            rotation = Rotation(x = 0f, y = 0f, z = 0f)
        },

        childNodes = rememberNodes {
            // Modelo 1: Elefante (izquierda arriba)
            add(
                ModelNode(
                    modelInstance = modelLoader.createModelInstance(modelo1),
                    scaleToUnits = 2f
                ).apply {
                    transform(position = Position(x = -3f, y = 4f, z = 0f))
                    onFrame = {
                        rotation1 = (rotation1 + 1f) % 360f
                        transform(
                            position = Position(x = -3f, y = 4f, z = 0f),
                            rotation = Rotation(y = rotation1)
                        )
                    }
                }
            )

            // Modelo 2: tortuga (derecha arriba)
            add(
                ModelNode(
                    modelInstance = modelLoader.createModelInstance(modelo2),
                    scaleToUnits = 2f
                ).apply {
                    transform(position = Position(x = 3f, y = 4f, z = 0f))
                    onFrame = {
                        rotation2 = (rotation2 + 1.5f) % 360f
                        transform(
                            position = Position(x = 3f, y = 4f, z = 0f),
                            rotation = Rotation(y = rotation2)
                        )
                    }
                }
            )

            // Modelo 3: Gato (izquierda abajo)
            add(
                ModelNode(
                    modelInstance = modelLoader.createModelInstance(modelo3),
                    scaleToUnits = 2f
                ).apply {
                    transform(position = Position(x = -3f, y = 1f, z = 0f))
                    onFrame = {
                        rotation3 = (rotation3 + 0.8f) % 360f
                        transform(
                            position = Position(x = -3f, y = 1f, z = 0f),
                            rotation = Rotation(y = rotation3)
                        )
                    }
                }
            )

            // Modelo 4: Rover (Perro,derecha abajo)
            add(
                ModelNode(
                    modelInstance = modelLoader.createModelInstance(modelo4),
                    scaleToUnits = 2f
                ).apply {
                    transform(position = Position(x = 3f, y = 1f, z = 0f))
                    onFrame = {
                        rotation4 = (rotation4 + 1.2f) % 360f
                        transform(
                            position = Position(x = 3f, y = 1f, z = 0f),
                            rotation = Rotation(y = rotation4)
                        )
                    }
                }
            )

            // SOL en el centro
            add(
                SphereNode(
                    engine = engine,
                    radius = 0.5f,
                    center = Position(x = 0f, y = 2.5f, z = 0f),
                    materialInstance = materialLoader.createColorInstance(
                        color = SceneColor(25.0f, 20.0f, 15.0f, 1f),
                        metallic = 0.0f,
                        roughness = 0.0f,
                        reflectance = 1.0f
                    ),
                    builderApply = {
                        castShadows(false)
                        receiveShadows(false)
                    }
                ).apply {
                    transform(position = Position(x = 0f, y = 2.5f, z = 0f))
                }
            )

            // HALOS DEL SOL
            add(
                SphereNode(
                    engine = engine,
                    radius = 0.9f,
                    center = Position(x = 0f, y = 2.5f, z = 0f),
                    materialInstance = materialLoader.createColorInstance(
                        color = SceneColor(20.0f, 15.0f, 10.0f, 0.7f),
                        metallic = 0.0f,
                        roughness = 0.0f,
                        reflectance = 0.0f
                    ),
                    builderApply = {
                        castShadows(false)
                        receiveShadows(false)
                    }
                ).apply {
                    transform(position = Position(x = 0f, y = 2.5f, z = 0f))
                }
            )

            add(
                SphereNode(
                    engine = engine,
                    radius = 1.3f,
                    center = Position(x = 0f, y = 2.5f, z = 0f),
                    materialInstance = materialLoader.createColorInstance(
                        color = SceneColor(5.0f, 4.0f, 2.0f, 0.7f),
                        metallic = 0.0f,
                        roughness = 0.0f,
                        reflectance = 0.0f
                    ),
                    builderApply = {
                        castShadows(false)
                        receiveShadows(false)
                    }
                ).apply {
                    transform(position = Position(x = 0f, y = 2.5f, z = 0f))
                }
            )

            add(
                SphereNode(
                    engine = engine,
                    radius = 1.8f,
                    center = Position(x = 0f, y = 2.5f, z = 0f),
                    materialInstance = materialLoader.createColorInstance(
                        color = SceneColor(4.0f, 3.0f, 1.5f, 0.5f),
                        metallic = 0.0f,
                        roughness = 0.0f,
                        reflectance = 0.0f
                    ),
                    builderApply = {
                        castShadows(false)
                        receiveShadows(false)
                    }
                ).apply {
                    transform(position = Position(x = 0f, y = 2.5f, z = 0f))
                }
            )

            // Luz principal
            add(
                LightNode(
                    engine = engine,
                    type = LightManager.Type.SUN,
                    entity = EntityManager.get().create(),
                    apply = {
                        intensity(100_000f)
                        color(1.0f, 0.9f, 0.7f)
                        direction(0.0f, -0.5f, -0.5f)
                    }
                )
            )
        }
    )
}
