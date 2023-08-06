package com.example.powerhouseassignment.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.powerhouseassignment.ForeCast

@Database(entities = [ForeCast::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class WeatherDatabase : RoomDatabase(){

  abstract fun weatherListDao(): WeatherDao

  companion object {
    @Volatile
    private var instance: WeatherDatabase? = null
    private val LOCK = Any()

    operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
      instance ?: createDatabase(context).also { instance = it }
    }

    private fun createDatabase(context: Context) =
      Room.databaseBuilder(
        context.applicationContext,
        WeatherDatabase::class.java,
        "forecast_db.db"
      ).build()
  }

}