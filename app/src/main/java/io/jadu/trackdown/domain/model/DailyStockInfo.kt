package io.jadu.trackdown.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

data class DailyStockInfo(
    val date: LocalDate,
    val close: Double
)
