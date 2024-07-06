package io.jadu.trackdown.data.remote.dto

import android.util.Log
import io.jadu.trackdown.BuildConfig
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("query?function=LISTING_STATUS")
    suspend fun getCompanyList(
        @Query("apikey") apiKey: String = STOCK_API_KEY,
    ):ResponseBody

    @GET("query?function=TIME_SERIES_INTRADAY&interval=5min&datatype=csv")
    suspend fun getIntradayInfo(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = STOCK_API_KEY
    ): ResponseBody

    @GET("query?function=TIME_SERIES_DAILY&datatype=csv")
    suspend fun getDailyInfo(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = STOCK_API_KEY
    ): ResponseBody

    @GET("query?function=TIME_SERIES_WEEKLY&datatype=csv")
    suspend fun getWeeklyInfo(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = STOCK_API_KEY
    ): ResponseBody

    @GET("query?function=TIME_SERIES_MONTHLY&datatype=csv")
    suspend fun getMonthlyInfo(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = STOCK_API_KEY
    ): ResponseBody


    @GET("query?function=OVERVIEW")
    suspend fun getCompanyInfo(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = STOCK_API_KEY
    ): CompanyInfoDto


    companion object {
        const val STOCK_API_KEY = BuildConfig.STOCK_API_KEY
        const val BASE_URL = "https://www.alphavantage.co"
        init {
            Log.d("ApiServicex", STOCK_API_KEY)
        }

    }
}