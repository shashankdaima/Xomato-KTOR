package com.xomato

import com.xomato.data.DatabaseFactory
import com.xomato.data.dao.RestaurantDao
import com.xomato.data.dao.RestaurantDaoImpl
import com.xomato.data.models.Restaurant
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.xomato.plugins.*
import kotlinx.coroutines.runBlocking

fun main() {
    embeddedServer(
        Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

val dao: RestaurantDao
    get() = RestaurantDaoImpl().also {
        runBlocking {
            if (it.allRestuarants().isEmpty()) {
                it.addNewRestuarant(
                    Restaurant(
                        id = 1,
                        restaurantName = "Haldiram",
                        address = "Tere bhai ke ghar mai",
                        orderOnline = true,
                        bookTable = false,
                        ratingOutOf5 = 4.5f,
                        contactNumber = "Daddy Please"
                    )
                )
            }
        }
    }

fun Application.module() {
    DatabaseFactory.init()
    configureSerialization()
    configureMonitoring()
    configureHTTP()
    configureSecurity()
    configureRouting()
}
