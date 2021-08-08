package com.tatam.thewheelycoolapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.tatam.thewheelycoolapp.data.model.WheelItem
import com.tatam.thewheelycoolapp.data.model.WheelItemRepository
import com.tatam.thewheelycoolapp.data.room.WheelItemsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application  ) : AndroidViewModel  (application) {

    val getWheelItems :  LiveData<List<WheelItem>>
    private var repository :  WheelItemRepository

    init {
        val wheelItemDao = WheelItemsDatabase.getDatabaseInstance(application).wheelItemDao()
        repository = WheelItemRepository(wheelItemDao)
        getWheelItems = repository.items
    }

    fun addWheelItem(item : WheelItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addItem(item)
        }
    }

    fun updateItem(wheelItem: WheelItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateItem(wheelItem)
        }
    }

    fun deleteItem(wheelItem: WheelItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(wheelItem)
        }
    }

    fun checkIfItemExists(itemName: String): Boolean {
        return repository.checkItemExists(itemName)
    }
}