package com.xomato.data.dao

import com.xomato.data.DatabaseFactory.dbQuery
import com.xomato.data.models.FoodItem
import com.xomato.data.models.FoodItems
import com.xomato.data.models.Restaurant
import com.xomato.data.models.Restaurants
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

interface FoodItemDao {
    suspend fun allFoodItems(restaurantId: Int, page: Int, pageSize: Int): List<FoodItem>
    suspend fun getFoodItemById(id: Int): FoodItem?
    suspend fun addNewFoodItem(foodItem: FoodItem): Boolean
    suspend fun updateFoodItem(id: Int, foodItem: FoodItem): Boolean
    suspend fun deleteFoodItem(id: Int): Boolean
}

class FoodItemDaoImpl : FoodItemDao {
    private fun resultRowToFoodItem(row: ResultRow) = FoodItem(
        id = row[FoodItems.id],
        dishName = row[FoodItems.dishName],
        restaurantId = row[FoodItems.restaurantId],
        course = row[FoodItems.course],
        diet = row[FoodItems.diet],
        price = row[FoodItems.price]
    )

    override suspend fun allFoodItems(restaurantId: Int, page: Int, pageSize: Int): List<FoodItem> = dbQuery {
        FoodItems.select { FoodItems.restaurantId eq restaurantId }
            .limit(pageSize, offset = ((page - 1) * pageSize).toLong()).map(this::resultRowToFoodItem)
    }

    override suspend fun getFoodItemById(id: Int): FoodItem? = dbQuery {
        FoodItems.select { FoodItems.id eq id }.map(this::resultRowToFoodItem).singleOrNull()
    }

    override suspend fun addNewFoodItem(foodItem: FoodItem): Boolean = dbQuery {
        val insertStatement = FoodItems.insert {
            it[FoodItems.id] = foodItem.id
            it[FoodItems.course] = foodItem.course
            it[FoodItems.diet] = foodItem.diet
            it[FoodItems.price] = foodItem.price
            it[FoodItems.dishName] = foodItem.dishName
            it[FoodItems.restaurantId] = foodItem.restaurantId
        }
        !insertStatement.resultedValues.isNullOrEmpty()
    }

    override suspend fun updateFoodItem(id: Int, foodItem: FoodItem): Boolean = dbQuery {
        FoodItems.update {
            it[FoodItems.id] = foodItem.id
            it[FoodItems.course] = foodItem.course
            it[FoodItems.diet] = foodItem.diet
            it[FoodItems.price] = foodItem.price
            it[FoodItems.dishName] = foodItem.dishName
            it[FoodItems.restaurantId] = foodItem.restaurantId
        } > 0
    }


    override suspend fun deleteFoodItem(id: Int): Boolean = dbQuery {
        FoodItems.deleteWhere { FoodItems.id eq id } > 0
    }

}