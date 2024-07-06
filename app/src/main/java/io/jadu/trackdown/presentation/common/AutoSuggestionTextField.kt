package io.jadu.trackdown.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import io.jadu.trackdown.domain.model.AutoQueryModel
import io.jadu.trackdown.domain.model.BestMatch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoComplete(value: String, onValueChange: (String) -> Unit, searchList: AutoQueryModel) {


    //problem fetching search list from api assigning pre-list values
    val newSearchList = AutoQueryModel(
        bestMatches = listOf(
            BestMatch(
                symbol = "AAPL",
                name = "Apple Inc.",
                type = "Equity",
                region = "United States",
                marketOpen = "09:30",
                marketClose = "16:00",
                timezone = "UTC-04",
                currency = "USD",
                matchScore = "1.0000"
            ),
            BestMatch(
                symbol = "MSFT",
                name = "Microsoft Corporation",
                type = "Equity",
                region = "United States",
                marketOpen = "09:30",
                marketClose = "16:00",
                timezone = "UTC-04",
                currency = "USD",
                matchScore = "0.8889"
            ),
            BestMatch(
                symbol = "GOOGL",
                name = "Alphabet Inc.",
                type = "Equity",
                region = "United States",
                marketOpen = "09:30",
                marketClose = "16:00",
                timezone = "UTC-04",
                currency = "USD",
                matchScore = "0.8000"
            ),
            BestMatch(
                symbol = "AMZN",
                name = "Amazon.com Inc.",
                type = "Equity",
                region = "United States",
                marketOpen = "09:30",
                marketClose = "16:00",
                timezone = "UTC-04",
                currency = "USD",
                matchScore = "0.7500"
            ),
            BestMatch(
                symbol = "TSLA",
                name = "Tesla Inc.",
                type = "Equity",
                region = "United States",
                marketOpen = "09:30",
                marketClose = "16:00",
                timezone = "UTC-04",
                currency = "USD",
                matchScore = "0.7000"
            ),
            BestMatch(
                symbol = "FB",
                name = "Meta Platforms Inc.",
                type = "Equity",
                region = "United States",
                marketOpen = "09:30",
                marketClose = "16:00",
                timezone = "UTC-04",
                currency = "USD",
                matchScore = "0.6500"
            ),
            BestMatch(
                symbol = "NVDA",
                name = "NVIDIA Corporation",
                type = "Equity",
                region = "United States",
                marketOpen = "09:30",
                marketClose = "16:00",
                timezone = "UTC-04",
                currency = "USD",
                matchScore = "0.6000"
            ),
        )
    )


    var category by remember {
        mutableStateOf("")
    }

    val heightTextFields by remember {
        mutableStateOf(55.dp)
    }

    var textFieldSize by remember {
        mutableStateOf(Size.Zero)
    }

    var expanded by remember {
        mutableStateOf(false)
    }
    val interactionSource = remember {
        MutableInteractionSource()
    }

    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    expanded = false
                }
            )
    ) {

        Column(modifier = Modifier.fillMaxWidth()) {

            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(heightTextFields)
                        .border(
                            width = 1.8.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .onGloballyPositioned { coordinates ->
                            textFieldSize = coordinates.size.toSize()
                        },
                    value = value,
                    onValueChange = {
                        onValueChange(it)
                        expanded = true
                    },
                    placeholder = {
                        Text(
                            "Search Stocks, Etfs..",
                            style = TextStyle(
                                color = Color.Gray,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp
                            )
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.Gray
                    ),
                    textStyle = TextStyle(
                        color = Color.Gray,
                        fontSize = 16.sp
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = Icons.Rounded.KeyboardArrowDown,
                                contentDescription = "arrow",
                                tint = Color.Gray
                            )
                        }
                    }
                )
            }

            AnimatedVisibility(visible = expanded) {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .width(textFieldSize.width.dp),
                    shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomEnd = 8.dp, bottomStart = 8.dp),
                ) {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 150.dp),
                    ) {
                        val filteredList = newSearchList.bestMatches.filter {
                            it.symbol?.contains(value, ignoreCase = true) == true
                        }

                        if (filteredList.isNotEmpty()) {
                            items(filteredList) { match ->
                                ItemsCategory(title = match.symbol ?: "") { title ->
                                    onValueChange(title)
                                    expanded = false
                                }
                            }
                        }
                    }

                }
            }

        }

    }

}

@Composable
fun ItemsCategory(
    title: String,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onSelect(title)
            }
            .padding(10.dp)
    ) {
        Text(text = title, fontSize = 16.sp)
    }
}

