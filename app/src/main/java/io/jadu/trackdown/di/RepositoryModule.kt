package io.jadu.trackdown.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.jadu.trackdown.data.csv.CSVParser
import io.jadu.trackdown.data.csv.CompanyListingParser
import io.jadu.trackdown.data.csv.DailyInfoParser
import io.jadu.trackdown.data.csv.IntraDayInfoParser
import io.jadu.trackdown.data.repository.CompanyRepositoryImpl
import io.jadu.trackdown.domain.model.CompanyListing
import io.jadu.trackdown.domain.model.DailyStockInfo
import io.jadu.trackdown.domain.model.IntraDayInfo
import io.jadu.trackdown.domain.repository.CompanyRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCompanyListingsParser(
        companyListingParser: CompanyListingParser
    ): CSVParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindIntraDayInfoParser(
        intraDayInfoParser: IntraDayInfoParser
    ): CSVParser<IntraDayInfo>

    @Binds
    @Singleton
    abstract fun bindDailyInfoParser(
        dailyInfoParser: DailyInfoParser
    ): CSVParser<DailyStockInfo>

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepository: CompanyRepositoryImpl
    ): CompanyRepository
}