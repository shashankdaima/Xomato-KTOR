package com.xomato.data.dao

import com.xomato.FoodItemData
import com.xomato.data.DatabaseFactory.dbQuery
import com.xomato.data.models.FoodItem
//import com.xomato.data.models.FoodItems
import org.jetbrains.exposed.sql.*


interface FoodItemDao {
    suspend fun allFoodItems(
        restaurantId: Int, page: Int, pageSize: Int, min: Int? = null, max: Int? = null
    ): List<FoodItem>

    suspend fun allFoodItems(
        restaurantId: Int, page: Int, pageSize: Int, search: String, min: Int? = null, max: Int? = null
    ): List<FoodItem>
//    suspend fun getFoodItemById(id: Int): FoodItem?
//    suspend fun addNewFoodItem(foodItem: FoodItem): Boolean
//    suspend fun updateFoodItem(id: Int, foodItem: FoodItem): Boolean
//    suspend fun deleteFoodItem(id: Int): Boolean
}

class FoodItemDaoImpl : FoodItemDao {


    override suspend fun allFoodItems(
        restaurantId: Int, page: Int, pageSize: Int, min: Int?, max: Int?
    ): List<FoodItem> {
        val fromIndex = (page - 1) * pageSize
        val unfilteredList = FoodItemData.data
        val list = unfilteredList.filter {
            it.restaurantId == restaurantId.toString() && if (min == null) {
                true
            } else (it.price.toInt() > min) && if (max == null) {
                true
            } else (it.price.toInt() < max)
        }
        println(list.size);
        return if (list.size <= fromIndex) {
            emptyList()
        } else list.subList(fromIndex, Math.min(fromIndex + pageSize, list.size))

    }

    override suspend fun allFoodItems(
        restaurantId: Int, page: Int, pageSize: Int, search: String, min: Int?, max: Int?
    ): List<FoodItem> {
        val fromIndex = (page - 1) * pageSize
        val unfilteredList = FoodItemData.data
        val list = unfilteredList.filter {
            it.restaurantId == restaurantId.toString() && it.dishName.toLowerCase()
                .contains(search.toLowerCase()) && if (min == null) {
                true
            } else (it.price.toInt() >= min) && if (max == null) {
                true
            } else (it.price.toInt() <= max)
        }

        println(list.size);
        return if (list.size <= fromIndex) {
            emptyList()
        } else list.subList(fromIndex, Math.min(fromIndex + pageSize, list.size))

    }


}