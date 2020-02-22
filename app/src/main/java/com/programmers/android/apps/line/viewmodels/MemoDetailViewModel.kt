package com.programmers.android.apps.line.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.programmers.android.apps.line.models.ArrayListLiveData
import com.programmers.android.apps.line.models.Memo
import com.programmers.android.apps.line.models.MemoImage

class MemoDetailViewModel(application: Application) : AndroidViewModel(application) {
    var title: String = ""
    var description: String = ""
    var images: ArrayListLiveData<MemoImage?> = ArrayListLiveData()

    fun setReadMode(memo: Memo) {

    }

    fun setModifyMode() {

    }
}