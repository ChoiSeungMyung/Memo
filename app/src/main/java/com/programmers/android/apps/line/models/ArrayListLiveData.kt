package com.programmers.android.apps.line.models

import androidx.lifecycle.MutableLiveData

class ArrayListLiveData<T>() : MutableLiveData<ArrayList<T>>() {
    init {
        value = ArrayList()
    }

    fun add(item: T) {
        val items = value
        items?.add(item)
        value = items
    }

    fun addAll(list: List<T>) {
        val items = value
        items?.addAll(list)
        value = items
    }

    fun replaceAll(list: List<T>) {
        val items = value
        items?.clear()
        items?.addAll(list)
        value = items
    }
    fun clear(notify: Boolean) {
        val items = value
        items?.clear()
        if (notify) value = items
    }

    fun remove(item: T) {
        val items = value
        items?.remove(item)
        value = items
    }

    fun removeAt(index: Int) {
        val items = value
        items?.removeAt(index)
        value = items
    }

    fun notifyChange() {
        val items = value
        value = items
    }
}