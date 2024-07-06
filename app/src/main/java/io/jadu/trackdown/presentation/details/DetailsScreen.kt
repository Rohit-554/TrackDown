package io.jadu.trackdown.presentation.details

import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import io.jadu.trackdown.domain.model.IntraDayInfo
import io.jadu.trackdown.presentation.companyList.StockModelClass
import io.jadu.trackdown.util.Helper.formatTo12HourTime
import io.jadu.trackdown.util.Helper.sortDataByDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    symbol: String,
    viewModel: StockDetailsViewModel = hiltViewModel()
) {
    val state = viewModel.state
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Company Details") }
            )
        },
        content = { padding ->
            if (state.error == null) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                ) {
                    item {
                        state.company?.let { data ->
                            Text(
                                text = data.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = data.symbol,
                                fontStyle = FontStyle.Italic,
                                fontSize = 14.sp,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Industry: ${data.industry}",
                                fontSize = 14.sp,
                                modifier = Modifier.fillMaxWidth(),
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Country: ${data.country}",
                                fontSize = 14.sp,
                                modifier = Modifier.fillMaxWidth(),
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = data.description,
                                fontSize = 12.sp,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }

                    if (state.stockInfos.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Market Summary")
                            Spacer(modifier = Modifier.height(16.dp))
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color.Black),
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Box(modifier = Modifier
                                    .height(300.dp)
                                    .fillMaxWidth()) {
                                    ShowGraph(state.stockInfos)
                                }
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    TimePeriodSelector(viewModel)
                                }
                                /*loadJsonFromAssets(context = context, fileName = "stock.json")*/
                            }
                        }
                        item {

                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator()
                } else if (state.error != null) {
                    Text(
                        text = state.error,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    )
}

@Composable
fun TimePeriodSelector(viewModel: StockDetailsViewModel) {
    val timePeriods = listOf("1D", "1W", "1M", "3M", "6M", "1Y")
    var selectedPeriod by remember { mutableStateOf("1D") }

    Row(
        modifier = Modifier
            .padding(8.dp)
            .border(BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(50))
            .padding(horizontal = 4.dp, vertical = 4.dp)
    ) {
        timePeriods.forEach { period ->
            Text(
                text = period,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .clickable {
                        selectedPeriod = period
                        viewModel.updateStockInfo(period)
                    }
                    .background(
                        if (selectedPeriod == period) Color(0xFFD35400) else Color.Transparent,
                        shape = RoundedCornerShape(50)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                color = if (selectedPeriod == period) Color.White else Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ShowGraph(stockInfos: List<IntraDayInfo>) {
    if (stockInfos.isNotEmpty()) {
        val dates = stockInfos.map { it.date.toString() } // Convert LocalDateTime to String
        val closingPrices = stockInfos.map { it.close.toFloat() }
        val dataLabel = "Stock Data"
        LineGraph(
            xData = dates,
            yData = closingPrices,
            dataLabel = dataLabel
        )
        try {
            Log.d("ParseJson", "Parsed stockInfos: $stockInfos")
        } catch (e: Exception) {
            Log.e("ParseJson", "Error logging stockInfos", e)
        }
    } else {
        Log.e("ParseJson", "Empty stockInfos list")
    }
}

@Composable
fun LineGraph(
    xData: List<String>,
    yData: List<Float>,
    dataLabel: String,
    modifier: Modifier = Modifier,
    axisTextColor: Color = Color.White,
    descriptionEnabled: Boolean = false,
    legendEnabled: Boolean = true,
    yAxisRightEnabled: Boolean = false,
    xAxisPosition: XAxis.XAxisPosition = XAxis.XAxisPosition.BOTTOM,
    legendOffset: Float = 20f,
    pointRadius: Float = 1f // New parameter for point radius
) {
    val (sortedXData, sortedYData) = sortDataByDateTime(xData, yData)
    val formattedXData = formatTo12HourTime(sortedXData)

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            val chart = LineChart(context)
            val entries: List<Entry> = yData.indices.map { Entry(it.toFloat(), yData[it]) }
            val dataSet = LineDataSet(entries, dataLabel)
            dataSet.valueTextColor = axisTextColor.toArgb()
            dataSet.circleRadius = pointRadius // Set the radius for the points
            chart.data = LineData(dataSet)
            chart.setDrawBorders(true)
            //remove the grid
            chart.axisLeft.setDrawGridLines(false)
            chart.xAxis.setDrawGridLines(false)

            // Enable touch gestures
            chart.setTouchEnabled(true)
            chart.isDragEnabled = true
            chart.isScaleXEnabled = true
            chart.isScaleYEnabled = false

            chart.description.isEnabled = descriptionEnabled
            chart.legend.isEnabled = legendEnabled
            chart.legend.textColor = axisTextColor.toArgb()

            chart.axisLeft.textColor = axisTextColor.toArgb()
            chart.axisRight.isEnabled = yAxisRightEnabled
            chart.xAxis.textColor = axisTextColor.toArgb()
            chart.xAxis.position = xAxisPosition

            // Set custom labels for x-axis
            chart.xAxis.valueFormatter = IndexAxisValueFormatter(formattedXData)
            chart.xAxis.labelRotationAngle = 0f
            chart.xAxis.setCenterAxisLabels(false)
            chart.xAxis.granularity = 1f

            // for setting extra offset to the legend title
            chart.setExtraOffsets(0f, 0f, 0f, legendOffset)
            // Refresh and return the chart
            chart.invalidate()
            chart
        }
    )
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

//for testing purposes
fun loadJsonFromAssets(context: Context, fileName: String): String {
    return try {
        context.assets.open("stock.json").bufferedReader().use { it.readText() }
    } catch (e: Exception) {
        Log.e("LoadJsonFromAssets", "Error loading JSON from assets", e)
        ""
    }
}



