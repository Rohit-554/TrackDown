package io.jadu.trackdown.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import io.jadu.trackdown.domain.model.CompanyListing
import retrofit2.http.Query

@Dao
interface CompanyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanyList(
        companyListingModel: List<CompanyListingModel>
    )

    @androidx.room.Query("DELETE FROM CompanyListingModel")
    suspend fun deleteAllCompanyList()

    @androidx.room.Query(
        """
    SELECT * FROM CompanyListingModel
    WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%'
        OR UPPER(:query) == symbol
        OR LOWER(symbol) LIKE '%' || LOWER(:query) || '%'
    """
    )
    suspend fun searchCompanyListing(query: String): List<CompanyListingModel>

}