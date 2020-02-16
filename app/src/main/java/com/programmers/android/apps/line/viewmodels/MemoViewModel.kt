package com.programmers.android.apps.line.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.programmers.android.apps.line.models.Memo
import com.programmers.android.apps.line.models.MemoRepository
import com.programmers.android.apps.line.models.room.MemoDatabase

class MemoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MemoRepository
    val allMemos: LiveData<List<Memo>>

    init {
        val memoDao = MemoDatabase.getInstance(application).memoDao()
        repository = MemoRepository.getInstance(memoDao)
        allMemos = repository.getAllMemos()
    }

    fun insertMemo(memo: Memo) = repository.insertMemo(memo)
    fun getMemo(id: Int) = repository.getMemo(id)
    fun deleteMemo(vararg memo: Memo) = repository.deleteMemo(*memo)
    fun modifyMemo(memo: Memo) = repository.modifyMemo(memo)
    fun deleteAllMemo() = repository.deleteAllMemo()
}