package com.xomato.data.models

import com.google.gson.annotations.SerializedName
import org.jetbrains.exposed.sql.*
import kotlinx.serialization.Serializable

@Serializable
data class FoodItem(
    val id: String,
    val dishName: String,
    @SerializedName("restaurantID")
    val restaurantId: String,
    val course: String,
    val diet: String,
    val price: String
)

