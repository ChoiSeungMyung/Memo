package com.programmers.android.apps.line.models

import androidx.lifecycle.MutableLiveData

/**
 * MutableLiveData<>()를 ArrayList처럼 쓰기 위해 만든 클래스
 *
 * 각 메소드마다 value 변경후 set
 */
class ArrayListLiveData<T>() : MutableLiveData<ArrayList<T>>() {
    init {
        value = ArrayList()
    }

    fun add(item: T) {
        val items = value
        items?.add(item)
        value = items
    }

    fun replaceAll(list: List<T>) {
        val items = value
        items?.clear()
        items?.addAll(list)
        value = items
    }

    fun removeAt(index: Int) {
        val items = value
        items?.removeAt(index)
        value = items
    }
}