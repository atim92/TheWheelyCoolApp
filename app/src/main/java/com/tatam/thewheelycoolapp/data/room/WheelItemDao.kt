package com.tatam.thewheelycoolapp.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tatam.thewheelycoolapp.data.model.WheelItem

@Dao
interface WheelItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addWheelItem(item: WheelItem)

    @Update
    suspend fun updateItem(item : WheelItem)

    @Delete
    suspend fun deleteItem(item: WheelItem)

    @Query("SELECT * FROM wheel_items_table order by id ASC")
    fun getItems(): LiveData<List<WheelItem>>

    @Query("SELECT EXISTS (SELECT 1 FROM wheel_items_table WHERE name = :itemName)")
    fun checkItemExists(itemName: String): Boolean
}