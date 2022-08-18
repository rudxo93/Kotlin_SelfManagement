package com.duran.selfmg.ui.view

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_name)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val userNickname = binding.edCreateName.text

        // 상단 툴바
        initToolBarSetting()

        // 닉네임 작성 후 중복확인 버튼 클릭 시
        binding.btnCreateNameCheck.setOnClickListener {
            initNickNameCheck()
        }

        // 닉네임 생성 확인버튼 클릭 시 -> Firebase Database에 닉네임을 문서이름으로 user정보와 닉네임 같이 등록
        binding.btnSuccessCreateName.setOnClickListener {
            when(userNickname.length){
                0 -> {
                    initNicknameEmpty()
                }
                else -> {
                    initBtnClickSuccess()
                }
            }
        }
    }

    // =======================================닉네임 중복확인=======================================
    // 전제조건 -> 닉네임을 작성하고 눌러야 한다.
    private fun initNickNameCheck() { // firebase db에서 닉네임을 가져와서 비교한다.
        val userNickname = binding.edCreateName.text.toString()

        // 중복확인 클릭 시 현재 닉네임 입력창이 비었는지 아닌지에 대한 동작
        when(userNickname.length) {
            0 -> { // 닉네임 입력창이 비어있다.
                initNicknameEmpty()
            }
            else -> { // 이외에 닉네임 입력창이 작성되어 있다면 -> 닉네임 체크
                // 닉네임 조회 -> user컬렉션의 모든 닉네임 문서 조회
                firestore.collection("user").get().addOnSuccessListener { // 닉네임이 입력되었다면 조회 가능
                        result ->
                    // 닉네임 조회결과를 result에 담고 document에 전달
                    for(document in result) { // 조회 결과 ->
                        // 만약 조회 결과와 현재 입력된 닉네임이 같다면
                        if(document.id.equals(userNickname)){
                            initNicknameFail()
                        }
                        // 조회된 결과와 현재 입력된 닉네임이 같지 않다면
                        else {
                            initNicknameSuccess()
                        }
                    }
                }
            }
        }

    }
    // =======================================닉네임 중복확인=======================================

    // =======================================닉네임 입력창이 비었다면 Dialog창=======================================
    private fun initNicknameEmpty() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("중복 확인")
            .setMessage("닉네임 입력 후 시도해주세요.")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, which ->

                })
        builder.show()
    }
    // =======================================닉네임 입력창이 비었다면 Dialog창=======================================

    // =======================================닉네임 사용가능 Dialog창=======================================
    private fun initNicknameSuccess() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("중복 확인")
            .setMessage("사용가능한 닉네임입니다.")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, which ->

                })
        builder.show()
    }
    // =======================================닉네임 사용가능 Dialog창=======================================

    // =======================================닉네임 중복일시 Dialog창=======================================
    // 확인 하게되면 다시 닉네임 입력창을 비워준다.
    private fun initNicknameFail() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("중복 확인")
            .setMessage("사용중인 닉네임입니다. 다시 닉네임작성 후 확인해주세요.")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, which ->
                    binding.edCreateName.text.clear()
                })
        builder.show()
    }
    // =======================================닉네임 중복일시 Dialog창=======================================

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

        // 메인 페이지 이동
        moveMain()
    }
    // =======================================확인버튼 -> 닉네임 작성 성공=======================================

    // =======================================메인페이지 이동=======================================
    private fun moveMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    // =======================================메인페이지 이동=======================================

    // =======================================닉네임 툴바=======================================
    /* toolbar */
    private fun initToolBarSetting() {
        val toolbar = binding.toolbar

        // 툴바 생성
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when(id) {
            android.R.id.home -> {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    // =======================================닉네임 툴바=======================================
}