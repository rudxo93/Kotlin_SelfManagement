# Kotlin_SelfManagement - 자기 관리 앱

TodoList와 메모, 캘린더에 한 줄로 간단한 일정 메모를 할 수 있는 자기관리앱입니다.(개발중)

>
<div align="center">
    <img src=./img/introImg.jpg width="200">
    <img src=./img/loginImg.jpg width="200">
    <img src=./img/todoListImg.jpg width="200">
</div>

# Develop Environment 개발 환경

<div align="center">
    <img src="https://img.shields.io/badge/Android-3DDC84?style=flat&logo=Android&logoColor=white"/>
    <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=flat&logo=Kotlin&logoColor=white"/>
    <img src="https://img.shields.io/badge/Firebase-FFCA28?style=flat&logo=Firebase&logoColor=white"/>
</div>

## 1. OS
 - Android

## 2. Language
 - Kotlin

## 3. DateBase
 - Firebase, RoomDB

## 4. Architecture Pattern
 - MVVM

## 5. Use Library
 - lottie(Loding page Animation)
 - Firebase(Authentication, Firestore Database)
 - Google Login(play-services-auth)
 - Coroutine(ViewModelScope)
 - JetPack(Data Binding, LiveData, Navigation, Room, ViewModel, Fragment)
 - ViewPager2
 - Material-CalendarView

# Intro 
<img src=./img/introImg.jpg width="300">

***

 - Lottie 라이브러리를 이용한 움직이는 로딩화면을 구현했습니다. 

# Login - Sign In

<div align="center">
    <img src=./img/loginImg.jpg width="200">
    <img src=./img/loginEmailEmpty.jpg width="200">
    <img src=./img/loginPasswordEmpty.jpg width="200">
    <img src=./img/noUserInfo.jpg width="200">
</div>

***

 - 이메일을 입력하지 않고 로그인 버튼을 클릭하게되면 메세지를 띄워줍니다.
 - 비밀번호을 입력하지 않고 로그인 버튼을 클릭하게되면 메세지를 띄워줍니다.
 - 이메일과 비밀번호를 입력하고 로그인 버튼을 클릭했을때 유저정보가 존재하지 않다면 메세지를 띄워줍니다.
 - 구글로그인 버튼을 클릭하게되면 구글로그인을 진행합니다.(Firebase)
 - 회원가입 버튼을 클릭하게 되면 회원가입 화면으로 이동합니다.
 - 아이디 찾기와 비밀번호 찾기(개발예정)

### Login - Sign In(Success)

<div align="center">
    <img src=./img/signInSuccess.jpg width="200">
    <img src=./img/signInMoveMain.jpg width="200">
</div>

***

 - 로그인 시 닉네임이 존재하는 이메일이라면 성공적으로 로그인이 진행되고 Main으로 이동합니다.

### Login - Sign In(CreateNickname)

<div align="center">
    <img src=./img/signInSuccessMoveNickname.jpg width="200">
    <img src=./img/signInNicknameView.jpg width="200">
</div>

***

 - 로그인 시 닉네임이 존재하지 않다면 닉네임 생성 View로 이동합니다. 

# Login - Sign Up

<div align="center">
    <img src=./img/signUp.jpg width="200">
</div>

***

 - 회원가입 화면입니다.
 - 이메일 중복확인과 비밀번호가 일치한지 확인 후 회원가입이 진행됩니다.

### Login - Sign Up(Email)

<div align="center">
    <img src=./img/signUpEmailError.jpg width="200">
    <img src=./img/signUpEmailfail.jpg width="200">
    <img src=./img/signUpEmailSuccess.jpg width="200">
</div>

***

 - 회원가입에 사용할 이메일을 작성합니다.
 - 잘못된 이메일 형식이라면 경고창과 함께 중복확인이 진행되지 않습니다.
 - 정상적인 이메일 입력 후 중복확인을 진행시 이미 존재하는 이메일이라면 경고창과 함께 지워줍니다.
 - 존재하지 않는 정상적인 이메일이 입력되었다면 중복확인 완료 알림창과 이메일을 도중에 변경할 수 없도록 readOnly

### Login - Sign Up(Password)

<div align="center">
    <img src=./img/signUpPasswordEmpty.jpg width="200">
    <img src=./img/signUpPasswordPatternError.jpg width="200">
    <img src=./img/signUpPasswordFail.jpg width="200">
    <img src=./img/signUpPasswordSuccess.jpg width="200">
</div>

***

 - 비밀번호 입력을 하지 않고 일치확인 클릭 시 비어있다는 경고창을 보여줍니다.
 - 비밀번호 정규식(영문, 특수문자포함 8~20자)을 검사하고 정규식이 틀렸다면 경고창을 보여줍니다.
 - 정규식을 통과했지만 비밀번호와 비밀번호 입력이 다르게 입력되었다면 불일치 경고창을 보여줍니다.
 - 정규식을 통과하고 사용가능한 비밀번호라면 확인창과 함께 비밀번호를 변경할 수 없도록 readOnly

