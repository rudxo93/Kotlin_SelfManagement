package com.duran.selfmg.ui.view.activity

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.duran.selfmg.R
import com.duran.selfmg.data.model.UserModel
import com.duran.selfmg.databinding.ActivityCreateNameBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateNameActivity : AppCompatActivity() {

    lateinit var binding: ActivityCreateNameBinding
    private lateinit var firestore: FirebaseFirestore // Firebase DB
    private lateinit var auth: FirebaseAuth // Firebase Auth

    private lateinit var currentUid: String

    private val nickname by lazy { binding.edCreateName }
    private val btnNicknameCheck by lazy { binding.btnCreateNameCheck }
    private val btnNicknameSuccess by lazy { binding.btnSuccessCreateName }

    private var isSuccess = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_name)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        currentUid = auth.currentUser!!.uid // 현재 로그인된 사용자의 uid정보

        initToolBarSetting() // 상단 툴바
        initNicknameCheck() // 닉네임 중복 확인
        initCreateNicknameFinishBtn() // 닉네임 만들기 최종 확인 버튼
    }

    // ======================================= 닉네임 생성 상단 툴바 =======================================
    private fun initToolBarSetting() {
        val toolbar = binding.toolbarCreateName

        // 툴바 생성
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // ======================================= 닉네임 중복 확인 =======================================
    // 닉네임 중복확인 버튼 클릭
    private fun initNicknameCheck() {
        val useNickname = nickname.text
        /* 닉네임 중복 확인
           1. 우선 닉네임 입력 칸이 빈칸인지 체크
           2. user 컬렉션에서 같은 닉네임이 존재하는지 체크
           3. 중복이 아니라면 DB에 생성해주고 메인엑티비티 이동
           4. 중복이라면 중복이라고 알람*/
        btnNicknameCheck.setOnClickListener {
            if (useNickname.isNotEmpty()) {
                initNicknameSelect()
            } else {
                initNicknameEmpty()
            }
        }
    }

    // 닉네임 사용중인지 조회
    private fun initNicknameSelect() {
        Log.d("tag-Select-Nickname", "닉네임 만들기 - 현재 사용중인 닉네임을 조회합니다.... 기다려주세요....")
        val selectNickname = nickname.text.toString()

        firestore.collection("user").whereEqualTo("nickName", selectNickname).get()
            .addOnSuccessListener {
                result ->
                for (item in result.documentChanges) {
                    initUseNickname() // 이미 사용중인 닉네임 알람
                }
                if(result.size() == 0) {
                    initNicknameSuccess() // 사용가능 닉네임 알람
                }

        }
    }

    // ======================================= 닉네임 DB에 저장하고 회원가입 완료 =======================================
    private fun initCreateNicknameFinishBtn() {
        btnNicknameSuccess.setOnClickListener {
            if(nickname.text.isEmpty()) { // 닉네임이 비어있음
                initSignUpNicknameEmpty()
            } else if(isSuccess == 0) { // 닉네임 중복확인 x
                initSignUpNicknameFail()
            } else { // 닉네임이 입력되어있고 중복확인 통과 -> isSuccess = 1
                initFbInsertNickname()
            }
        }
    }

    // 닉네임을 DB에 등록
    private fun initFbInsertNickname() {
        val useNickname = nickname.text.toString()

        val userModel = UserModel()
        userModel.nickName = useNickname
        userModel.userId = auth.currentUser?.email
        userModel.uid = auth.uid
        userModel.timestamp = System.currentTimeMillis()
        // user 컬렉션에 닉네임으로된 문서에 닉네임 필드에 닉네임을 추가한다.
        firestore.collection("user").document(useNickname).set(userModel)
        Log.d("tag-Insert-Nickname", "닉네임 만들기 - 현재 입력한 닉네임으로 DB에 저장합니다.")
        moveMain(useNickname)
    }

    // 메인 엑티비티 이동
    private fun moveMain(nickname: String?) {
        if(nickname != null) {
            val intent = Intent(this, MainActivity()::class.java)
            startActivity(intent)
            finish()
        }
    }

    // ============================================================Dialog============================================================
    /*<<<<<<<<<< 닉네임 생성 >>>>>>>>>>*/
    // ======================================= 닉네임 입력창이 비어있다면 =======================================
    private fun initNicknameEmpty() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("닉네임 중복 확인")
            .setIcon(R.drawable.ic_cancel)
            .setMessage("닉네임 입력 후 시도해주세요.")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, which ->
                    nickname.requestFocus()
                    isSuccess = 0
                    Log.d("tag-Create-Nickname", "닉네임 만들기 - 현재 닉네임 입력칸이 비었습니다. 닉네임을 입력해주세요. ")
                })
            .setCancelable(false)
        builder.show()
    }

    // ======================================= 사용중인 닉네임이라면 =======================================
    private fun initUseNickname() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("닉네임 중복 확인")
            .setIcon(R.drawable.ic_info)
            .setMessage("현재 사용중인 닉네임입니다. 다른 닉네임 입력 후 시도해주세요.")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, which ->
                    nickname.requestFocus()
                    nickname.text.clear()
                    isSuccess = 0
                    Log.d("tag-Select-Nickname", "닉네임 만들기 - 현재 사용중인 닉네임입니다. 다른 닉네임을 사용해주세요. ")
                })
            .setCancelable(false)
        builder.show()
    }

    // ======================================= 사용중인 닉네임이라면 =======================================
    private fun initNicknameSuccess() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("닉네임 중복 확인")
            .setIcon(R.drawable.ic_check_circle)
            .setMessage("사용 가능한 닉네임입니다.")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, which ->
                    // 키패드 내리기
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(nickname.windowToken, 0)
                    nickname.isEnabled = false
                    isSuccess = 1
                    Log.d("tag-Select-Nickname", "닉네임 만들기 - 사용 가능한 닉네임입니다. 해당 닉네임으로 가입을 진행합니다.")
                })
            .setCancelable(false)
        builder.show()
    }

    /*<<<<<<<<<< 닉네임 완료 >>>>>>>>>>*/
    // ======================================= 사용중인 닉네임이라면 =======================================
    private fun initSignUpNicknameEmpty() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("닉네임 생성 실패")
            .setIcon(R.drawable.ic_cancel)
            .setMessage("닉네임이 비어있습니다. 닉네임을 입력해주세요.")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, which ->
                    nickname.requestFocus()
                    isSuccess = 0
                    Log.d("tag-SignUp-Nickname", "닉네임 완료 - 닉네임을 입력하지 않고 확인버튼을 눌렀습니다. 닉네임을 먼저 입력해주세요.")
                })
            .setCancelable(false)
        builder.show()
    }

    // ======================================= 사용중인 닉네임이라면 =======================================
    private fun initSignUpNicknameFail() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("닉네임 생성 실패")
            .setIcon(R.drawable.ic_info)
            .setMessage("닉네임 중복확인은 필수입니다. 닉네임 중복확인 후 다시 시도해주세요")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, which ->
                    nickname.requestFocus()
                    isSuccess = 0
                    Log.d("tag-SignUp-Nickname", "닉네임 완료 - 닉네임 중복확인을 하지 않고 확인버튼을 눌렀습니다. 닉네임 중복확인은 필수입니다.")
                })
            .setCancelable(false)
        builder.show()
    }

}