package com.programmers.android.apps.line.viewmodels

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import com.programmers.android.apps.line.adapters.MemoImagesAdapter
import com.programmers.android.apps.line.models.ArrayListLiveData
import com.programmers.android.apps.line.models.Memo
import com.programmers.android.apps.line.models.MemoImage
import com.programmers.android.apps.line.models.MemoRepository
import com.programmers.android.apps.line.models.room.MemoDatabase

class MemoDetailViewModel(application: Application) : AndroidViewModel(application) {
    enum class Mode {
        WRITE, READ, MODIFY
    }

    private val repository: MemoRepository

    var hasInit: Boolean = false

    var mode = Mode.WRITE
    private val isRead: Boolean
        get() = mode == Mode.READ

    val isReadObservable: ObservableBoolean = ObservableBoolean()

    var title: ObservableField<String> = ObservableField()
    var description: ObservableField<String> = ObservableField()
    var images: ArrayListLiveData<MemoImage?> = ArrayListLiveData()

    var memo: Memo? = null

    val memoImagesAdapter = MemoImagesAdapter(getApplication(), this)

    init {
        val memoDao = MemoDatabase.getInstance(application).memoDao()
        repository = MemoRepository.getInstance(memoDao)
    }

    fun imageRemoveAt(position: Int) = images.removeAt(position)

    fun setReadMode(receivedId: Int) {
        hasInit = true
        memo = repository.getMemo(receivedId)
        mode = Mode.READ
        isReadObservable.set(isRead)
        title.set(memo?.memoTitle)
        description.set(memo?.memoDescription)
        images.replaceAll(memo!!.memoImages)
        images.value?.forEach { it?.deletable = isReadObservable.get() }
    }

    fun setModifyMode() {
        mode = Mode.MODIFY
        isReadObservable.set(isRead)
        images.value?.forEach { it?.deletable = isReadObservable.get() }
        memoImagesAdapter.notifyDataSetChanged()
    }

    fun okAction(context: Context) {
        when (mode) {
            Mode.WRITE -> {
                val memo = Memo(
                    title.get() ?: "",
                    description.get() ?: "",
                    images.value?.toList() ?: emptyList()
                )
                repository.insertMemo(memo)
            }
            Mode.MODIFY -> {
                memo?.apply {
                    memoTitle = title.get() ?: ""
                    memoDescription = description.get() ?: ""
                    memoImages = images.value!!.toList()
                }
                repository.modifyMemo(memo!!)
            }
            Mode.READ -> {

            }
        }
        (context as Activity).finish()
    }

    fun deleteMemo() = memo?.let { repository.deleteMemo(it) }
    fun cancelAction(context: Context) = (context as Activity).finish()
}