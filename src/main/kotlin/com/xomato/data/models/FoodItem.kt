package com.xomato.data.models

import org.jetbrains.exposed.sql.*
import kotlinx.serialization.Serializable

@Serializable
data class FoodItem(
    val id: Int,
    val dishName: String,
    val restaurantId: Int,
    val course: String,
    val diet: String,
    val price: Int
)

object FoodItems : Table() {
    val id = integer("id").autoIncrement()
    val dishName = varchar("dishName", 2000)
    val restaurantId = integer("restaurantId")
    val course = varchar("course", 2000)
    val diet = varchar("diet", 2000)
    val price = integer("price")
    override val primaryKey = PrimaryKey(Restaurants.id)
}