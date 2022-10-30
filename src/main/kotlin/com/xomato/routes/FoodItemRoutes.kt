package com.xomato.routes

import com.xomato.foodItemDao
import com.xomato.restaurantDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.foodItemRouting() {
    route("/restaurants/{restaurantId}/foodItem") {

        get {
            val restaurantId = Integer.parseInt(
                call.parameters["restaurantId"] ?: return@get call.respondText(
                    "Missing id", status = HttpStatusCode.BadRequest
                )
            )
            val response = foodItemDao.allFoodItems(restaurantId)
            if (response.isNotEmpty()) {
                call.respond(response)
            } else {
                call.respondText("No FoodItems found", status = HttpStatusCode.OK)
            }
        }
    }
}