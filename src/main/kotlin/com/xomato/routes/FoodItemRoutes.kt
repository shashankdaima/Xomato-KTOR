package com.xomato.routes

import com.google.gson.Gson
import com.xomato.RestaurantList
import com.xomato.data.models.FoodItem
import com.xomato.data.models.PaginateWrapper
import com.xomato.foodItemDao
import com.xomato.restaurantDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.io.File


fun Route.foodItemRouting() {

    route("/restaurants/{restaurantId}/foodItem") {

        get {
            val page = call.request.queryParameters["page"]?.toInt() ?: 1
            val size = call.request.queryParameters["pageSize"]?.toInt() ?: 10
            val searchQuery = call.request.queryParameters["q"] ?: ""
            val min = call.request.queryParameters["min"]?.toInt()
            val max = call.request.queryParameters["max"]?.toInt()

            val restaurantId = Integer.parseInt(
                call.parameters["restaurantId"] ?: return@get call.respondText(
                    "Missing id", status = HttpStatusCode.BadRequest
                )
            )
            val response = if (searchQuery.isEmpty()) {
                foodItemDao.allFoodItems(
                    restaurantId = restaurantId,
                    pageSize = size,
                    page = page,
                    min = min,
                    max = max
                )
            } else {
                foodItemDao.allFoodItems(
                    restaurantId = restaurantId,
                    pageSize = size,
                    page = page,
                    search = searchQuery, min = min, max = max
                )
            }
            if (response.isNotEmpty()) {
                call.respond(PaginateWrapper(response, pageSize = response.size, page = page))
            } else {
                call.respondText("No FoodItems found", status = HttpStatusCode.NotFound)
            }
        }
    }
}

@Serializable
data class FoodItemList(
    val list: List<FoodItem>
)