package io.jadu.trackdown.presentation

import TimeSeriesResponse
import android.content.Context
import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.serialization.json.Json
@Composable
fun DetailsScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CustomAppBar(title = "Details Screen")
        Column {
            StockItemCard(stock = StockModelClass("https://www.apple.com/ac/structured-data/images/knowledge_graph_logo.png?202109201039", "AAPLE INC", "$139", "$150.00"))
        }
        val xData = listOf(1f, 2f, 3f, 4f, 5f)
        val yData = listOf(10f, 20f, 15f, 25f, 30f)

    }
    loadJsonFromAssets(context = context, fileName = "stock.json")
    ParseJson(context)


}

fun loadJsonFromAssets(context: Context, fileName: String): String {
    return try {
        context.assets.open("stock.json").bufferedReader().use { it.readText() }
    } catch (e: Exception) {
        Log.e("LoadJsonFromAssets", "Error loading JSON from assets", e)
        ""
    }
}

@Composable
fun ParseJson(context: Context) {
    val jsonString = loadJsonFromAssets(context, "data.json")
    if (jsonString.isNotBlank()) {
        val stockData = Json.decodeFromString<TimeSeriesResponse>(jsonString)
        val dates = stockData.timeSeries.keys.toList()
        val closingPrices = stockData.timeSeries.values.map { it.close.toFloat() }
        LineGraph(
            xData = dates,
            yData = closingPrices,
            dataLabel = stockData.metaData.symbol
        )
        try {

            Log.d("ParseJson", "Parsed io.jadu.trackdown.model.StockData: ${stockData.timeSeries}")
        } catch (e: Exception) {
            Log.e("ParseJson", "Error parsing JSON", e)
        }
    } else {
        Log.e("ParseJson", "Empty JSON string")
    }
}

@Composable
fun StockItemCard(stock: StockModelClass) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = stock.image,
                    contentDescription = "Company Logo",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.LightGray, CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = stock.companyName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Apple",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "text",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stock.stockPrice,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = stock.stockChange,
                    color = if (stock.stockChange.startsWith("+")) Color.Green else Color.Red,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun LineGraph(
    xData: List<String>,
    yData: List<Float>,
    dataLabel: String,
    modifier: Modifier = Modifier,
    lineColor: Color = Color.Red,
    fillColor: Color = Color.Red,
    fillAlpha: Int = 255,
    axisTextColor: Color = Color.White,
    backgroundColor: Color = Color.DarkGray,
    drawValues: Boolean = false,
    drawMarkers: Boolean = false,
    drawFilled: Boolean = true,
    descriptionEnabled: Boolean = false,
    legendEnabled: Boolean = true,
    yAxisRightEnabled: Boolean = false,
    xAxisPosition: XAxis.XAxisPosition = XAxis.XAxisPosition.BOTTOM
) {
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            val chart = LineChart(context)

            // Create entries using indices as x values
            val entries: List<Entry> = yData.indices.map { Entry(it.toFloat(), yData[it]) }
            val dataSet = LineDataSet(entries, dataLabel)

            chart.data = LineData(dataSet)  // Pass the dataset to the chart

            // Enable touch gestures
            chart.setTouchEnabled(true)
            chart.isDragEnabled = true
            chart.isScaleXEnabled = true
            chart.isScaleYEnabled = false

            chart.description.isEnabled = descriptionEnabled
            chart.legend.isEnabled = legendEnabled

            chart.axisLeft.textColor = axisTextColor.toArgb()
            chart.axisRight.isEnabled = yAxisRightEnabled
            chart.xAxis.textColor = axisTextColor.toArgb()
            chart.xAxis.position = xAxisPosition

            // Set custom labels for x-axis (month names)
            chart.xAxis.valueFormatter = IndexAxisValueFormatter(xData)
            chart.xAxis.labelRotationAngle = -45f
            chart.xAxis.setCenterAxisLabels(false)
            chart.xAxis.granularity = 1f // Ensure one label per x-axis entry

            // Refresh and return the chart
            chart.invalidate()
            chart
        }
    )
}


@Composable
@Preview
fun DetailsScreenPreview() {
    DetailsScreen()
}
