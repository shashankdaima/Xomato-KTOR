package com.xomato

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.opencsv.CSVReader
import com.xomato.data.DatabaseFactory
import com.xomato.data.dao.FoodItemDao
import com.xomato.data.dao.FoodItemDaoImpl
import com.xomato.data.dao.RestaurantDao
import com.xomato.data.dao.RestaurantDaoImpl
import com.xomato.data.models.FoodItem
import com.xomato.data.models.Restaurant
import com.xomato.data.models.RestaurantString
import com.xomato.plugins.*
import com.xomato.routes.FoodItemList
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import java.io.File
import java.io.FileReader
import java.nio.charset.StandardCharsets
import java.util.*

fun main() {
    FoodItemData.init()
    embeddedServer(
        Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

object FoodItemData {
    var data = mutableListOf<FoodItem>()
    val gson = Gson();

    fun init() {
        val fileName = "src/main/kotlin/com/xomato/food_items.json"
        val file = File(fileName).readText()
        val finalList = gson.fromJson(file, FoodItemList::class.java)
        data.addAll(finalList.list)
    }
}

val restaurantDao: RestaurantDao
    get() = RestaurantDaoImpl().also { dao ->
        runBlocking {
//            if (dao.allRestuarants(1, 5).isEmpty()) {
//            val fileName = "src/main/kotlin/com/xomato/restaurants.json"
//            val file = File(fileName).readText()
////            println(file)
//            val gson = Gson();
//            val finalList = gson.fromJson(file, RestaurantList::class.java)
//            println(finalList.rest.size)
//            }
//            finalList.rest.forEach {
//                dao.addNewRestuarant(
//                    Restaurant(
//                        id = it.id.toInt(),
//                        restaurantName = it.restaurantName,
//                        address = it.address,
//                        orderOnline = it.orderOnline == "Yes",
//                        contactNumber = it.contactNumber,
//                        ratingOutOf5 = it.ratingOutOf5,
//                        bookTable = it.bookTable == "Yes",
//                        locality = it.locality,
//                        votes = it.votes.toInt()
//                    )
//                )
//            }

        }
    }


val foodItemDao: FoodItemDao
    get() = FoodItemDaoImpl().also { dao ->
        runBlocking {
//            FoodItemDao
//            val fileName = "src/main/kotlin/com/xomato/Shashankfinalitemsdatafile.csv"
//            val fr = FileReader(fileName, StandardCharsets.UTF_8)
//            var foodItem: FoodItem
//            fr.use {
//                val reader = CSVReader(fr)
//                var flag = false;
//                reader.use { r ->
//                    var line = r.readNext()
//                    while (line != null) {
//                        if (!flag) {
//                            flag = true;
//                        } else {
////                                id,dishName,restaurantID,course,diet,price
//                            foodItem = FoodItem(
//                                id = (line[0]).toInt(),
//                                dishName = line[1],
//                                restaurantId = (line[2]).toInt(),
//                                course = line[3],
//                                diet = line[4],
//                                price = line[5].toInt()
//                            )
//                            dao.addNewFoodItem(foodItem)
//                        }
//                        line = r.readNext()
//
//                    }
//
//                }
//            }
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

@Serializable
data class RestaurantList(
    var rest: List<RestaurantString>
)