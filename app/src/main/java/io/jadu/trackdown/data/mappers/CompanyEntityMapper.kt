package io.jadu.trackdown.data.mappers

import io.jadu.trackdown.data.local.CompanyListingModel
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