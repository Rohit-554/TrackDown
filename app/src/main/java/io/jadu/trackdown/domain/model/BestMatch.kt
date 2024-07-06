package io.jadu.trackdown.domain.model

data class BestMatch(
    val symbol: String?,
    val name: String?,
    val type: String?,
    val region: String?,
    val marketOpen: String?,
    val marketClose: String?,
    val timezone: String?,
    val currency: String?,
    val matchScore: String?
)