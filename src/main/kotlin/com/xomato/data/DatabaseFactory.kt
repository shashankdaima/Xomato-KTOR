package com.xomato.data

import com.xomato.data.dao.RestaurantDao
import com.xomato.data.dao.RestaurantDaoImpl
import com.xomato.data.models.FoodItems
import com.xomato.data.models.Restaurants
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.*

object DatabaseFactory {

    fun init() {
        val driverClassName = "org.h2.Driver"
        val jdbcURL = "jdbc:h2:file:./build/db"
        val database = Database.connect(jdbcURL, driverClassName)
        transaction(database) {
            SchemaUtils.create(Restaurants)
            SchemaUtils.create(FoodItems)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) {
        block();
    }
}

