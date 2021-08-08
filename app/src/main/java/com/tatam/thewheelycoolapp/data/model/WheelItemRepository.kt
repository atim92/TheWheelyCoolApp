package com.tatam.thewheelycoolapp.data.model

import androidx.lifecycle.LiveData

class WheelItemRepository (private val wheelItemDao: WheelItemDao) {

    val items : LiveData<List<WheelItem>> = wheelItemDao.getItems()

    suspend  fun addItem(item : WheelItem) {
        wheelItemDao.addWheelItem(item)
    }

    suspend fun updateItem(item: WheelItem) {
        wheelItemDao.updateItem(item)
    }

    suspend fun deleteItem(item: WheelItem) {
        wheelItemDao.deleteItem(item)
    }

    fun checkItemExists(item: String) : Boolean {
        return wheelItemDao.checkItemExists(item)
    }
}