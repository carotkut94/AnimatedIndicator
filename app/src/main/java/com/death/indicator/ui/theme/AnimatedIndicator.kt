package com.death.indicator.ui.theme

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs

@Composable
fun AnimatedIndicator(
    modifier: Modifier = Modifier,
    progressColor: Color = MaterialTheme.colors.primary,
    progressBackgroundColor: Color = Color.Blue,
    strokeWidth: Dp = 10.dp,
    strokeBackgroundWidth: Dp = 10.dp,
    progressDirection: AnimationDirection = AnimationDirection.RIGHT,
    roundedBorder: Boolean = false,
    durationInMilliSecond: Int = 800,
    multilayer:Boolean = false,
    radius: Dp = 80.dp
) {

    val stroke = with(LocalDensity.current) {
        Stroke(width = strokeWidth.toPx(), cap = if(roundedBorder) StrokeCap.Round else StrokeCap.Square)
    }

    val strokeBackground = with(LocalDensity.current) {
        Stroke(width = strokeBackgroundWidth.toPx())
    }

    val transition = rememberInfiniteTransition()

    val animatedRestart by transition.animateFloat(
        initialValue = 0f,
        targetValue = if(progressDirection == AnimationDirection.RIGHT) 360f else -360f,
        animationSpec = infiniteRepeatable(tween(durationInMilliSecond, easing = LinearEasing))
    )

    val endAngle by transition.animateFloat(
        0f,
        JumpRotationAngle,
        infiniteRepeatable(
            animation = keyframes {
                durationMillis = HeadAndTailAnimationDuration + HeadAndTailDelayDuration
                0f at 0 with CircularEasing
                JumpRotationAngle at HeadAndTailAnimationDuration
            }
        )
    )

    val startAngle by transition.animateFloat(
        0f,
        JumpRotationAngle,
        infiniteRepeatable(
            animation = keyframes {
                durationMillis = HeadAndTailAnimationDuration + HeadAndTailDelayDuration
                0f at HeadAndTailDelayDuration with CircularEasing
                JumpRotationAngle at durationMillis
            }
        )
    )

    Canvas(
        modifier
            .size(radius * 2)
    ) {
        val higherStrokeWidth =
            if (stroke.width > strokeBackground.width) stroke.width else strokeBackground.width
        val radius = (size.minDimension - higherStrokeWidth) / 2
        val halfSize = size / 2.0f
        val topLeft = Offset(
            halfSize.width - radius,
            halfSize.height - radius
        )
        val size = Size(radius * 2, radius * 2)

        val sweep = abs(endAngle - startAngle)
        val diameterOffset = stroke.width / 2
        val arcDimen = size.width -  diameterOffset
        val sizeOfBar = Size(arcDimen, arcDimen)

        if(multilayer) {
            drawArc(
                startAngle = 0f,
                sweepAngle = 360f,
                topLeft = topLeft,
                color = progressBackgroundColor,
                useCenter = false,
                size = sizeOfBar,
                style = strokeBackground
            )
        }



        drawArc(
            color = progressColor,
            startAngle = animatedRestart,
            sweepAngle = sweep,
            useCenter = false,
            topLeft = topLeft,
            size = sizeOfBar,
            style = stroke,
        )
    }
}

enum class AnimationDirection{
    LEFT,
    RIGHT
}

// Indeterminate circular indicator transition specs

// Each rotation is 1 and 1/3 seconds, but 1332ms divides more evenly
private const val RotationDuration = 1332

// How far the base point moves around the circle
private const val BaseRotationAngle = 286f

// How far the head and tail should jump forward during one rotation past the base point
private const val JumpRotationAngle = 290f

// The head animates for the first half of a rotation, then is static for the second half
// The tail is static for the first half and then animates for the second half
private const val HeadAndTailAnimationDuration = (RotationDuration * 0.5).toInt()
private const val HeadAndTailDelayDuration = HeadAndTailAnimationDuration

// The easing for the head and tail jump
private val CircularEasing = CubicBezierEasing(0.4f, 0f, 0.2f, 1f)