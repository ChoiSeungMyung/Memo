라인 신입 개발자 채용 - 앱 개발 챌린지  
==============================
Android - Kotlin
----------------

#### AAC - Room, LiveData, ViewModel, DataBinding, Lifecycles
#### Library - Glide: <https://github.com/bumptech/glide/>

## 목표
    1. MVVM 패턴을 적용해 테스트코드 작성을 용이하게 하고 뷰와 로직을 분리
    2. 상황에 맞게 적재적소에 AAC 사용   
    3. 정상 동작
    4. kotlin이 지향하는 철학적 사고 이해하기
    5. 확장성과 의존성을 고려한 코드 짜기
    6. 협업을 고려한 가독성과 문서화 등 상대방을 배려하는 프로그래머가 되기
## 패키지 구조
    model
	    ㄴroom
		ㄴ MemoDao.kt
		ㄴ MemoDataBase.kt
	    ㄴ Memo.kt
	    ㄴ MemoRepository.kt

    view
	    ㄴ MemoDetailActivity.kt
	    ㄴ MemoListActivity.kt

    viewmodel
	    ㄴ MemoDetailViewModel.kt
	    ㄴ MemoListViewModel.kt

## 기능
### 기능1: 메모리스트
    1. Room에 저장된 메모들을 읽어 들여와 이미지 리스트로 보여줍니다.    
      1-1. MemoListActivity가 시작되면 MemoListViewModel을 생성합니다.
      1-2. MemoListViewModel에서 MemoRepository를 이용해 Room에 저장된 메모들을 가져옵니다.
      1-3. MemoListActivity에서 MemoList를 관찰하며 데이터 변동이 일어나면 실시간으로 갱신합니다.
    2. FloatingActionButton을 클릭하면 새로운 메모 작성이 가능합니다.
    3. DataBinding을 통해 이미지의 썸네일, 제목, 글의 일부를 보여줍니다.
    4. 메모를 클릭하면 메모 상세보기 액티비티(MemoDetailActivity)로 이동하게 됩니다.
### 기능2: 메모 상세보기
    1. DataBinding으로 메모의 제목과 내용을 보여줍니다.
    2. intent에 receivedId 값에 따라 상세보기 인지 메모 작성인지 판단합니다.
    3. Toolbar를 이용해 수정, 삭제가 가능합니다.
    
### 기능3: 메모 편집 및 작성
    1. MemoDetailViewModel 클래스를 이용해 화면전환이 이루어 지더라도 데이터가 유지됩니다.
    2. 제목, 내용, 이미지가 모두 비어있다면 저장되지 않습니다.
    3. 이미지 추가 삭제시 MemoDetailViewModel을 통해 실시간으로 반영됩니다.
    
### 후기
      안드로이드 개발자로 일하게 된지 1년이 갓 넘었습니다. 1년간 많은 경험을 하게되었습니다.
      자바에서 코틀린으로 옮기게 되었고 출시 서비스를 개발, 유지, 보수 하며 겪게된 경험 등등
      어느 하나 제게 도움이 되지 않았던 것은 없었습니다.
      
      이번 과제를 통해 정말 많은걸 배우고  깨닫게 되었습니다. 이런 기회를 주셔서 정말 감사합니다.
