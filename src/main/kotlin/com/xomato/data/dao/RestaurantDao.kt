package com.xomato.data.dao

import com.xomato.data.DatabaseFactory.dbQuery
import com.xomato.data.models.Restaurant
import com.xomato.data.models.Restaurants
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like

interface RestaurantDao {
    suspend fun allRestuarants(page: Int, pageSize: Int, search: String): List<Restaurant>

    suspend fun allRestuarants(page: Int, pageSize: Int): List<Restaurant>
    suspend fun getRestuarant(id: Int): Restaurant?
    suspend fun addNewRestuarant(restaurant: Restaurant): Boolean
    suspend fun editRestuarant(id: Int, restaurant: Restaurant): Boolean
    suspend fun deleteRestuarant(id: Int): Boolean
}

class RestaurantDaoImpl : RestaurantDao {
    private fun resultRowToRestuarant(row: ResultRow) = Restaurant(
        id = row[Restaurants.id],
        restaurantName = row[Restaurants.restaurantName],
        address = row[Restaurants.address],
        orderOnline = row[Restaurants.orderOnline],
        bookTable = row[Restaurants.bookTable],
        ratingOutOf5 = row[Restaurants.ratingOutOf5],
        votes = row[Restaurants.votes],
        contactNumber = row[Restaurants.contactNumber],
        locality = row[Restaurants.locality]
    )

    override suspend fun allRestuarants(page: Int, pageSize: Int, search: String): List<Restaurant> = dbQuery {
        Restaurants.select {
            Restaurants.restaurantName like "%$search%" or(Restaurants.address like "%$search%")
        }.limit(pageSize, offset = ((page - 1) * pageSize).toLong())
            .orderBy(Restaurants.restaurantName to SortOrder.ASC).map(this::resultRowToRestuarant)

    }

    override suspend fun allRestuarants(page: Int, pageSize: Int): List<Restaurant> = dbQuery {

        Restaurants.selectAll().limit(pageSize, offset = ((page - 1) * pageSize).toLong())
            .orderBy(Restaurants.restaurantName to SortOrder.ASC).map(this::resultRowToRestuarant)
    }

    override suspend fun getRestuarant(id: Int): Restaurant? = dbQuery {
        Restaurants.select { Restaurants.id eq id }.map(this::resultRowToRestuarant).singleOrNull()
    }

    override suspend fun addNewRestuarant(restaurant: Restaurant): Boolean = dbQuery {
        val insertStatement = Restaurants.insert {
            it[Restaurants.id] = restaurant.id
            it[Restaurants.restaurantName] = restaurant.restaurantName
            it[Restaurants.address] = restaurant.address
            it[Restaurants.orderOnline] = restaurant.orderOnline
            it[Restaurants.bookTable] = restaurant.bookTable
            it[Restaurants.ratingOutOf5] = restaurant.ratingOutOf5
            it[Restaurants.votes] = restaurant.votes
            it[Restaurants.contactNumber] = restaurant.contactNumber
            it[Restaurants.locality] = restaurant.locality
        }
        !insertStatement.resultedValues.isNullOrEmpty()
    }

    override suspend fun editRestuarant(id: Int, restaurant: Restaurant): Boolean = dbQuery {
        Restaurants.update {
            it[Restaurants.id] = restaurant.id
            it[Restaurants.restaurantName] = restaurant.restaurantName
            it[Restaurants.address] = restaurant.address
            it[Restaurants.orderOnline] = restaurant.orderOnline
            it[Restaurants.bookTable] = restaurant.bookTable
            it[Restaurants.ratingOutOf5] = restaurant.ratingOutOf5
            it[Restaurants.votes] = restaurant.votes
            it[Restaurants.contactNumber] = restaurant.contactNumber
            it[Restaurants.locality] = restaurant.locality
        } > 0
    }

    override suspend fun deleteRestuarant(id: Int): Boolean = dbQuery {
        Restaurants.deleteWhere { Restaurants.id eq id } > 0
    }

}

