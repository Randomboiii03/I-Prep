package com.example.i_prep.presentation.home.composables.details.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine

@Composable
fun DChart(pointData: List<Point>, yScale: Int, label: String, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
//        border = BorderStroke(1.5f.dp, color = MaterialTheme.colorScheme.onSurface),
        modifier = modifier.padding(start = 16.dp, end = 16.dp, top = 6.dp)
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(3.dp)
        ) {
            LineChart(
                modifier = modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                    .padding(end = 6.dp)
                    .height(250.dp),
                lineChartData = getLineChartData(
                    pointData = pointData,
                    yScale = if (yScale <= 0) 2 else yScale
                )
            )

            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
            )
        }
    }
}

@Composable
private fun getLineChartData(pointData: List<Point>, yScale: Int): LineChartData {
    return LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointData,
                    LineStyle(
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        width = 6f,
                        lineType = LineType.SmoothCurve(isDotted = false)
                    ),
                    IntersectionPoint(
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        radius = 4.dp
                    ),
                    SelectionHighlightPoint(color = MaterialTheme.colorScheme.onTertiaryContainer),
                    ShadowUnderLine(
                        alpha = 0.5f,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.onTertiaryContainer,
                                Color.Transparent
                            )
                        )
                    ),
                    SelectionHighlightPopUp()
                )
            )
        ),
        backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
        xAxisData = getXAxisData(pointData = pointData),
        yAxisData = getYAxisData(yScale = yScale),
        gridLines = GridLines(
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            enableVerticalLines = false
        ),
        paddingRight = 0.dp
    )
}

@Composable
private fun getXAxisData(pointData: List<Point>): AxisData {
    return AxisData.Builder()
        .backgroundColor(MaterialTheme.colorScheme.tertiaryContainer)
        .steps(pointData.size - 1)
        .labelData { (it + 1).toString() }
        .axisLabelColor(MaterialTheme.colorScheme.onTertiaryContainer)
        .axisLineColor(MaterialTheme.colorScheme.onTertiaryContainer)
        .shouldDrawAxisLineTillEnd(true)
        .build()
}

@Composable
private fun getYAxisData(yScale: Int): AxisData {
    val steps = highestDivisibleDivisor(yScale)

    return AxisData.Builder()
        .backgroundColor(MaterialTheme.colorScheme.tertiaryContainer)
        .steps(steps)
        .labelData {
            val newYScale = (yScale / steps)
            (it * newYScale).toString()
        }
        .axisLabelColor(MaterialTheme.colorScheme.onTertiaryContainer)
        .axisLineColor(MaterialTheme.colorScheme.onTertiaryContainer)
        .shouldDrawAxisLineTillEnd(true)
        .build()
}

fun highestDivisibleDivisor(number: Int) = (5 downTo 1).first { number % it == 0 }