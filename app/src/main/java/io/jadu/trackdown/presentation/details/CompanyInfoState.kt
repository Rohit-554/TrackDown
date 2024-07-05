package io.jadu.trackdown.presentation.details

import io.jadu.trackdown.domain.model.CompanyInfo
import io.jadu.trackdown.domain.model.IntraDayInfo

data class CompanyInfoState(
    val stockInfos: List<IntraDayInfo> = emptyList(),
    val company: CompanyInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
