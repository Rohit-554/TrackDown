package io.jadu.trackdown.data.remote.dto

import io.jadu.trackdown.BuildConfig
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("query?function=LISTING_STATUS")
    suspend fun getCompanyList(
        @Query("apikey") apiKey: String = STOCK_API_KEY,
    ):ResponseBody

    companion object {
        const val STOCK_API_KEY = BuildConfig.STOCK_API_KEY
        const val BASE_URL = "https://www.alphavantage.co"
    }
}