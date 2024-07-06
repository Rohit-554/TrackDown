package io.jadu.trackdown.data.csv

import com.opencsv.CSVReader
import io.jadu.trackdown.data.mappers.toDailyInfoDto
import io.jadu.trackdown.data.remote.dto.DailyInfoDto
import io.jadu.trackdown.data.remote.dto.IntraDayInfoDto
import io.jadu.trackdown.domain.model.DailyStockInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DailyInfoParser @Inject constructor(): CSVParser<DailyStockInfo>{
    override suspend fun parse(stream: InputStream): List<DailyStockInfo> {
        val csvReader = CSVReader(InputStreamReader(stream))
        return withContext(Dispatchers.IO){
            csvReader.readAll().drop(1)
                .mapNotNull { line->
                    val timestamp = line.getOrNull(0) ?: return@mapNotNull null
                    val close = line.getOrNull(4) ?: return@mapNotNull null
                    val dto = DailyInfoDto(timestamp,close.toDouble())
                    dto.toDailyInfoDto()
                }
                .filter {
                    it.date.dayOfMonth == LocalDateTime.now().minusDays(1).dayOfMonth
                }
                .also {
                    csvReader.close()
                }
        }
    }
}