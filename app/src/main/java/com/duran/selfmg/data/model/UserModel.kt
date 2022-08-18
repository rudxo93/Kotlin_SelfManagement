package com.duran.selfmg.data.model

import java.sql.Timestamp

data class UserModel(
    var userId: String? = null, // 로그인 유저 이메일
    var uid: String? = null, // 어떤 유저의 닉네임인지 구분
    var nickName: String? = null, // 유저 닉네임
    var timestamp: Long? = null, // 닉네임 설정 날짜
)
