import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TimeSeriesResponse(
    @SerialName("Meta Data")
    val metaData: MetaData,

    @SerialName("Time Series (Daily)")
    val timeSeries: Map<String, DailyTimeSeries>
)

@Serializable
data class MetaData(
    @SerialName("1. Information")
    val information: String,

    @SerialName("2. Symbol")
    val symbol: String,

    @SerialName("3. Last Refreshed")
    val lastRefreshed: String,

    @SerialName("4. Output Size")
    val outputSize: String,

    @SerialName("5. Time Zone")
    val timeZone: String
)

@Serializable
data class DailyTimeSeries(
    @SerialName("1. open")
    val open: String,

    @SerialName("2. high")
    val high: String,

    @SerialName("3. low")
    val low: String,

    @SerialName("4. close")
    val close: String,

    @SerialName("5. volume")
    val volume: String
)