### Login - Sign Up(Create Nickname)

<div align="center">
    <img src=./img/signUpCreateNicknameView.jpg width="200">
    <img src=./img/signUpCreateNicknameEmpty.jpg width="200">
    <img src=./img/signUpCreateNicknameFail.jpg width="200">
    <img src=./img/signUpCreateNicknameSuccess.jpg width="200">
</div>

***

 - 이메일과 비밀번호가 정상적으로 입력 후 다음으로 닉네임 생성View로 이동합니다.
 - 닉네임이 입력되지 않고 중복확인 또는 확인버튼을 누르게되면 경고창을 보여줍니다.
 - 이미 존재하는 닉네임을 입력 후 중복확인을 하게되면 경고창을 보여줍니다.
 - 중복되지 않는 닉네임을 입력 후 중복확인을 하게되면 사용가능한 닉네임 경고창을 보여줍니다.

# Main

<div align="center">
    <img src=./img/mainCurrentView.jpg width="200">
    <img src=./img/mainHealthView.jpg width="200">
    <img src=./img/mainPlanView.jpg width="200">
    <img src=./img/mainSettingView.jpg width="200">
</div>

***

 - 로그인이 성공적으로 진행되었다면 가장먼저 현재상태를 보여주는 View를 먼져 보여줍니다.
 - 현재상태에서는 현재 나의 건강상태와 계획의 진행정도를 보여줄 수 있도록 합니다. (개발 예정)
 - 자기관리에서는 나의 건강에 관련된 컨텐츠가 들어있습니다. (개발 중)
 - 계획에서는 계획을 세울 수 있는 컨텐츠가 들어있습니다.
 - 더보기에서는 닉네임변경과 탈퇴하기 등 기타 세팅이 들어있습니다. (개발 예정)

# Plan

<div align="center">
    <img src=./img/planFragmentLayout.JPG width="200">
    <img src=./img/planFragmentViewpagerAdapter.JPG width="200">
</div>

***

 - Plan에서는 TabLayout과 ViewPager2를 사용해서 레이아웃을 구성했습니다.
 - Adapter를 사용해서 TabLayout에서 Title 클릭 시 Title에 맞는 Fragment로 이동하도록 구현했습니다.

## Plan - TodoList

<div align="center">
    <img src=./img/todoListMain.jpg width="200">
</div>

***

 - 작성한 todoList들을 RecyclerView를 사용해서 보여줍니다.
 - 완료한 todo들은 체크박스를 클릭합니다.
 - 우측 하단의 add 버튼을 클릭하게되면 todo를 추가할 수 있습니다.

#### TodoList(Add)

<div align="center">
    <img src=./img/todoListAddBtnClick.jpg width="200">
    <img src=./img/todoListEmptyContent.jpg width="200">
    <img src=./img/todoListContent.jpg width="200">
    <img src=./img/todoListSaveContent.jpg width="200">
</div>

***

 - todo를 작성할 수 있는 dialog입니다.
 - content를 입력하지 않고 저장하기를 클릭하게되면 경고창을 보여줍니다.
 - content를 입력 후 저장하기를 누르게되면 Todo가 List에 추가되게 됩니다.

#### TodoList(Update)

<div align="center">
    <img src=./img/todoListContentClick.jpg width="200">
    <img src=./img/todoListContentEmpty.jpg width="200">
    <img src=./img/todoListContentChange.jpg width="200">
    <img src=./img/todoListContentUpdate.jpg width="200">
</div>

***

 - todoList의 content를 클릭하게되면 해당 todo를 수정할 수 있는 dialog창을 보여줍니다.
 - 마찬가지로 content가 비었다면 경고창을 보여줍니다.
 - content를 변경 후 수정하기 버튼을 클릭하게되면 content가 update되어 todoList에 보여줍니다.

#### TodoList(Delete)

<div align="center">
    <img src=./img/todoListContentDeleteSelect.jpg width="200">
    <img src=./img/todoListContentDeleteOne.jpg width="200">
    <img src=./img/todoListContentDeleteSelectAll.jpg width="200">
    <img src=./img/todoListContentDeleteEmpty.jpg width="200">
</div>

***

 - 완료된 todo들은 체크박스를 클릭해서 선택해줍니다.
 - 각 todo들의 오른쪽에 delete 아이콘이 있는데 클릭하게되면 해당 todo를 삭제해줍니다.
 - 다수의 todo들을 선택 후 우측 상단의 선택항목 삭제 버튼을 클릭하게되면 체크되어 있는 todo들을 한번에 삭제합니다.
 - 만약 체크되어있는 todo가 없다면 선택항목 삭제를 눌러도 삭제가 진행되지 않고 Toast메세지로 알려줍니다.

