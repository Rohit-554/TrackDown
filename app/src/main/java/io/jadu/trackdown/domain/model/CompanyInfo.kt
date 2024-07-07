package io.jadu.trackdown.domain.model

data class CompanyInfo(
    val symbol: String,
    val description: String,
    val name: String,
    val country: String,
    val industry: String,
    val sector: String,
    val marketCap: String,
    val peRatio: String,
    val beta: String,
    val dividendYield: String,
    val profitMargin: String,
    val fiftyTwoWeeksLow: String,
    val fiftyTwoWeeksHigh: String,
    val fiftyDayMovingAverage: String
)