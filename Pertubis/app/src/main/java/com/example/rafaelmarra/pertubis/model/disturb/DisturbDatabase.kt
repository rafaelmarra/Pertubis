package com.example.rafaelmarra.pertubis.model.disturb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Disturb::class], version = 2, exportSchema = false)
abstract class DisturbDatabase: RoomDatabase() {

    abstract fun disturbDao(): RoomDaoDisturb

    companion object {
        private var INSTANCE: DisturbDatabase? = null

        fun getInstance(context: Context): DisturbDatabase? {
            if (INSTANCE == null) {
                synchronized(DisturbDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        DisturbDatabase::class.java, "disturbs.db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}