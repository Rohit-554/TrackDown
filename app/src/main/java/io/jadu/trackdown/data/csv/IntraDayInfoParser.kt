package io.jadu.trackdown.data.csv

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.opencsv.CSVReader
import io.jadu.trackdown.data.mappers.toIntraDayInfo
import io.jadu.trackdown.data.remote.dto.IntraDayInfoDto
import io.jadu.trackdown.domain.model.CompanyListing
import io.jadu.trackdown.domain.model.IntraDayInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IntraDayInfoParser @Inject constructor() : CSVParser<IntraDayInfo> {
    override suspend fun parse(stream: InputStream): List<IntraDayInfo> {
        val csvReader = CSVReader(InputStreamReader(stream))
        return withContext(Dispatchers.IO) {
            val allLines = csvReader.readAll().drop(1)
            val parsedData = allLines.mapNotNull { line ->
                val timestamp = line.getOrNull(0) ?: run {
                    Log.e("IntraDayInfoParser", "Missing timestamp in line: $line")
                    return@mapNotNull null
                }
                val close = line.getOrNull(4) ?: run {
                    Log.e("IntraDayInfoParser", "Missing close price in line: $line")
                    return@mapNotNull null
                }
                try {
                    val dto = IntraDayInfoDto(timestamp, close.toDouble())
                    dto.toIntraDayInfo()
                } catch (e: Exception) {
                    Log.e("IntraDayInfoParser", "Error parsing line: $line", e)
                    null
                }
            }

            val maxDate = parsedData.maxByOrNull { it.date }?.date
            Log.d("IntraDayInfoParser", "Max date found: $maxDate")

            val filteredData = parsedData.filter {
                it.date.toLocalDate() == maxDate?.toLocalDate()
            }
                .sortedBy {
                    it.date.hour
                }
                .also {
                    csvReader.close()
                }
            filteredData
        }
    }
}


