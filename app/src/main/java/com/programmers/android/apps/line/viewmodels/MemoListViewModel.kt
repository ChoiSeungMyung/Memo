package com.programmers.android.apps.line.viewmodels

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.programmers.android.apps.line.models.Memo
import com.programmers.android.apps.line.models.MemoRepository
import com.programmers.android.apps.line.models.room.MemoDatabase

class MemoListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MemoRepository
    val allMemos: LiveData<List<Memo>>

    init {
        val memoDao = MemoDatabase.getInstance(application).memoDao()
        repository = MemoRepository.getInstance(memoDao)
        allMemos = repository.getAllMemos()
    }

    fun deleteAllMemo() = repository.deleteAllMemo()
    fun callActivity(intent: Intent) {
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(getApplication(), intent, null)
    }
}