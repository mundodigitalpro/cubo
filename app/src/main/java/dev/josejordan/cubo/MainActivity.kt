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
import androidx.compose.ui.graphics.drawscope.DrawScope
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
    val infiniteTransition = rememberInfiniteTransition()

    rotationX = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    ).value

    rotationY = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
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

        // Define colors for each face
        val faceColors = listOf(
            Color.Red, Color.Green, Color.Blue,
            Color.Yellow, Color.Cyan, Color.Magenta
        )

        // Draw faces
        drawFace(points[0], points[1], points[2], points[3], faceColors[0])
        drawFace(points[4], points[5], points[6], points[7], faceColors[1])
        drawFace(points[0], points[1], points[5], points[4], faceColors[2])
        drawFace(points[2], points[3], points[7], points[6], faceColors[3])
        drawFace(points[0], points[3], points[7], points[4], faceColors[4])
        drawFace(points[1], points[2], points[6], points[5], faceColors[5])

        // Draw edges
        val edges = listOf(
            0 to 1, 1 to 2, 2 to 3, 3 to 0,
            4 to 5, 5 to 6, 6 to 7, 7 to 4,
            0 to 4, 1 to 5, 2 to 6, 3 to 7
        )

        edges.forEach { (start, end) ->
            drawLine(
                Color.Black,
                points[start],
                points[end],
                strokeWidth = 3f
            )
        }
    }
}

fun DrawScope.drawFace(p1: Offset, p2: Offset, p3: Offset, p4: Offset, color: Color) {
    drawPath(
        path = androidx.compose.ui.graphics.Path().apply {
            moveTo(p1.x, p1.y)
            lineTo(p2.x, p2.y)
            lineTo(p3.x, p3.y)
            lineTo(p4.x, p4.y)
            close()
        },
        color = color.copy(alpha = 0.7f)
    )
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