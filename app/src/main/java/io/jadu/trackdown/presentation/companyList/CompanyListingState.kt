package io.jadu.trackdown.presentation.companyList

import io.jadu.trackdown.domain.model.AutoQueryModel
import io.jadu.trackdown.domain.model.CompanyListing

data class CompanyListingState(
    val companies: List<CompanyListing> = emptyList(),
    val searchQuerySuggestions : AutoQueryModel = AutoQueryModel(emptyList()),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = ""
)
