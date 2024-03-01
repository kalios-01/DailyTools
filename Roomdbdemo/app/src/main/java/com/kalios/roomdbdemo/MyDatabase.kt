package com.kalios.roomdbdemo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: MyDatabase? = null

        fun getDatabase(context: Context): MyDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = INSTANCE
                if (instance != null) {
                    return instance
                }

                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = newInstance
                return newInstance
            }
        }
    }
}