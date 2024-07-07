package io.jadu.trackdown.domain.repository

import androidx.room.Query
import io.jadu.trackdown.domain.model.AutoQueryModel
import io.jadu.trackdown.domain.model.CompanyInfo
import io.jadu.trackdown.domain.model.CompanyListing
import io.jadu.trackdown.domain.model.DailyStockInfo
import io.jadu.trackdown.domain.model.IntraDayInfo
import io.jadu.trackdown.domain.model.LogoModel
import io.jadu.trackdown.util.Resource
import kotlinx.coroutines.flow.Flow
import okhttp3.Response
import okhttp3.ResponseBody

interface CompanyRepository {
    suspend fun getCompanyList(
        getFromRemoteSource: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>>

    suspend fun getIntraDayInfo(
        symbol: String
    ): Resource<List<IntraDayInfo>>

    suspend fun getDailyInfo(
        symbol: String
    ): Resource<List<DailyStockInfo>>

    suspend fun getWeeklyInfo(
        symbol: String
    ): Resource<List<DailyStockInfo>>

    suspend fun getMonthlyInfo(
        symbol: String
    ): Resource<List<DailyStockInfo>>


    suspend fun getCompanyInfo(
        symbol: String
    ): Resource<CompanyInfo>

    suspend fun getCompanyLogo(
        ticker: String
    ): Resource<LogoModel>

    suspend fun searchCompany(
        keywords: String
    ): Flow<Resource<AutoQueryModel>>
}