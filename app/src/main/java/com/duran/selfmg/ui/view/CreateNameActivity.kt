package com.duran.selfmg.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
    lateinit var userNickname: String
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

        userNickname = binding.edCreateName.text.toString()

        // 닉네임 입력
        initCreateName()

        // 닉네임 생성 확인버튼 클릭 시 -> Firebase Database에 닉네임을 문서이름으로 user정보와 닉네임 같이 등록
        binding.btnSuccessCreateName.setOnClickListener {
            // 확인버튼 -> 닉네임이 입력이 되었는지 비었는지
            if(userNickname.isNotEmpty()){ // 닉네임 입력 창이 비어있지 않다면
                initBtnClickSuccess()
            } else { // 닉네임 입력 창이 비었다면
                initNicknameEmpty()
            }
        }
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
                    binding.ivCreateNameAlarm.visibility = View.VISIBLE
                    binding.tvCreateNameAlarm.visibility = View.VISIBLE
                    binding.ivCreateNameAlarm.setImageResource(R.drawable.ic_cancel)
                    binding.tvCreateNameAlarm.text = "닉네임을 입력해주세요"
                } else if(nickname.length == 6) {
                    binding.ivCreateNameAlarm.visibility = View.VISIBLE
                    binding.tvCreateNameAlarm.visibility = View.VISIBLE
                    binding.ivCreateNameAlarm.setImageResource(R.drawable.ic_info)
                    binding.tvCreateNameAlarm.text = "최대 6글자입니다."
                } else { // 비어있지 않고 길이 충족한다면 -> 중복확인 진행
                    binding.ivCreateNameAlarm.visibility = View.VISIBLE
                    binding.tvCreateNameAlarm.visibility = View.VISIBLE
                    binding.ivCreateNameAlarm.setImageResource(R.drawable.ic_info)
                    binding.tvCreateNameAlarm.text = "닉네임 작성 후 중복 확인해 주세요"
                    // 닉네임 중복 확인 버튼
                    binding.btnCreateNameCheck.setOnClickListener {
                        initNickNameCheck()
                    }
                }
            }

            // 입력이 끝났다면
            override fun afterTextChanged(s: Editable?) {

            }
        })
    }
    // =======================================닉네임 입력=======================================

    // =======================================닉네임 중복확인=======================================
    private fun initNickNameCheck() { // firebase db에서 닉네임을 가져와서 비교한다.
        val userNickname = binding.edCreateName.text.toString()


            // 닉네임 조회
            firestore.collection("user").get().addOnSuccessListener { // 닉네임이 입력되었다면 조회 가능
                    result ->
                for(document in result) {
                    // 만약 조회한 닉네임과 현재 입력한 닉네임이 같지않다면
                    if(userNickname.isNotEmpty()){
                        if(document.id != userNickname){
                            binding.ivCreateNameAlarm.visibility = View.VISIBLE
                            binding.tvCreateNameAlarm.visibility = View.VISIBLE
                            binding.ivCreateNameAlarm.setImageResource(R.drawable.ic_check_circle)
                            binding.tvCreateNameAlarm.text = "사용가능한 닉네임입니다."
                        } else { // 조회한 닉네임과 현재 입력한 닉네임이 같다. -> edittext 지워버린다.
                            binding.ivCreateNameAlarm.visibility = View.VISIBLE
                            binding.tvCreateNameAlarm.visibility = View.VISIBLE
                            binding.ivCreateNameAlarm.setImageResource(R.drawable.ic_cancel)
                            binding.tvCreateNameAlarm.text = "닉네임이 존재합니다."
                            binding.edCreateName.text.clear()
                        }
                    }
                }
            }
                .addOnFailureListener {
                        exception ->

                }

    }
    // =======================================닉네임 중복확인=======================================

    // =======================================확인버튼 -> 닉네임 작성 성공=======================================
    private fun initBtnClickSuccess(){ // 상단의 중복확인이 통과되었으면 실행 -> 닉네임은 중복이 되면 안된다.
        val userNickname = binding.edCreateName.text.toString()

        var createNickname = UserModel()
        createNickname.userId = auth.currentUser?.email // 로그인 후 현재 유저의 이메일
        createNickname.uid = auth.currentUser?.uid // uid값
        createNickname.nickName = binding.edCreateName.text.toString()
        createNickname.timestamp = System.currentTimeMillis()

        // user 컬렉션 안에 닉네임 이름으로된 문서로 user정보와 닉네임을 추가해서 저장
        firestore.collection("user").document(userNickname).set(createNickname)
        Toast.makeText(this, "닉네임 생성 완료", Toast.LENGTH_SHORT).show()


        // 닉네임 생성 확인버튼 클릭 시 -> Firebase Database에 닉네임을 문서이름으로 user정보와 닉네임 같이 등록

    }
    // =======================================확인버튼 -> 닉네임 작성 성공=======================================

    // =======================================닉네임 입력 창이 비었다면=======================================
    private fun initNicknameEmpty(){
        Toast.makeText(this, "닉네임 입력 후 시도해주세요.", Toast.LENGTH_SHORT).show()
    }
    // =======================================닉네임 입력 창이 비었다면=======================================
}