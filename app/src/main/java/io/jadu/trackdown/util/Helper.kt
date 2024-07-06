package io.jadu.trackdown.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object Helper {

    fun formatTo12HourTime(dateTimeStrings: List<String>): List<String> {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
        val outputFormatter = DateTimeFormatter.ofPattern("hh:mm a")

        return dateTimeStrings.map { dateTimeString ->
            try {
                val dateTime = LocalDateTime.parse(dateTimeString, inputFormatter)
                dateTime.format(outputFormatter)
            } catch (e: DateTimeParseException) {
                dateTimeString
            }
        }
    }

    fun sortDataByDateTime(xData: List<String>, yData: List<Float>): Pair<List<String>, List<Float>> {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")

        val dateTimeFloatPairs = xData.zip(yData).mapNotNull { (dateTimeString, yValue) ->
            try {
                val dateTime = LocalDateTime.parse(dateTimeString, inputFormatter)
                Pair(dateTime, yValue)
            } catch (e: DateTimeParseException) {
                null
            }
        }

        val sortedPairs = dateTimeFloatPairs.sortedBy { it.first }

        val sortedXData = sortedPairs.map { it.first.format(inputFormatter) }
        val sortedYData = sortedPairs.map { it.second }

        return Pair(sortedXData, sortedYData)
    }
}