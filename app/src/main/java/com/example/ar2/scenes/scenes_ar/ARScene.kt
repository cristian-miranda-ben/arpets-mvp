package com.example.ar2.scenes.scenes_ar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.filament.Engine
import com.google.ar.core.Anchor
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.Plane
import io.github.sceneview.ar.ARScene
import io.github.sceneview.node.CubeNode
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.ar.arcore.isValid
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Position
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberOnGestureListener
import io.github.sceneview.rememberView
import kotlin.also
import kotlin.apply
import kotlin.collections.firstOrNull
import kotlin.collections.forEach
import kotlin.collections.isNotEmpty
import kotlin.collections.plusAssign
import kotlin.let
import kotlin.math.cos
import kotlin.math.sin
import kotlin.ranges.rangeTo

private const val modelo2 = "models/elefante.glb"

@Composable
fun Scene(){
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val materialLoader = rememberMaterialLoader(engine)
    val cameraNode = rememberARCameraNode(engine)
    val childNodes = rememberNodes()
    val view = rememberView(engine)
    val collisionSystem = rememberCollisionSystem(view)
    var elephantNode by remember { mutableStateOf<ModelNode?>(null) }
    var planeRenderer by remember { mutableStateOf(true) }
    var currentAnimation by remember { mutableStateOf("17") }
    var frame by remember { mutableStateOf<Frame?>(null) }

    ARScene(
        modifier = Modifier.fillMaxSize(),
        childNodes = childNodes,
        engine = engine,
        view = view,
        modelLoader = modelLoader,
        collisionSystem = collisionSystem,
        sessionConfiguration = { session, config ->
            config.depthMode =
                when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                    true -> Config.DepthMode.AUTOMATIC
                    else -> Config.DepthMode.DISABLED
                }
            config.instantPlacementMode = Config.InstantPlacementMode.LOCAL_Y_UP
            config.lightEstimationMode =
                Config.LightEstimationMode.ENVIRONMENTAL_HDR
        },
        cameraNode = cameraNode,
        planeRenderer = planeRenderer,
        onSessionUpdated = { session, updatedFrame ->
            frame = updatedFrame

            if (childNodes.isEmpty()) {
                updatedFrame.getUpdatedPlanes()
                    .firstOrNull { it.type == Plane.Type.HORIZONTAL_UPWARD_FACING }
                    ?.let { it.createAnchorOrNull(it.centerPose) }?.let { anchor ->
                        childNodes += createAnchorNode(
                            engine = engine,
                            modelLoader = modelLoader,
                            materialLoader = materialLoader,
                            anchor = anchor
                        ).also { anchorNode ->
                            // Obtener el ModelNode y aplicar animación inicial
                            elephantNode = (anchorNode.childNodes.firstOrNull() as? ModelNode)?.apply {
                                playAnimation("17", loop = true)
                            }
                        }
                    }
            }
        },
        onGestureListener = rememberOnGestureListener(
            onSingleTapConfirmed = { motionEvent, node ->
                if (node == null) {
                    val hitResults = frame?.hitTest(motionEvent.x, motionEvent.y)
                    hitResults?.firstOrNull {
                        it.isValid(
                            depthPoint = false,
                            point = false
                        )
                    }?.createAnchorOrNull()
                        ?.let { anchor ->
                            planeRenderer = false
                            childNodes += createAnchorNode(
                                engine = engine,
                                modelLoader = modelLoader,
                                materialLoader = materialLoader,
                                anchor = anchor
                            ).also { anchorNode ->
                                // Aplicar animación inicial al tocar
                                elephantNode = (anchorNode.childNodes.firstOrNull() as? ModelNode)?.apply {
                                    playAnimation("17", loop = true)
                                }
                            }
                        }
                }
            })
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 200.dp), // ⬅️ Espacio para los botones
        contentAlignment = Alignment.BottomCenter // ⬅️ Centrado
    ) {
        Control { angle, strength ->
            val node = elephantNode ?: return@Control
            if (strength > 5f) {
                // Reproduce animación de caminar
                val walkingAnim = "12"
                if (currentAnimation != walkingAnim) {
                    node.playAnimation(walkingAnim, loop = true)
                    currentAnimation = walkingAnim
                }

                val angleRad = Math.toRadians(angle.toDouble())
                val deltaX = (strength / 100f) * sin(angleRad) * 0.02f
                val deltaZ = (strength / 100f) * cos(angleRad) * 0.02f

                node.rotation = Rotation(0f, -angle, 0f)
                node.position = Position(
                    node.position.x + deltaX.toFloat(),
                    node.position.y,
                    node.position.z + deltaZ.toFloat()
                )
            } else {
                // Vuelve a animación idle
                val idleAnim = "17"
                if (currentAnimation != idleAnim) {
                    node.playAnimation(idleAnim, loop = true)
                    currentAnimation = idleAnim
                }
            }
        }
    }
}

fun createAnchorNode(
    engine: Engine,
    modelLoader: ModelLoader,
    materialLoader: MaterialLoader,
    anchor: Anchor
): AnchorNode {
    val anchorNode = AnchorNode(engine = engine, anchor = anchor)
    val modelNode = ModelNode(
        modelInstance = modelLoader.createModelInstance(modelo2),
        scaleToUnits = 0.7f,
    ).apply {
        isEditable = true
        editableScaleRange = 0.2f..0.75f

        // APLICAR ANIMACIÓN INICIAL AQUÍ
        playAnimation("17", loop = true)
    }

    val boundingBoxNode = CubeNode(
        engine,
        size = modelNode.extents,
        center = modelNode.center,
        materialInstance = materialLoader.createColorInstance(Color.White.copy(alpha = 0.5f))
    ).apply {
        isVisible = false
    }

    modelNode.addChildNode(boundingBoxNode)
    anchorNode.addChildNode(modelNode)

    listOf(modelNode, anchorNode).forEach {
        it.onEditingChanged = { editingTransforms ->
            boundingBoxNode.isVisible = editingTransforms.isNotEmpty()
        }
    }

    return anchorNode
}



