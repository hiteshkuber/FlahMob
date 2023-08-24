package com.example.flashmob

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flashmob.ui.theme.green
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun CustomCircularProgressIndicator(
    modifier: Modifier = Modifier,
    initialValue : Int,
    primaryColor: Color,
    secondaryColor: Color,
    minValue: Int = 0,
    maxValue: Int = 100,
    circleRadius: Float,
    onPositionChange: (Int) -> Unit
) {

    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    var positionValue by remember {
        mutableStateOf(initialValue)
    }

    var changeAngle by remember {
        mutableStateOf(0f)
    }

    var dragStartedAngle by remember {
        mutableStateOf(0f)
    }

    var oldPositionValue by remember {
        mutableStateOf(initialValue)
    }

    Box(
        modifier = modifier,
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
                .pointerInput(true){
                    detectDragGestures(
                        onDragStart = {offset ->
                            dragStartedAngle = -atan2(
                                x = circleCenter.y - offset.y,
                                y = circleCenter.x - offset.x
                            ) * (180f / PI).toFloat()
                            dragStartedAngle = (dragStartedAngle + 180f).mod(360f)
                        },
                        onDrag = { change, _ ->
                            var touchAngle = -atan2(
                                x = circleCenter.y - change.position.y,
                                y = circleCenter.x - change.position.x
                            ) * (180f / PI).toFloat()
                            touchAngle = (touchAngle + 180f).mod(360f)

                            val currentAngle = oldPositionValue*360f/(maxValue-minValue)
                            changeAngle = touchAngle - currentAngle

                            val lowerThreshold = currentAngle - (360f / (maxValue-minValue) * 5)
                            val higherThreshold = currentAngle + (360f / (maxValue-minValue) * 5)

                            if(dragStartedAngle in lowerThreshold .. higherThreshold){
                                positionValue = (oldPositionValue + (changeAngle/(360f/(maxValue-minValue))).roundToInt())
                            }

                        },
                        onDragEnd = {
                            oldPositionValue = positionValue
                            onPositionChange(positionValue)
                        }
                    )
                }
        ) {

            val width = size.width
            val height = size.height
            val circleThickness = width / 5f
            circleCenter = Offset(x = width / 2f, y = height / 2f)

            drawCircle(
                brush = Brush.radialGradient(
                    listOf(
                        primaryColor.copy(0.75f),
                        secondaryColor.copy(0.15f)
                    )
                ),
                radius = circleRadius,
                center = circleCenter
            )

            drawCircle(
                style = Stroke(
                    width = circleThickness
                ),
                color = secondaryColor,
                radius = circleRadius,
                center = circleCenter
            )

            drawArc(
                color =  primaryColor,
                startAngle = 90f,
                sweepAngle = (360f/maxValue) * positionValue.toFloat(),
                style = Stroke(
                    width = circleThickness,
                    cap = StrokeCap.Round
                ),
                useCenter = false,
                size = Size(
                    width = circleRadius*2f,
                    height = circleRadius*2f
                ),
                topLeft = Offset((width - circleRadius*2f)/2, (height - circleRadius*2f)/2)
            )

            val outerRadius = circleRadius + circleThickness / 2f
            val gap = 15f
            for (i in 0 .. (maxValue - minValue)) {
                val color = if (i < positionValue - minValue) {
                    primaryColor
                } else {
                    primaryColor.copy(alpha = 0.3f)
                }
                val angleInDegrees = i*360f/(maxValue - minValue).toFloat()
                val angleInRed = angleInDegrees * PI / 180f + PI/2f

                val yGapAdjustments = cos(angleInDegrees * PI / 180f)*gap
                val xGapAdjustments = -sin(angleInDegrees * PI / 180f)*gap

                val start = Offset(
                    x = (outerRadius * cos(angleInRed) + circleCenter.x + xGapAdjustments).toFloat(),
                    y = (outerRadius * sin(angleInRed) + circleCenter.y + yGapAdjustments).toFloat()
                )

                val end = Offset(
                    x = (outerRadius * cos(angleInRed) + circleCenter.x + xGapAdjustments).toFloat(),
                    y = (outerRadius * sin(angleInRed) +circleThickness + circleCenter.y + yGapAdjustments).toFloat()
                )

                rotate(
                    angleInDegrees,
                    pivot = start
                ) {
                    drawLine(
                        color = color,
                        start = start,
                        end = end,
                        strokeWidth = 1.dp.toPx()
                    )
                }
            }

            drawContext.canvas.nativeCanvas.apply {
                drawIntoCanvas {
                    drawText(
                        "$positionValue",
                        circleCenter.x,
                        circleCenter.y + 15.dp.toPx(),
                        Paint().apply {
                            textSize = 38.sp.toPx()
                            textAlign = Paint.Align.CENTER
                            color = secondaryColor.toArgb()
                            isFakeBoldText = true
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewComposable() {
    CustomCircularProgressIndicator(
        initialValue= 60,
        primaryColor= green,
        secondaryColor= Color.Yellow,
        circleRadius = 230f,
        onPositionChange = {},
        modifier = Modifier
            .size(250.dp)
            .background(Color.DarkGray)
    )
}