## Plan - Memo

<div align="center">
    <img src=./img/memoMain.jpg width="200">
</div>

***

 - 메모를 할 수 있는 공간입니다.
 - 삭제하고자 하는 메모들은 각 메모의 체크박스를 클릭합니다.
 - 우측 하단의 add 버튼을 클릭하게 되면 memo를 추가할 수 있습니다.

#### Memo(Add)

<div align="center">
    <img src=./img/addMemoVIew.jpg width="200">
    <img src=./img/addMemoEmptyContent.jpg width="200">
    <img src=./img/addMemoNoTItle.jpg width="200">
    <img src=./img/addMemoTitle.jpg width="200">
    <img src=./img/addMemoSaveSuccess.jpg width="200">
</div>

***

 - 메모를 작성할 수 있는 공간입니다.
 - 메모의 content를 작성하지 않고 저장을 클릭하게되면 Toast메세지와 함께 저장되지 않습니다.
 - 메모는 Title없이 저장할 수 있습니다(단, Content는 필수)
 - Title을 입력 후 저장하게 되면 메모 제목에 입력한 Title이 나오고 Title을 입력하지 않고 저장했다면 
   텍스트노트+저장날짜 가 임의의 Title이 되어 저장되고 보여지게 됩니다.
 - 메모를 길게 작성 후 저장하게 되어도 메모 목록을 보여주는 view에서는 6줄까지 보여주게 되고 그 이상 길이는 ... 으로 보여줍니다.

#### Memo(Update)

<div align="center">
    <img src=./img/addMemoUpdateView.jpg width="200">
    <img src=./img/addMemoUpdateContentEmpty.jpg width="200">
    <img src=./img/addMemoContentChange.jpg width="200">
    <img src=./img/addMemoUpdateResult.jpg width="200">
</div>

***

 - 메모의 Content를 클릭하게 되면 메모를 수정할 수 있는 View로 이동합니다.
 - 마찬가지로 Content가 비어있다면 변경되지 않습니다.
 - Title과 Content를 변경 후 저장하게되면 변경된 Memo가 List에 보여지게 됩니다.

#### Memo(Delete)

<div align="center">
    <img src=./img/memoDeleteSelect.jpg width="200">
    <img src=./img/memoDeleteOne.jpg width="200">
</div>

***

 - 삭제하고자 하는 메모가 있다면 체크박스클 클릭합니다.
 - 체크박스 클릭 시 delete 아이콘이 생성되고 아이콘을 클릭하게 되면 해당 Memo가 삭제됩니다.

## Plan - Calendar

<div align="center">
    <img src=./img/CalendarMain.jpg width="200">
</div>

***

 - 간단하게 해당 날짜에 한줄 일정을 추가할 수 있는 공간입니다.
 - 토요일은 파란색 일요일은 빨간색으로 구분해주었고 당일 날짜에는 보라색 원으로 표현했습니다.

#### Calendar(Add)

<div align="center">
   <img src=./img/CalendarSelectDate.jpg width="200">
   <img src=./img/CalendarSelectDateContentEmpty.jpg width="200">
   <img src=./img/CalendarSelectDateContent.jpg width="200">
   <img src=./img/CalendarSelectDateContentSave.jpg width="200">
</div>

***

 - 달력에서 간단한 일정을 추가하고자 하는 날짜를 선택하게 되면 일정을 추가할 수 있도록 입력창이 생깁니다.
 - content가 비었다면 Toast 메세지와 함께 저장되지 않습니다.
 - content 작성 후 저장하게 되면 해당 날짜에 content가 생성되고 update와 delete할 수 있는 버튼이 생성됩니다.

#### Calendar(Update)

<div align="center">
   <img src=./img/CalendarSelectDate1.jpg width="200">
   <img src=./img/CalendarSelectDateUpdate.jpg width="200">
   <img src=./img/CalendarSelectDateUpdateContent.jpg width="200">
   <img src=./img/CalendarSelectDateUpdateContentResult.jpg width="200">
</div>

***

 - 선택한 날짜에 스케줄이 있다면 변경이 가능합니다.
 - 수정하기 버튼을 클릭하게되면 content를 변경할 수 있는 View로 변경이 되고 Content 변경 후 저장하기를
   클릭하게 되면 변경된 스케줄이 해당 날짜에 보여지게 됩니다.

#### Calendar(Delete)

<div align="center">
   <img src=./img/CalendarDeleteSelect.jpg width="200">
   <img src=./img/CalendarDeleteSelectSuccess.jpg width="200">
</div>

***

 - 선택한 날짜의 스케줄을 삭제할 수 있습니다.
 - 원하는 날짜를 선택 후 스케줄이 존재한다면 삭제하기 버튼을 통해 삭제가 되고 다시 스케줄을 작성할 수 있는
   입력창으로 바뀝니다.