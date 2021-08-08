package com.tatam.thewheelycoolapp.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tatam.thewheelycoolapp.data.model.WheelItem
import com.tatam.thewheelycoolapp.data.model.WheelItemDao

@Database(entities = [WheelItem::class], version = 1, exportSchema = false)
abstract class WheelItemsDatabase : RoomDatabase() {

    abstract fun wheelItemDao(): WheelItemDao

    companion object {
        @Volatile
        private var INSTANCE: WheelItemsDatabase? = null

        fun getDatabaseInstance(context: Context): WheelItemsDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WheelItemsDatabase::class.java,
                    "wheel_items_table"
                )
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}