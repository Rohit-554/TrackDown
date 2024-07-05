package io.jadu.trackdown.presentation.companyList

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import io.jadu.trackdown.domain.model.CompanyListing
import io.jadu.trackdown.presentation.navigation.Screen

@Composable
fun StocksApp(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: CompanyListingViewModel = hiltViewModel()
) {

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = viewModel.state.isRefreshing)
    val state = viewModel.state


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CustomAppBar(state = state)
        OutlinedTextField(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            value = state.searchQuery, onValueChange = {
                viewModel.onEvent(
                    CompanyListingEvents.OnSearchQueryChange(it)
                )
            },
            placeholder = {
                Text(text = "Search Company")
            },
            maxLines = 1,
            singleLine = true
        )
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                viewModel.onEvent(CompanyListingEvents.Refresh)
            }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.companies.size) { data ->
                    val companyData = state.companies[data]
                    StockCard(company = companyData, modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable {
                            navController.navigate(Screen.CompanyDetail.withArgs(companyData.symbol))
                        }
                    )
                    /*CompanyOverAllStocksCard(company = companyData,modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable {

                        })*/
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        CustomBottomAppBar()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAppBar(title: String = "Stock App", state: CompanyListingState) {
    TopAppBar(title = { Text(text = title) })
}

/*@Composable
fun CompanyOverAllStocksCard(company: CompanyListing,modifier: Modifier) {
    val stockList: List<StockModelClass> = listOf(
        StockModelClass(
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT9-uBexCbY-wzK-6K2z65xoP3e-aLoF1QL0Q&s",
            "Alphabet Inc. - Class A Shares (GOOGL)",
            "$139.72",
            "+0.45"
        ),
        StockModelClass(
            "https://substackcdn.com/image/fetch/f_auto,q_auto:good,fl_progressive:steep/https%3A%2F%2Fsubstack-post-media.s3.amazonaws.com%2Fpublic%2Fimages%2F8ed3d547-94ff-48e1-9f20-8c14a7030a02_2000x2000.jpeg",
            "Apple",
            "$151.4",
            "+0.88"
        ),
        StockModelClass(
            "https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Microsoft_logo.svg/1024px-Microsoft_logo.svg.png",
            "Microsoft",
            "332.06",
            "+0.41"
        ),
        StockModelClass(
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT9-uBexCbY-wzK-6K2z65xoP3e-aLoF1QL0Q&s",
            "Alphabet Inc. - Class A Shares (GOOGL)",
            "$139.72",
            "+0.45"
        ),
        StockModelClass(
            "https://substackcdn.com/image/fetch/f_auto,q_auto:good,fl_progressive:steep/https%3A%2F%2Fsubstack-post-media.s3.amazonaws.com%2Fpublic%2Fimages%2F8ed3d547-94ff-48e1-9f20-8c14a7030a02_2000x2000.jpeg",
            "Apple",
            "$151.4",
            "+0.88"
        ),
        StockModelClass(
            "https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Microsoft_logo.svg/1024px-Microsoft_logo.svg.png",
            "Microsoft",
            "332.06",
            "+0.41"
        ),
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(16.dp)
    ) {
        items(stockList) { stock ->
            StockCard(stock,company,modifier)
        }
    }
}*/

@Composable
fun StockCard(company: CompanyListing, modifier: Modifier) {
    Card(
        modifier = modifier,
        colors = CardColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black,
            disabledContentColor = Color.Gray,
            disabledContainerColor = Color.Transparent,
        ),
        shape = RoundedCornerShape(8.dp), // Adjust the corner shape if needed
        border = BorderStroke(0.5.dp, Color.LightGray) // Add a stroke with color and width
    ) {
        Box(
            Modifier.size(200.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                AsyncImage(
                    model = "",
                    contentDescription = "Company Logo",
                    modifier = Modifier
                        .height(48.dp)
                        .width(48.dp)
                        .clip(CircleShape)
                        .border(0.5.dp, Color.Gray.copy(alpha = 0.2f), CircleShape),
                    contentScale = ContentScale.Fit
                )
                Text(text = company.name)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = company.symbol, color = Color.Gray)
                Text(
                    text = company.exchange,
                    /*color = if (stock.stockChange.startsWith("+")) Color.Green.copy(alpha = 0.5f) else Color.Red*/
                )
            }
        }
    }
}

@Composable
fun CustomBottomAppBar() {
    BottomAppBar(
        modifier = Modifier
            .height(56.dp)
            .clip(RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp))
            .border(
                BorderStroke(1.dp, Color.Gray),
                shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp)
            ),
        tonalElevation = 0.dp,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable { /* Handle click for TOP GAINERS */ },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "TOP GAINERS",
                    textAlign = TextAlign.Center
                )
            }
            VerticalDivider(
                modifier = Modifier
                    .height(56.dp)
                    .width(1.dp),
                color = Color.Black
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable { /* Handle click for TOP LOSERS */ },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "TOP LOSERS",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


data class StockModelClass(
    val image: String,
    val companyName: String,
    val stockPrice: String,
    val stockChange: String
)

