package com.example.ar2.scenes.scenes3d

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.filament.EntityManager
import com.google.android.filament.LightManager
import dev.romainguy.kotlin.math.lookAt
import io.github.sceneview.Scene
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironment
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Position
import io.github.sceneview.node.CubeNode
import io.github.sceneview.node.CylinderNode
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberRenderer
import io.github.sceneview.rememberScene
import io.github.sceneview.rememberView
import io.github.sceneview.math.Size
import io.github.sceneview.node.LightNode
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node
import io.github.sceneview.node.SphereNode
import io.github.sceneview.rememberModelLoader
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.time.Duration.Companion.nanoseconds
import io.github.sceneview.math.Color as SceneColor


private const val modelo1 ="models/elefante.glb"
private const val modelo2 ="models/toon_cat_free.glb"
private const val modelo3 ="models/sea_turtle.glb"
private const val modelo4 ="models/Rover.glb"
@Composable
fun Escena3dSplash(){
    val engine = rememberEngine()
    val materialLoader = rememberMaterialLoader(engine)
    val environmentLoader = rememberEnvironmentLoader(engine)
    val environment = rememberEnvironment(engine)
    val modelLoader = rememberModelLoader(engine)


    // Estado de animación
    var cycleCount by remember { mutableStateOf(0) }
    var animationProgress by remember { mutableStateOf(0f) }

    // Función para generar posición aleatoria
    fun generarPosicionAleatoria(): Position {
        val x = Random.nextFloat() * 4f - 2f // Entre -2 y 2
        val y = Random.nextFloat() * 4f - 1f // Entre -1 y 3
        val z = 3f + Random.nextFloat() * 2f // Entre 3 y 5
        return Position(x = x, y = y, z = z)
    }

    // Posiciones iniciales de los objetos (aleatorias)
    var cylinderPos by remember { mutableStateOf(generarPosicionAleatoria()) }
    var cubePos by remember { mutableStateOf(generarPosicionAleatoria()) }
    var spherePos by remember { mutableStateOf(generarPosicionAleatoria()) }

    // Guardar posiciones iniciales para interpolación
    var cylinderStartPos by remember { mutableStateOf(cylinderPos) }
    var cubeStartPos by remember { mutableStateOf(cubePos) }
    var sphereStartPos by remember { mutableStateOf(spherePos) }

    // Visibilidad
    var showObjects by remember { mutableStateOf(true) }
    var currentAnimal by remember { mutableIntStateOf(0) }

    LaunchedEffect(currentAnimal) {
        Log.i("CurrentAnimal", "⭐ currentAnimal cambió a: $currentAnimal")
    }
    var showLogo by remember { mutableStateOf(false) }
    val nodesKey = "$currentAnimal-$showLogo-$showObjects"
    var currentRotationY by remember { mutableStateOf(0.0f) }

    // Duración total: 35 segundos
    // 9 ciclos + 3 animales (2s cada uno) = 9 ciclos + 6s
    // Cada ciclo debe durar: (35 - 6) / 9 ≈ 3.2 segundos
    val velocidadAnimacion = 0.016f / 3.2f // 60 FPS / 3.2 segundos por ciclo

    Scene(
        modifier = Modifier.fillMaxSize(),
        engine = engine,
        view = rememberView(engine),
        renderer = rememberRenderer(engine),
        scene = rememberScene(engine),
        materialLoader = materialLoader,
        environmentLoader = environmentLoader,

        cameraNode = rememberCameraNode(engine) {
            position = Position(x = 0f, y = 5f, z = 8f)  // Un poco más arriba
            lookAt(
                eye = Position(x = 0f, y = 5f, z = 8f),
                target = Position(x = 0f, y = 3f, z = 0f),  // Sigue mirando al sol
                up = Position(x = 0f, y = 1f, z = 0f)
            )
        },

        childNodes = remember(nodesKey) {  // <-- USA remember con la clave
            mutableListOf<Node>().apply {
            // Cilindro
            if (showObjects) {
                add(
                    CylinderNode(
                        engine = engine,
                        radius = 0.2f,
                        height = 2f,
                        materialInstance = materialLoader.createColorInstance(
                            color = Color.White,
                            metallic = 0.5f,
                            roughness = 0.2f,
                            reflectance = 4f
                        )
                    ).apply {
                        transform(
                            position = cylinderPos,
                            rotation = Rotation(x = 90.0f)
                        )
                        onFrame = { frameTime ->
                            // Animar hacia el sol
                            if (animationProgress < 1f) {
                                animationProgress += velocidadAnimacion
                                val progress = animationProgress.coerceIn(0f, 1f)

                                // Interpolación desde posición inicial hacia el sol (y=3, z=0)
                                cylinderPos = Position(
                                    x = cylinderStartPos.x * (1f - progress),
                                    y = cylinderStartPos.y + (3f - cylinderStartPos.y) * progress,
                                    z = cylinderStartPos.z * (1f - progress)
                                )

                                // Calcular opacidad: se desvanece cuando está cerca (progress > 0.7)
                                val opacity = if (progress > 0.7f) {
                                    1f - ((progress - 0.7f) / 0.3f) // De 1.0 a 0.0 en los últimos 30%
                                } else {
                                    1f
                                }

                                // Actualizar material con nueva opacidad
                                materialInstance = materialLoader.createColorInstance(
                                    color = Color.White.copy(alpha = opacity),
                                    metallic = 0.5f,
                                    roughness = 0.2f,
                                    reflectance = 4f
                                )

                                transform(
                                    position = cylinderPos,
                                    rotation = Rotation(x = 90.0f)
                                )

                                // Al completar la animación
                                if (progress >= 1f) {
                                    showObjects = false
                                    cycleCount++
                                    Log.i("Ciclo", "✅ Ciclo completado: $cycleCount")

                                    // Reiniciar después de un breve delay
                                    kotlinx.coroutines.GlobalScope.launch {
                                        Log.i("Corrutina", "🔵 Corrutina iniciada, cycleCount = $cycleCount")  // <-- NUEVO
                                        kotlinx.coroutines.delay(500)
                                        Log.i("Corrutina", "🔵 Después del delay de 500ms")  // <-- NUEVO

                                        if (cycleCount == 3) {
                                            Log.i("Animal", "🐘 ANTES de cambiar a 1")  // <-- NUEVO
                                            currentAnimal = 1
                                            Log.i("Animal", "🐘 DESPUÉS de cambiar a 1, currentAnimal = $currentAnimal")  // <-- NUEVO
                                            kotlinx.coroutines.delay(2000)
                                            Log.i("Animal", "🐘 Ocultando animal")  // <-- NUEVO
                                            currentAnimal = 0
                                        } else if (cycleCount == 6) {
                                            Log.i("Animal", "🐢 Mostrando animal 2")
                                            currentAnimal = 2
                                            kotlinx.coroutines.delay(2000)
                                            currentAnimal = 0
                                        } else if (cycleCount == 9) {
                                            Log.i("Animal", "🚀 Mostrando animal 3")
                                            currentAnimal = 3
                                            kotlinx.coroutines.delay(2000)
                                            showLogo = true
                                            return@launch
                                        }

                                        Log.i("Corrutina", "🟢 Antes de reiniciar objetos")  // <-- NUEVO
                                        // Reiniciar posiciones
                                        animationProgress = 0f
                                        cylinderStartPos = generarPosicionAleatoria()
                                        cubeStartPos = generarPosicionAleatoria()
                                        sphereStartPos = generarPosicionAleatoria()

                                        cylinderPos = cylinderStartPos
                                        cubePos = cubeStartPos
                                        spherePos = sphereStartPos
                                        showObjects = true
                                        Log.i("Corrutina", "🟢 Objetos reiniciados")  // <-- NUEVO
                                    }
                                }
                            }
                        }
                    }
                )
            }

            // Cubo
            if (showObjects) {
                add(
                    CubeNode(
                        engine = engine,
                        size = Size(x = 0.5f, y = 0.5f, z = 0.3f),
                        materialInstance = materialLoader.createColorInstance(
                            color = Color.Red,
                            metallic = 0.5f,
                            roughness = 0.2f,
                            reflectance = 0.3f
                        )
                    ).apply {
                        transform(
                            position = cubePos,
                            rotation = Rotation(x = 90.0f)
                        )
                        onFrame = { frameTime ->
                            currentRotationY = (currentRotationY + 2f) % 360f

                            if (animationProgress < 1f) {
                                val progress = animationProgress.coerceIn(0f, 1f)

                                cubePos = Position(
                                    x = cubeStartPos.x * (1f - progress),
                                    y = cubeStartPos.y + (3f - cubeStartPos.y) * progress,
                                    z = cubeStartPos.z * (1f - progress)
                                )

                                // Calcular opacidad
                                val opacity = if (progress > 0.7f) {
                                    1f - ((progress - 0.7f) / 0.3f)
                                } else {
                                    1f
                                }

                                // Actualizar material con nueva opacidad
                                materialInstance = materialLoader.createColorInstance(
                                    color = Color.Red.copy(alpha = opacity),
                                    metallic = 0.5f,
                                    roughness = 0.2f,
                                    reflectance = 0.3f
                                )

                                transform(
                                    position = cubePos,
                                    rotation = Rotation(
                                        x = 90.0f,
                                        y = currentRotationY
                                    )
                                )
                            }
                        }
                    }
                )
            }

            // Nueva geometría: Esfera
            if (showObjects) {
                add(
                    SphereNode(
                        engine = engine,
                        radius = 0.3f,
                        center = spherePos,
                        materialInstance = materialLoader.createColorInstance(
                            color = Color.Yellow,
                            metallic = 0.7f,
                            roughness = 0.2f,
                            reflectance = 0.3f
                        )
                    ).apply {
                        transform(position = spherePos)
                        onFrame = {
                            if (animationProgress < 1f) {
                                val progress = animationProgress.coerceIn(0f, 1f)

                                spherePos = Position(
                                    x = sphereStartPos.x * (1f - progress),
                                    y = sphereStartPos.y + (3f - sphereStartPos.y) * progress,
                                    z = sphereStartPos.z * (1f - progress)
                                )

                                // Calcular opacidad
                                val opacity = if (progress > 0.7f) {
                                    1f - ((progress - 0.7f) / 0.3f)
                                } else {
                                    1f
                                }

                                // Actualizar material con nueva opacidad
                                materialInstance = materialLoader.createColorInstance(
                                    color = Color.Blue.copy(alpha = opacity),
                                    metallic = 0.7f,
                                    roughness = 0.1f,
                                    reflectance = 0.8f
                                )

                                transform(position = spherePos)
                            }
                        }
                    }
                )
            }

            // Modelos 3D de animales (GLB)
            if (currentAnimal == 1) {
                Log.i("animal 1","se ejecuta")
                add(
                    ModelNode(
                        modelInstance = modelLoader.createModelInstance(modelo1),
                        scaleToUnits = 3f
                    ).apply {
                        transform(position = Position(x = 0f, y = 3f, z = 3f))
                    }
                )
            }

            if (currentAnimal == 2) {
                Log.i("animal 2","se ejecuta")
                add(
                    ModelNode(
                        modelInstance = modelLoader.createModelInstance(modelo2),
                        scaleToUnits = 4f
                    ).apply {
                        transform(position = Position(x = 0f, y = 3f, z = 3f))
                    }
                )
            }

            if (currentAnimal == 3) {
                Log.i("animal 3","se ejecuta")
                add(
                    ModelNode(
                        modelInstance = modelLoader.createModelInstance(modelo3),
                        scaleToUnits = 8f
                    ).apply {
                        transform(position = Position(x = 0f, y = 3f, z = 3f))
                    }
                )
            }


            // Logo final
            if (showLogo) {
                add(
                    ModelNode(
                        modelInstance = modelLoader.createModelInstance(modelo4),
                        scaleToUnits = 4f
                    ).apply {
                        transform(position = Position(x = 0f, y = 3f, z = 0f))
                    }
                )
            }

            // SOL - NÚCLEO
            add(
                SphereNode(
                    engine = engine,
                    radius = 0.5f,
                    center = Position(x = 0f, y = 3f, z = 0f),
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
                    transform(position = Position(x = 0f, y = 3f, z = 0f))
                }
            )

            // HALOS DEL SOL
            add(//esfera interior
                SphereNode(
                    engine = engine,
                    radius = 0.9f,
                    center = Position(x = 0f, y = 3f, z = 0f),
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
                    transform(position = Position(x = 0f, y = 3f, z = 0f))
                }
            )

            add( // esfera 2
                SphereNode(
                    engine = engine,
                    radius = 1.3f,
                    center = Position(x = 0f, y = 3f, z = 0f),
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
                    transform(position = Position(x = 0f, y = 3f, z = 0f))
                }
            )

            add(// esfera exterior
                SphereNode(
                    engine = engine,
                    radius = 1.8f,
                    center = Position(x = 0f, y = 3f, z = 0f),
                    materialInstance = materialLoader.createColorInstance(
                        color = SceneColor(4.0f, 3.0f, 1.5f, 0.4f),
                        metallic = 0.0f,
                        roughness = 0.0f,
                        reflectance = 0.0f
                    ),
                    builderApply = {
                        castShadows(false)
                        receiveShadows(false)
                    }
                ).apply {
                    transform(position = Position(x = 0f, y = 3f, z = 0f))
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
                        color(1.0f, 0.9f, 0.3f)
                        direction(0.0f, -0.5f, -0.5f)
                    }
                )
            )
        }
}
    )
}