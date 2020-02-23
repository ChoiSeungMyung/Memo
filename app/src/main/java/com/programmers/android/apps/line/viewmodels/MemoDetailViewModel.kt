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
import com.programmers.android.apps.line.models.MemoRepository
import com.programmers.android.apps.line.models.room.MemoDatabase

class MemoDetailViewModel(application: Application) : AndroidViewModel(application) {
    private enum class Mode {
        WRITE, READ, MODIFY
    }

    private val repository: MemoRepository

    var hasInit: Boolean = false

    private var mode = Mode.WRITE
    private val isRead: Boolean
        get() = mode == Mode.READ

    val isReadObservable: ObservableBoolean = ObservableBoolean()

    var title: ObservableField<String> = ObservableField()
    var description: ObservableField<String> = ObservableField()
    var images: ArrayListLiveData<String?> = ArrayListLiveData()

    private var memo: Memo? = null

    val memoImagesAdapter = MemoImagesAdapter(getApplication(), this)

    init {
        val memoDao = MemoDatabase.getInstance(application).memoDao()
        repository = MemoRepository.getInstance(memoDao)
    }

    fun imageRemoveAt(position: Int) = images.removeAt(position)

    /**
     * setReadMode()를 통해 읽기모드로 전환
     * @param receivedId = 리스트액티비티에서 선택된 아이템의 Id
     *
     * hasInit = 화면 회전시 액티비티 라이프사이클에 memo의 내용이 초기화 되는걸 방지하기 위한 플래그
     * mode = 읽기 모드
     * images에 저장되어 있는 image들에게 수정 될 수 없음을 알림
     *
     * title, description, images - memo에 저장되어있는 데이터로 갱신시키기
     */
    fun setReadMode(receivedId: String) {
        hasInit = true
        memo = repository.getMemo(receivedId)
        mode = Mode.READ
        isReadObservable.set(isRead)
        title.set(memo?.memoTitle)
        description.set(memo?.memoDescription)
        images.value?.addAll(memo!!.memoImages)
    }

    /**
     * setModifyMode()를 통해 수정모드로 전환
     *
     * mode = 수정 모드
     * images에 저장되어있는 image들에게 수정 될수 있음을 알림
     */
    fun setModifyMode() {
        mode = Mode.MODIFY
        isReadObservable.set(isRead)
    }

    /**
     * MemoDetailActivity View에서 발생한 OK버튼 클릭이벤트 처리
     *
     */
    fun okAction(context: Context) {
        when (mode) {
            // 작성 모드 - 새로운 Memo 객체 생성후 Room에 넣어주기 : NPE 체크
            Mode.WRITE -> {
                val memo = Memo(
                    title.get() ?: "",
                    description.get() ?: "",
                    images.value?.toList() ?: emptyList()
                )
                if (!memo.memoTitle.isBlank() || !memo.memoDescription.isBlank() || memo.memoImages.isNotEmpty())
                    repository.insertMemo(memo)
            }
            // 수정 모드 - 불러온 Memo의 내용을 바꾼후 repository를 통해 Room에게 업데이트를 알림
            Mode.MODIFY -> {
                memo?.apply {
                    memoTitle = title.get() ?: ""
                    memoDescription = description.get() ?: ""
                    memoImages = images.value!!.toList()
                }
                repository.modifyMemo(memo!!)
            }
            else -> {}
        }
        (context as Activity).finish()
    }

    fun deleteMemo() = memo?.let { repository.deleteMemo(it) }
    fun cancelAction(context: Context) = (context as Activity).finish()
}