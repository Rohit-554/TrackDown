package io.jadu.trackdown.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CompanyListingModel::class], version = 1)
abstract  class Database:RoomDatabase() {
    abstract fun companyDao(): CompanyDao
}