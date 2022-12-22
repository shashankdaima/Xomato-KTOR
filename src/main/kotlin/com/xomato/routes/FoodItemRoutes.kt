package com.xomato.routes

import com.xomato.data.models.PaginateWrapper
import com.xomato.foodItemDao
import com.xomato.restaurantDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.foodItemRouting() {
    route("/restaurants/{restaurantId}/foodItem") {

        get {
            val page = call.request.queryParameters["page"]?.toInt() ?: 1
            val size = call.request.queryParameters["pageSize"]?.toInt() ?: 10
            val restaurantId = Integer.parseInt(
                call.parameters["restaurantId"] ?: return@get call.respondText(
                    "Missing id", status = HttpStatusCode.BadRequest
                )
            )
            val response = foodItemDao.allFoodItems(restaurantId,page,size)
            if (response.isNotEmpty()) {
                call.respond(PaginateWrapper(response, pageSize = response.size, pageNo = page))
            } else {
                call.respondText("No FoodItems found", status = HttpStatusCode.NotFound)
            }
        }
    }
}