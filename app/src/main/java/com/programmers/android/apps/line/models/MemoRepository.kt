package com.programmers.android.apps.line.models

import com.programmers.android.apps.line.models.room.MemoDao

class MemoRepository internal constructor(private val dao: MemoDao) {
    fun getAllMemos() = dao.getAllMemos()
    fun getMemo(id: Int) = dao.getMemo(id)
    fun insertMemo(memo: Memo) = dao.insertMemo(memo)
    fun deleteMemo(vararg memo: Memo) = dao.deleteMemo(*memo)
    fun modifyMemo(memo: Memo) = dao.modifyMemo(memo)
    fun deleteAllMemo() = dao.deleteAll()

    companion object {
        @Volatile
        private var instance: MemoRepository? = null

        fun getInstance(dao: MemoDao) =
            instance ?: synchronized(this) {
                instance ?: MemoRepository(dao).also { instance = it }
            }
    }
}