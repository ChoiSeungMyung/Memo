package com.programmers.android.apps.line.models

import androidx.lifecycle.LiveData
import com.programmers.android.apps.line.adapters.MemoImagesAdapter

class MemoImagesRepository internal constructor(private val memoImagesAdapter: MemoImagesAdapter) {
    fun getAllImages() = memoImagesAdapter.images
    fun insertImage(path: String?) = memoImagesAdapter.images.add(path)
    fun deleteImage(index: Int) = memoImagesAdapter.images.removeAt(index)
    fun getImage(index: Int) = memoImagesAdapter.images[index]

    companion object {
        @Volatile
        private var instance: MemoImagesRepository? = null

        fun getInstance(adapter: MemoImagesAdapter) =
            instance ?: synchronized(this) {
                instance ?: MemoImagesRepository(adapter).also { instance = it }
            }
    }
}