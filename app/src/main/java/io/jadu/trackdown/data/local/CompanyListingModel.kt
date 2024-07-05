package io.jadu.trackdown.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CompanyListingModel (
    val name:String,
    val symbol:String,
    val exchange:String,
    @PrimaryKey val id:Int? = null
)