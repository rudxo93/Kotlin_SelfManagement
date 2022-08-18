package com.duran.selfmg.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.duran.selfmg.R
import com.duran.selfmg.data.model.UserModel
import com.duran.selfmg.databinding.ActivityCreateNameBinding
import com.google.common.base.MoreObjects
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateNameActivity : AppCompatActivity() {

    lateinit var binding: ActivityCreateNameBinding
    // Firebase DB
    lateinit var firestore: FirebaseFirestore
    // Firebase Auth
    lateinit var auth: FirebaseAuth

    lateinit var dUid: String
   /* lateinit var currentUid: String*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_name)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        // LoginActivity에서 로그인했을때 uid값이 dUid에 담겨서 전달됨 -> uid값을 받는다
        dUid = intent.getStringExtra("dUid")!!
        /*// 현재 로그인된 사용자의 uid정보
        currentUid = auth.currentUser!!.uid*/

        // 닉네임 입력
        initCreateName()
        // 닉네임 중복 확인 버튼
        initNickNameCheck()
        // 닉네임 작성 완료
        initBtnClickSuccess()
    }

    // =======================================닉네임 입력=======================================
    private fun initCreateName() {
        binding.edCreateName.addTextChangedListener(object: TextWatcher {
            val nickname = binding.edCreateName.text

            // 입력하기 전
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            // 텍스트 변화가 있다면
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 우선 닉네임 입력이 비었는지?
                if(nickname.isEmpty()){ // 비어있다면?
                    // 닉네임을 입력해주세요 문구
                    binding.liCreateNameAlarm.visibility = View.VISIBLE
                    binding.ivCreateNameAlarm.setImageResource(R.drawable.ic_cancel)
                    binding.tvCreateNameAlarm.text = "닉네임을 입력해주세요"
                } else if(nickname.length == 6) {
                    binding.liCreateNameAlarm.visibility = View.VISIBLE
                    binding.ivCreateNameAlarm.setImageResource(R.drawable.ic_info)
                    binding.tvCreateNameAlarm.text = "최대 6글자입니다."
                }
            }

            // 입력이 끝났다면
            override fun afterTextChanged(s: Editable?) {

            }
        })
    }
    // =======================================닉네임 입력=======================================

    // =======================================닉네임 중복확인=======================================
    private fun initNickNameCheck() {
    }
    // =======================================닉네임 중복확인=======================================

    // =======================================닉네임 작성 성공 -> 확인버튼=======================================
    private fun initBtnClickSuccess(){
        binding.btnSuccessCreateName.setOnClickListener {
            var createNickname = UserModel()
            createNickname.userId = auth.currentUser?.email // 로그인 후 현재 유저의 이메일
            createNickname.uid = auth.currentUser?.uid // uid값
            createNickname.nickName = binding.edCreateName.text.toString()
            createNickname.timestamp = System.currentTimeMillis()

            // nickName 컬렉션 안에 UserModel을 set
            firestore.collection("user").document(dUid).set(createNickname)
        }

    }
    // =======================================닉네임 작성 성공 -> 확인버튼=======================================
}