package com.programmers.android.apps.line.models

import com.programmers.android.apps.line.models.room.MemoDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * suspend 함수를 호출하기 위해 Coroutine, runBlocking적용
 *
 * runBlocking{} - 반환값을 받기 위해 runBlocking{}를 이용
 * Coroutine(Dispatchers.Default) - Job을 이용해 흐름제어 처리에 이용하기 위해 코루틴 사용
 */
class MemoRepository internal constructor(private val dao: MemoDao) {
    fun getAllMemos() = runBlocking(context = Dispatchers.Default){dao.getAllMemos()}
    fun getMemo(id: String): Memo = runBlocking(context = Dispatchers.Default) { dao.getMemo(id) }
    fun insertMemo(memo: Memo) = CoroutineScope(Dispatchers.Default).launch { dao.insertMemo(memo) }
    fun deleteMemo(vararg memo: Memo) = CoroutineScope(Dispatchers.Default).launch { dao.deleteMemo(*memo) }
    fun modifyMemo(memo: Memo) = CoroutineScope(Dispatchers.Default).launch { dao.modifyMemo(memo) }
    fun deleteAllMemo() = CoroutineScope(Dispatchers.Default).launch { dao.deleteAll() }

    companion object {
        @Volatile
        private var instance: MemoRepository? = null

        fun getInstance(dao: MemoDao) =
            instance ?: synchronized(this) {
                instance ?: MemoRepository(dao).also { instance = it }
            }
    }
}