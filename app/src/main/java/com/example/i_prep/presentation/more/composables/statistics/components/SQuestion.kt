package com.example.i_prep.presentation.more.composables.statistics.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.example.i_prep.presentation.GlobalState

@Composable
fun SQuestion(globalState: GlobalState, modifier: Modifier = Modifier) {

    val pieChartData = PieChartData(
        slices = listOf(
            PieChartData.Slice(
                "Multiple Choice",
                globalState.pTestList.count { it.questionType == "mcq" }.toFloat(),
                MaterialTheme.colorScheme.primary
            ),
            PieChartData.Slice(
                "True or False",
                globalState.pTestList.count { it.questionType == "tof" }.toFloat(),
                MaterialTheme.colorScheme.secondary
            ),
            PieChartData.Slice(
                "Fill-in-the-Blanks",
                globalState.pTestList.count { it.questionType == "fitb" }.toFloat(),
                MaterialTheme.colorScheme.tertiary
            ),
        ),
        plotType = PlotType.Pie
    )

    val pieChartConfig = PieChartConfig(
        isAnimationEnable = true,
        showSliceLabels = false,
        backgroundColor = Color.Transparent,
        animationDuration = 1500,
        chartPadding = 12,
    )

    Column(
        modifier = modifier.padding(horizontal = 12.dp).padding(top = 10.dp),
    ) {
        Column(
            modifier = modifier.padding(start = 16.dp)
        ) {
            Text(
                text = "Question",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PieChart(
                modifier = modifier
                    .width(175.dp)
                    .height(175.dp)
                    .clip(RoundedCornerShape(50)),
                pieChartData = pieChartData,
                pieChartConfig = pieChartConfig
            )

            Column {
                for (data in pieChartData.slices) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Column(
                            modifier = modifier
                                .size(10.dp)
                                .background(data.color, RoundedCornerShape(2.dp))
                        ) {}

                        Text(
                            text = "${data.label} (${(data.value * 100) / data.value}%)",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
    }
}