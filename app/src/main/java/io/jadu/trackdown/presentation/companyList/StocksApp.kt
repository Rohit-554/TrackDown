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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
    val coroutineScope = rememberCoroutineScope()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = viewModel.state.isRefreshing)
    val state = viewModel.state
    var isDropdownMenuExpanded by remember { mutableStateOf(false) }

    Scaffold { padding ->
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CustomAppBar(state = state)
                AutoComplete(
                    value = state.searchQuery,
                    searchList = state.searchQuerySuggestions,
                    onValueChange = {
                        viewModel.onEvent(
                            CompanyListingEvents.OnSearchQueryChange(it)
                        )
                    }
                )

            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    viewModel.onEvent(CompanyListingEvents.Refresh)
                }
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2), // Set the number of columns
                    modifier = Modifier.fillMaxSize().padding(start = 8.dp, end = 8.dp)
                ) {
                    items(state.companies.size) { companyData ->
                        StockCard(
                            company = state.companies[companyData],
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .clickable {
                                    navController.navigate(Screen.CompanyDetail.withArgs(state.companies[companyData].symbol))
                                },
                            viewModel = viewModel
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            CustomBottomAppBar()
        }
    }
}





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAppBar(title: String = "Stock App", state: CompanyListingState) {
    TopAppBar(title = { Text(text = title) })
}


@Composable
fun StockCard(
    company: CompanyListing,
    modifier: Modifier,
    viewModel: CompanyListingViewModel
) {
    val coroutineScope = rememberCoroutineScope()
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
            Modifier.size(220.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                AsyncImage(
                    model = {
                    },
                    contentDescription = "Company Logo",
                    modifier = Modifier
                        .height(48.dp)
                        .width(48.dp)
                        .clip(CircleShape)
                        .border(0.5.dp, Color.Gray.copy(alpha = 0.2f), CircleShape),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = company.name,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
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

