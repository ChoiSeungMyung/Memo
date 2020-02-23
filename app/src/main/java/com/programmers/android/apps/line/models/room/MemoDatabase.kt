package com.programmers.android.apps.line.models.room

import android.content.Context
import androidx.room.*
import com.google.gson.Gson
import com.programmers.android.apps.line.DB_NAME
import com.programmers.android.apps.line.models.Memo
import com.programmers.android.apps.line.models.MemoImage

@Database(entities = [Memo::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MemoDatabase : RoomDatabase() {
    abstract fun memoDao(): MemoDao

    companion object {
        @Volatile
        private var instance: MemoDatabase? = null

        fun getInstance(context: Context): MemoDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDataBase(context).also { instance = it }
            }
        }

        private fun buildDataBase(context: Context): MemoDatabase {
            return Room.databaseBuilder(context, MemoDatabase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}

class Converters {
    @TypeConverter
    fun listToJson(value: List<MemoImage?>): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String): List<MemoImage?> {
        val objects = Gson().fromJson(value, Array<MemoImage?>::class.java)
        return objects.toList()
    }
}