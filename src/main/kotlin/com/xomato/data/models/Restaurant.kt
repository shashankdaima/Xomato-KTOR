package com.xomato.data.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*

@Serializable
data class Restaurant(
    val id: Int,
    val restaurantName: String,
    val address: String,
    val orderOnline: Boolean,
    val bookTable: Boolean,
    val ratingOutOf5: Float = Float.NaN,
    val votes: Int = 0,
    val contactNumber: String?,
    val locality: String? = null
)

object Restaurants : Table() {
    val id = integer("id").autoIncrement()
    val restaurantName = varchar("restaurantName", 128)
    val address = varchar("address", 1024)
    val orderOnline = bool("orderOnline")
    val bookTable = bool("bookTable")
    val ratingOutOf5 = float("ratingOutOf5")
    val votes = integer("votes")
    val contactNumber = varchar("contactNumber", 32).nullable()
    val locality = varchar("locality", 1024).nullable()

    override val primaryKey = PrimaryKey(id)
}
