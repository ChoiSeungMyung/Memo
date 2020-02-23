package com.programmers.android.apps.line.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.programmers.android.apps.line.DB_TABLE_MEMO
import java.util.*

@Entity(tableName = DB_TABLE_MEMO)
data class Memo(
    @ColumnInfo(name = "memoTitle") var memoTitle: String,
    @ColumnInfo(name = "memoDescription") var memoDescription: String,
    @ColumnInfo(name = "memoImages") var memoImages: List<String?> = listOf()
) {
    @PrimaryKey @ColumnInfo(name = "memoId")
    var memoId:String = UUID.randomUUID().toString()
}