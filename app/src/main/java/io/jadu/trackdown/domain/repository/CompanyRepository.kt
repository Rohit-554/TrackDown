package io.jadu.trackdown.domain.repository

import androidx.room.Query
import io.jadu.trackdown.domain.model.CompanyInfo
import io.jadu.trackdown.domain.model.CompanyListing
import io.jadu.trackdown.domain.model.IntraDayInfo
import io.jadu.trackdown.util.Resource
import kotlinx.coroutines.flow.Flow

interface CompanyRepository {
    suspend fun getCompanyList(
        getFromRemoteSource: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>>

    suspend fun getIntraDayInfo(
        symbol: String
    ): Resource<List<IntraDayInfo>>

    suspend fun getCompanyInfo(
        symbol: String
    ): Resource<CompanyInfo>

}