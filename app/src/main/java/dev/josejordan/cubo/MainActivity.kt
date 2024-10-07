package dev.josejordan.cubo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RotatingCube()
        }
    }
}

@Composable
fun RotatingCube() {
    var rotationX by remember { mutableStateOf(0f) }
    var rotationY by remember { mutableStateOf(0f) }
    val infiniteTransition = rememberInfiniteTransition(label = "")

    rotationX = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    ).value

    rotationY = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    ).value

    Canvas(modifier = Modifier.fillMaxSize()) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val cubeSize = size.minDimension / 4

        val points = listOf(
            Point3D(-1f, -1f, -1f), Point3D(1f, -1f, -1f),
            Point3D(1f, 1f, -1f), Point3D(-1f, 1f, -1f),
            Point3D(-1f, -1f, 1f), Point3D(1f, -1f, 1f),
            Point3D(1f, 1f, 1f), Point3D(-1f, 1f, 1f)
        ).map { it.rotateX(rotationX).rotateY(rotationY) }
            .map { Offset(it.x * cubeSize + centerX, it.y * cubeSize + centerY) }

        // Dibuja las aristas del cubo
        val edges = listOf(
            0 to 1, 1 to 2, 2 to 3, 3 to 0, // cara frontal
            4 to 5, 5 to 6, 6 to 7, 7 to 4, // cara trasera
            0 to 4, 1 to 5, 2 to 6, 3 to 7  // conexiones
        )

        edges.forEach { (start, end) ->
            drawLine(
                Color.Blue,
                points[start],
                points[end],
                strokeWidth = 5f
            )
        }
    }
}

data class Point3D(val x: Float, val y: Float, val z: Float) {
    fun rotateX(angle: Float): Point3D {
        val rad = Math.toRadians(angle.toDouble())
        val cosa = cos(rad).toFloat()
        val sina = sin(rad).toFloat()
        val newY = y * cosa - z * sina
        val newZ = y * sina + z * cosa
        return Point3D(x, newY, newZ)
    }

    fun rotateY(angle: Float): Point3D {
        val rad = Math.toRadians(angle.toDouble())
        val cosa = cos(rad).toFloat()
        val sina = sin(rad).toFloat()
        val newX = x * cosa - z * sina
        val newZ = x * sina + z * cosa
        return Point3D(newX, y, newZ)
    }
}