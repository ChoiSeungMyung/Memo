package com.programmers.android.apps.line.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.programmers.android.apps.line.DB_TABLE_MEMO

@Entity(tableName = DB_TABLE_MEMO)
data class Memo(
    val memoTitle: String,
    val memoText: String,
    val memoImages: List<String?> = listOf()
) {
    @PrimaryKey(autoGenerate = true)
    var memoId: Int = 0
}