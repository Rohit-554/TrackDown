package io.jadu.trackdown.domain.repository

import androidx.room.Query
import io.jadu.trackdown.domain.model.CompanyListing
import io.jadu.trackdown.util.Resource
import kotlinx.coroutines.flow.Flow

interface CompanyRepository {
    suspend fun getCompanyList(
        getFromRemoteSource: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>>
}