package com.xomato.routes

import com.xomato.restaurantDao
import com.xomato.data.models.Restaurant
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.restaurantRouting() {
    route("/restaurants") {
        get {
            val response = restaurantDao.allRestuarants()
            if (response.isNotEmpty()) {
                call.respond(response)
            } else {
                call.respondText("No restaurants found", status = HttpStatusCode.OK)
            }
        }
        get("{id?}") {
            val id = Integer.parseInt(
                call.parameters["id"] ?: return@get call.respondText(
                    "Missing id", status = HttpStatusCode.BadRequest
                )
            )
            val restuarant = restaurantDao.getRestuarant(id) ?: return@get call.respondText(
                "No restuarant found",
                status = HttpStatusCode.NotFound
            )
            call.respond(restuarant)
        }
        post {
            val restaurant = call.receive<Restaurant>()
            if (restaurantDao.addNewRestuarant(restaurant)) {
                call.respondText("Restaurant added correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Restuarant Not Found", status = HttpStatusCode.NotFound)
            }
        }
        delete("{id?}") {
            val id = Integer.parseInt(
                call.parameters["id"] ?: return@delete call.respondText(
                    "Missing id", status = HttpStatusCode.BadRequest
                )
            )
            if (restaurantDao.deleteRestuarant(id)) {
                call.respondText("Restaurant removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
        put("{id?}") {
            val restaurant = call.receive<Restaurant>()
            val id = Integer.parseInt(
                call.parameters["id"] ?: return@put call.respondText(
                    "Missing id", status = HttpStatusCode.BadRequest
                )
            )
            if (restaurantDao.editRestuarant(id, restaurant)) {
                call.respondText("Restaurant update correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Restuarant Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }
}