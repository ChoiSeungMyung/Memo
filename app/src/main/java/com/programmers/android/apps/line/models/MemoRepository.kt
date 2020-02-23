package com.programmers.android.apps.line.models

import com.programmers.android.apps.line.models.room.MemoDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MemoRepository internal constructor(private val dao: MemoDao) {
    fun getAllMemos() = runBlocking(context = Dispatchers.Default){dao.getAllMemos()}
    fun getMemo(id: Int): Memo = runBlocking(context = Dispatchers.Default) { dao.getMemo(id) }
    fun insertMemo(memo: Memo) = CoroutineScope(Dispatchers.IO).launch { dao.insertMemo(memo) }
    fun deleteMemo(vararg memo: Memo) = CoroutineScope(Dispatchers.IO).launch { dao.deleteMemo(*memo) }
    fun modifyMemo(memo: Memo) = CoroutineScope(Dispatchers.IO).launch { dao.modifyMemo(memo) }
    fun deleteAllMemo() = CoroutineScope(Dispatchers.IO).launch { dao.deleteAll() }

    companion object {
        @Volatile
        private var instance: MemoRepository? = null

        fun getInstance(dao: MemoDao) =
            instance ?: synchronized(this) {
                instance ?: MemoRepository(dao).also { instance = it }
            }
    }
}