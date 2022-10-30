package com.xomato

import com.xomato.data.DatabaseFactory
import com.xomato.data.dao.FoodItemDao
import com.xomato.data.dao.FoodItemDaoImpl
import com.xomato.data.dao.RestaurantDao
import com.xomato.data.dao.RestaurantDaoImpl
import com.xomato.data.models.FoodItem
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

val restaurantDao: RestaurantDao
    get() = RestaurantDaoImpl().also {
        runBlocking {
            if (it.allRestuarants().isEmpty()) {
                it.addNewRestuarant(
                    Restaurant(
                        id = 1,
                        restaurantName = "Haldiram",
                        address = "Delhi-6, New Delhi, India",
                        orderOnline = true,
                        bookTable = false,
                        ratingOutOf5 = 4.5f,
                        contactNumber = "+918888888888"
                    )
                )
            }
        }
    }

val foodItemDao: FoodItemDao
    get() = FoodItemDaoImpl().also {
        runBlocking {
            if (it.allFoodItems(1).isEmpty()) {
                it.addNewFoodItem(
                    FoodItem(
                        id = 1,
                        dishName = "Roti Sabzi",
                        restaurantId = 1,
                        course = "Main Course",
                        diet = "Vegetarian",
                        price = 100
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
