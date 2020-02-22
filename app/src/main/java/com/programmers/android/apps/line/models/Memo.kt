package com.programmers.android.apps.line.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.programmers.android.apps.line.DB_TABLE_MEMO

@Entity(tableName = DB_TABLE_MEMO)
data class Memo(
    var memoTitle: String,
    var memoDescription: String,
    var memoImages: List<MemoImage?> = listOf()
//    var memoImages: MutableLiveData<ArrayList<String?>>
) {
    @PrimaryKey(autoGenerate = true)
    var memoId: Int = 0
}