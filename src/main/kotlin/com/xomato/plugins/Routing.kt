package com.xomato.plugins

import com.xomato.routes.foodItemRouting
import com.xomato.routes.restaurantRouting
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.application.*
import io.ktor.server.response.*

fun Application.configureRouting() {
    install(StatusPages) {
        exception<AuthenticationException> { call, cause ->
            call.respond(HttpStatusCode.Unauthorized)
        }
        exception<AuthorizationException> { call, cause ->
            call.respond(HttpStatusCode.Forbidden)
        }

    }
    routing {
        restaurantRouting()
        foodItemRouting()

    }
}


class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()
