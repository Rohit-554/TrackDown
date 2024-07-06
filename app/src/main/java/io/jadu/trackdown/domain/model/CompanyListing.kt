package io.jadu.trackdown.domain.model

//A data class that will hold the information for the ui
data class CompanyListing (
    val name:String,
    val symbol:String,
    val exchange:String,
    val logoUrl:String? = null
)