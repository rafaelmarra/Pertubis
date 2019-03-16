package com.example.rafaelmarra.pertubis.model.disturb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Disturb::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
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