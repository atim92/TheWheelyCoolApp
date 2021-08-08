package com.tatam.thewheelycoolapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wheel_items_table")
data class WheelItem(
    @PrimaryKey(autoGenerate = true )
    val id: Int,
    val name: String
)