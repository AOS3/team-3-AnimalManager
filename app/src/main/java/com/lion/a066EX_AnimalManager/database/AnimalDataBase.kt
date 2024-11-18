package com.lion.a066EX_AnimalManager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AnimalModel::class], version = 1, exportSchema = true)
abstract class AnimalDataBase : RoomDatabase() {

    abstract fun animalDataDao(): DataDaoAnimal

    companion object {
        var animalDatabase: AnimalDataBase? = null

        @Synchronized
        fun getInstance(context: Context): AnimalDataBase? {

            synchronized(AnimalDataBase::class) {
                animalDatabase = Room.databaseBuilder(
                    context.applicationContext, AnimalDataBase::class.java,
                    "Animal.db"
                ).build()
            }
            return animalDatabase
        }
        
    }
}

