package io.jadu.trackdown.data.mappers

import io.jadu.trackdown.data.local.CompanyListingModel
import io.jadu.trackdown.data.remote.dto.CompanyInfoDto
import io.jadu.trackdown.domain.model.CompanyInfo
import io.jadu.trackdown.domain.model.CompanyListing

fun CompanyListingModel.toCompanyListing(): CompanyListing {
    return CompanyListing(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyListing.toCompanyListingModel(): CompanyListingModel {
    return CompanyListingModel(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyInfoDto.toCompanyInfo(): CompanyInfo {
    return CompanyInfo(
        symbol = symbol ?: "",
        description = description ?: "",
        name = name ?: "",
        country = country ?: "",
        industry = industry ?: "",
        sector = sector?: "",
        marketCap = marketCap?: "",
        peRatio = peRatio?: "",
        beta = beta?: "",
        dividendYield = dividendYield?: "",
        profitMargin = profitMargin?: "",
        fiftyTwoWeeksLow = fiftyTwoWeeksLow?: "",
        fiftyTwoWeeksHigh = fiftyTwoWeeksHigh?: "",
        fiftyDayMovingAverage = fiftyDayMovingAverage?: ""
    )
}