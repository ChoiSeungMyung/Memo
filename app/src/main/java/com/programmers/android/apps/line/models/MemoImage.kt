package com.programmers.android.apps.line.models

/**
 * 메모의 이미지들을 저장하기 위한 data
 *
 * @property imageUrl : image의 uri || url
 * @property deletable : 현재 삭제 가능상황인지 판단
 */
data class MemoImage(
    val imageUrl: String,
    var deletable: Boolean = false
)