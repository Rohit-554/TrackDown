package io.jadu.trackdown.data.mappers

import io.jadu.trackdown.data.remote.dto.DailyInfoDto
import io.jadu.trackdown.domain.model.DailyStockInfo
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun DailyInfoDto.toDailyInfoDto(): DailyStockInfo {
    val pattern = "yyyy-MM-dd"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    val localDate = LocalDate.parse(timestamp, formatter)
    return DailyStockInfo(
        date = localDate,
        close = close
    )
}
