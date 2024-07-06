package io.jadu.trackdown.data.remote.dto

import android.util.Log
import io.jadu.trackdown.BuildConfig
import io.jadu.trackdown.data.remote.dto.ApiService.Companion
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface LogoApiService {

    @GET("logo")
    suspend fun getCompanyLogo(
        @Query("ticker") ticker: String,
        @Header("X-Api-Key") apiKey: String = ApiService.LOGO_API_KEY
    ): ResponseBody

    companion object {
        const val STOCK_API_KEY = BuildConfig.STOCK_API_KEY
        const val LOGO_BASE_URL = "https://api.api-ninjas.com/v1/"
        const val LOGO_API_KEY = BuildConfig.LOGO_API_KEY
        const val BASE_URL = "https://www.alphavantage.co"
        init {
            Log.d("ApiServicex", STOCK_API_KEY)
        }

    }
}