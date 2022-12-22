package com.xomato

import com.opencsv.CSVReader
import com.xomato.data.DatabaseFactory
import com.xomato.data.dao.FoodItemDao
import com.xomato.data.dao.FoodItemDaoImpl
import com.xomato.data.dao.RestaurantDao
import com.xomato.data.dao.RestaurantDaoImpl
import com.xomato.data.models.FoodItem
import com.xomato.data.models.Restaurant
import com.xomato.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileReader
import java.nio.charset.StandardCharsets
import java.util.*

fun main() {
    embeddedServer(
        Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

//src/main/kotlin/com/xomato/Restaurantdatashashank.csv
//id,restaurantName,contactNumber,address,ratingOutOf5,restType,orderOnline,bookTable,locality,votes
fun readLocalCSV() {

}

val restaurantDao: RestaurantDao
    get() = RestaurantDaoImpl().also { dao ->
        runBlocking {
            if (dao.allRestuarants(1, 5).isEmpty()) {
                val fileName = "src/main/kotlin/com/xomato/Restaurantdatashashank.csv"
                val fr = FileReader(fileName, StandardCharsets.UTF_8)

                fr.use {
                    val reader = CSVReader(fr)
                    var restaurant: Restaurant
                    var flag = false;
                    reader.use { r ->
                        var line = r.readNext()
                        while (line != null) {
                            if (!flag) {
                                flag = true;
                            } else {
                                restaurant = Restaurant(
                                    id = Integer.parseInt(line[0]),
                                    restaurantName = line[1],
                                    contactNumber = line[2],
                                    address = line[3],
                                    ratingOutOf5 = line[4],
                                    orderOnline = line[6] == "Yes",
                                    bookTable = line[7] == "Yes",
                                    votes = line[9].toInt(),
                                    locality = line[8]
                                )
                                dao.addNewRestuarant(restaurant)
                            }
                            line = r.readNext()

                        }
                    }
                }
            }

        }
    }

val foodItemDao: FoodItemDao
    get() = FoodItemDaoImpl().also { dao ->
        runBlocking {
            val fileName = "src/main/kotlin/com/xomato/Shashankfinalitemsdatafile.csv"
            val fr = FileReader(fileName, StandardCharsets.UTF_8)
            var foodItem: FoodItem
            fr.use {
                val reader = CSVReader(fr)
                var flag = false;
                reader.use { r ->
                    var line = r.readNext()
                    while (line != null) {
                        if (!flag) {
                            flag = true;
                        } else {
//                                id,dishName,restaurantID,course,diet,price
                            foodItem = FoodItem(
                                id = (line[0]).toInt(),
                                dishName = line[1],
                                restaurantId = (line[2]).toInt(),
                                course = line[3],
                                diet = line[4],
                                price = line[5].toInt()
                            )
                            dao.addNewFoodItem(foodItem)
                        }
                        line = r.readNext()

                    }

                }
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
