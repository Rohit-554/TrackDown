package io.jadu.trackdown.data.repository

import android.util.Log
import coil.network.HttpException
import com.opencsv.CSVReader
import io.jadu.trackdown.BuildConfig
import io.jadu.trackdown.data.csv.CSVParser
import io.jadu.trackdown.data.csv.CompanyListingParser
import io.jadu.trackdown.data.csv.IntraDayInfoParser
import io.jadu.trackdown.data.local.Database
import io.jadu.trackdown.data.mappers.toCompanyInfo
import io.jadu.trackdown.data.mappers.toCompanyListing
import io.jadu.trackdown.data.mappers.toCompanyListingModel
import io.jadu.trackdown.data.remote.dto.ApiService
import io.jadu.trackdown.domain.model.CompanyInfo
import io.jadu.trackdown.domain.model.CompanyListing
import io.jadu.trackdown.domain.model.DailyStockInfo
import io.jadu.trackdown.domain.model.IntraDayInfo
import io.jadu.trackdown.domain.repository.CompanyRepository
import io.jadu.trackdown.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.internal.connection.Exchange
import okio.IOException
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanyRepositoryImpl @Inject constructor(
    val api: ApiService,
    val db: Database,
    val companyListingParser: CSVParser<CompanyListing>,
    private val intraDayInfoParser: CSVParser<IntraDayInfo>,
    private val dailyInfoParser: CSVParser<DailyStockInfo>
) : CompanyRepository {

    private val dao = db.companyDao()

    init {
        val STOCK_API_KEY = BuildConfig.STOCK_API_KEY
        Log.d("ApiServicex", STOCK_API_KEY)
    }

    override suspend fun getCompanyList(
        getFromRemoteSource: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListings = dao.searchCompanyListing(query)
            emit(Resource.Success(
                data = localListings.map { it.toCompanyListing() }
            ))

            val isLocalEmpty = localListings.isEmpty() && query.isBlank()
            val shouldFetchFromCache = !isLocalEmpty && !getFromRemoteSource
            if (shouldFetchFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }
            val remoteListings = try {
                val response = api.getCompanyList()
                Log.d("CompanyRepositoryImpl", "Response: ${response.byteStream().toString()}")
                companyListingParser.parse(response.byteStream())
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Network Failure"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Network Failure"))
                null
            }

            remoteListings?.let { data ->
                dao.deleteAllCompanyList()
                dao.insertCompanyList(
                    data.map { it.toCompanyListingModel() }
                )
                emit(Resource.Success(
                    data = dao.searchCompanyListing("").map { it.toCompanyListing() }
                ))
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun getIntraDayInfo(symbol: String): Resource<List<IntraDayInfo>> {
        return try {
            val response = api.getIntradayInfo(symbol)
            val results = intraDayInfoParser.parse(response.byteStream())
            Log.d("CompanyRepositoryImpl", "Response: ${results}")
            Resource.Success(data = results)
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(message = "Network Failure")
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(message = "Network Failure")
        }
    }

    override suspend fun getDailyInfo(symbol: String): Resource<List<DailyStockInfo>> {
        return try {
            val response = api.getDailyInfo(symbol)
            val results = dailyInfoParser.parse(response.byteStream())
            Log.d("CompanyRepositoryImpl", "Response: ${results}")
            Resource.Success(data = results)
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(message = "Network Failure")
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(message = "Network Failure")
        }
    }

    override suspend fun getWeeklyInfo(symbol: String): Resource<List<DailyStockInfo>> {
        return try {
            val response = api.getWeeklyInfo(symbol)
            val results = dailyInfoParser.parse(response.byteStream())
            Log.d("CompanyRepositoryImpl", "Response: ${results}")
            Resource.Success(data = results)
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(message = "Network Failure")
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(message = "Network Failure")
        }
    }

    override suspend fun getMonthlyInfo(symbol: String): Resource<List<DailyStockInfo>> {
        return try {
            val response = api.getMonthlyInfo(symbol)
            val results = dailyInfoParser.parse(response.byteStream())
            Log.d("CompanyRepositoryImpl", "Response: ${results}")
            Resource.Success(data = results)
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(message = "Network Failure")
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(message = "Network Failure")
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> {
        return try {
            val result = api.getCompanyInfo(symbol)
            Resource.Success(result.toCompanyInfo())
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(message = "Network Failure")
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(message = "Network Failure")
        }
    }
}