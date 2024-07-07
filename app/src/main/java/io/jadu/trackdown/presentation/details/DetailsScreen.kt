package io.jadu.trackdown.presentation.details

import android.content.Context
import android.util.Log
import android.widget.Space
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import io.jadu.trackdown.R
import io.jadu.trackdown.domain.model.DailyStockInfo
import io.jadu.trackdown.domain.model.IntraDayInfo
import io.jadu.trackdown.presentation.common.shimmerBrush
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
    val graphModeSelected = remember { mutableStateOf(false) }
    val showShimmer = remember { mutableStateOf(true) }
    val imageState = viewModel.logoState.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.getImage(symbol)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Company Details") }
            )
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                if (state.error == null) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        item {
                            state.company?.let { data ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if(imageState?.image.isNullOrEmpty()){
                                        AsyncImage(
                                            model = "",
                                            contentDescription = "Company Logo",
                                            modifier = Modifier
                                                .height(100.dp)
                                                .width(100.dp)
                                                .clip(CircleShape)
                                                .border(
                                                    0.5.dp,
                                                    Color.Gray.copy(alpha = 0.2f),
                                                    CircleShape
                                                )
                                                .background(
                                                    shimmerBrush(
                                                        targetValue = 1300f,
                                                        showShimmer = showShimmer.value
                                                    )
                                                ),
                                            onSuccess = { showShimmer.value = false },
                                            contentScale = ContentScale.Fit
                                        )
                                    }else{
                                        AsyncImage(
                                            model = imageState?.image,
                                            contentDescription = "Company Logo",
                                            modifier = Modifier
                                                .height(100.dp)
                                                .width(100.dp)
                                                .clip(CircleShape)
                                                .border(
                                                    0.5.dp,
                                                    Color.Gray.copy(alpha = 0.2f),
                                                    CircleShape
                                                )
                                                .background(
                                                    shimmerBrush(
                                                        targetValue = 1300f,
                                                        showShimmer = showShimmer.value
                                                    )
                                                ),
                                            onSuccess = { showShimmer.value = false },
                                            contentScale = ContentScale.Fit
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column {
                                        Text(
                                            text = data.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = data.symbol,
                                            fontStyle = FontStyle.Italic,
                                            fontSize = 14.sp,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Country: ${data.country}",
                                            fontSize = 14.sp,
                                            modifier = Modifier.fillMaxWidth(),
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                if (state.stockInfos.isNotEmpty() || state.dailyInfos.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(text = "Market Summary")
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .border(
                                                BorderStroke(0.5.dp, Color.Gray),
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .height(300.dp)
                                                .fillMaxWidth()
                                        ) {
                                            ShowGraph(
                                                state.stockInfos,
                                                state.dailyInfos,
                                                graphModeSelected
                                            )
                                        }
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize(),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            TimePeriodSelector(viewModel, graphModeSelected)
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                        item {
                            //About section
                            state.company?.let { data ->
                                Box(
                                    modifier = Modifier
                                        .border(
                                            BorderStroke(0.5.dp, Color.Gray),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .background(Color.Transparent)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(8.dp)
                                    ) {
                                        Text(
                                            text = "About ${data.name}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        HorizontalDivider(modifier = Modifier.fillMaxWidth())
                                        Text(
                                            text = data.description,
                                            fontSize = 12.sp,
                                            modifier = Modifier.fillMaxWidth(),
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Column(
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .background(
                                                        color = Color(0xffedd4c6),
                                                        shape = RoundedCornerShape(16.dp)
                                                    )
                                                    .height(24.dp)
                                            ) {
                                                Text(
                                                    text = "Industry: ${data.industry}",
                                                    textAlign = TextAlign.Center,
                                                    fontSize = 12.sp,
                                                    maxLines = 1,
                                                    modifier = Modifier.padding(4.dp),
                                                    overflow = TextOverflow.Ellipsis,
                                                    style = TextStyle(color = Color(0xffb67754),fontWeight = FontWeight.Bold)
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Box(
                                                modifier = Modifier
                                                    .background(
                                                        color = Color(0xffedd4c6),
                                                        shape = RoundedCornerShape(12.dp)
                                                    )
                                                    .height(24.dp)
                                            ) {
                                                Text(
                                                    textAlign = TextAlign.Center,
                                                    text = "Sector: ${data.sector}",
                                                    fontSize = 12.sp,
                                                    maxLines = 1,
                                                    modifier = Modifier.padding(4.dp),
                                                    overflow = TextOverflow.Ellipsis,
                                                    style = TextStyle(color = Color(0xffb67754), fontWeight = FontWeight.Bold)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column {
                                                Text(text = "52 Weeks Low",style = TextStyle(color = Color.Gray))
                                                Text(text = data.fiftyTwoWeeksLow,style = TextStyle(fontWeight = FontWeight.Bold))
                                            }
                                            Column {
                                                Text(text = "52 Weeks High", style = TextStyle(color = Color.Gray))
                                                Text(text = "$"+data.fiftyTwoWeeksHigh, style = TextStyle(fontWeight = FontWeight.Bold))
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            // First column
                                            Column(
                                                modifier = Modifier.weight(1f),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                HeadingAndSubtitleText(
                                                    title = "Market Cap",
                                                    subtitle = data.marketCap
                                                )
                                            }

                                            // Second column
                                            Column(
                                                modifier = Modifier.weight(1f),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                HeadingAndSubtitleText(
                                                    title = "PE Ratio",
                                                    subtitle = data.peRatio
                                                )
                                            }

                                            // Third column
                                            Column(
                                                modifier = Modifier.weight(1f),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                HeadingAndSubtitleText(
                                                    title = "Beta",
                                                    subtitle = data.beta
                                                )
                                            }
                                        }

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceEvenly
                                        ) {
                                            // Fourth column
                                            Column(
                                                modifier = Modifier.weight(1f),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                HeadingAndSubtitleText(
                                                    title = "Dividend Yield",
                                                    subtitle = data.dividendYield
                                                )
                                            }

                                            // Fifth column
                                            Column(
                                                modifier = Modifier.weight(1f),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                HeadingAndSubtitleText(
                                                    title = "Profit Margin",
                                                    subtitle = data.profitMargin
                                                )
                                            }
                                        }

                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                } else {
                    if (state.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Text(
                            text = state.error,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    )

}

@Composable
fun HeadingAndSubtitleText(title:String, subtitle:String){
    Column {
        Text(
            text = title,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(color = Color.Gray)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subtitle,
            fontStyle = FontStyle.Normal,
            fontSize = 12.sp,
            style = TextStyle(fontWeight = FontWeight.Bold)
        )
    }

}


@Composable
fun TimePeriodSelector(viewModel: StockDetailsViewModel, graphModeSelected: MutableState<Boolean>) {
    val timePeriods = listOf("1D", "1W", "1M", "3M", "6M", "1Y")
    var selectedPeriod by remember { mutableStateOf("1D") }

    Row(
        modifier = Modifier
            .padding(8.dp)
            .border(BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(50))
            .padding(horizontal = 4.dp, vertical = 4.dp)
    ) {
        timePeriods.forEach { period ->
            val isEnabled = when (period) {
                "1D", "1W", "1M" -> true
                else -> false
            }

            Text(
                text = period,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .clickable(enabled = isEnabled) {
                        if (isEnabled) {
                            selectedPeriod = period
                            if (period != "1D") {
                                graphModeSelected.value = true
                                viewModel.updateStockInfo(period)
                            } else {
                                viewModel.getInfo()
                                graphModeSelected.value = false
                            }
                        }
                    }
                    .background(
                        if (selectedPeriod == period && isEnabled) Color(0xFFD35400) else Color.Gray,
                        shape = RoundedCornerShape(50)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                color = if (selectedPeriod == period && isEnabled) Color.White else Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun ShowGraph(
    stockInfos: List<IntraDayInfo>,
    dailyInfos: List<DailyStockInfo>,
    graphModeSelected: MutableState<Boolean>
) {
    Log.d("ShowGraph", "DailyInfos: $dailyInfos")
    val dates = stockInfos.map { it.date.toString() } // Convert LocalDateTime to String
    val closingPrices = stockInfos.map { it.close.toFloat() }

    val dailyDates = dailyInfos.map { it.date.toString() }
    val dailyClosingPrices = dailyInfos.map { it.close.toFloat() }

    if (dates.isNotEmpty() && closingPrices.isNotEmpty() && !graphModeSelected.value) {
        val dataLabel = "Stock Data"
        LineGraph(
            xData = dates,
            yData = closingPrices,
            dataLabel = dataLabel
        )
        try {
            Log.d("ParseJson", "Parsed data: dates = $dates, closingPrices = $closingPrices")
        } catch (e: Exception) {
            Log.e("ParseJson", "Error logging data", e)
        }
    } else {
        LineGraph(
            xData = dailyDates,
            yData = dailyClosingPrices,
            dataLabel = "Daily Stock Data"
        )
    }
}


@Composable
fun LineGraph(
    xData: List<String>,
    yData: List<Float>,
    dataLabel: String,
    modifier: Modifier = Modifier,
    axisTextColor: Color = Color.Gray,
    descriptionEnabled: Boolean = false,
    legendEnabled: Boolean = true,
    yAxisRightEnabled: Boolean = false,
    xAxisPosition: XAxis.XAxisPosition = XAxis.XAxisPosition.BOTTOM,
    legendOffset: Float = 20f,
    pointRadius: Float = 1f // New parameter for point radius
) {
    if (xData.isEmpty() || yData.isEmpty()) {
        // Show a circular progress indicator or any placeholder when data is empty
        Box(
            modifier = modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center),
            contentAlignment = Alignment.Center
        ) {
            val rawComposition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.loadinggraph))
            val progress by animateLottieCompositionAsState(composition = rawComposition)
            LottieAnimation(
                composition = rawComposition,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds,
            )
        }
    } else {
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

                // Set extra offset to the legend title
                chart.setExtraOffsets(0f, 0f, 0f, legendOffset)
                chart.invalidate()
                chart
            }
        )
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



