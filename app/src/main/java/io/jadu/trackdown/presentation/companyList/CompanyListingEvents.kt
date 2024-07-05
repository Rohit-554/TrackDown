package io.jadu.trackdown.presentation.companyList

sealed class CompanyListingEvents {
    object Refresh: CompanyListingEvents()
    data class OnSearchQueryChange(val query: String): CompanyListingEvents()
}