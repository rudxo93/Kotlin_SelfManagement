package com.duran.selfmg.ui.view.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.duran.selfmg.R
import com.duran.selfmg.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding // 데이터바인딩 사용
    private lateinit var auth: FirebaseAuth // 계정인증
    private lateinit var firestore: FirebaseFirestore // Firebase DB
    private lateinit var googleSignInClient: GoogleSignInClient // 구글 로그인

    private val email by lazy { binding.edittextId } // 이메일 입력
    private val pw by lazy { binding.edittextPassword } // 비밀번호 입력
    private val loginBtn by lazy { binding.btnEmailLogin } // 일반 로그인
    private val googleLoginBtn by lazy { binding.googleLoginButton } // 구글 로그인 버튼
    private val signUpBtn by lazy { binding.tvSignUp } // 회원가입입

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        // auth를 사용하기 전에 싱글톤패턴으로 인스턴스 받아온다.
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        initSignUpBtnClicked() // 회원가입
        initLoginBtn() // 일반 로그인
        initGoogleLoginBtn() // 구글 로그인
        initGoogleLoginClient() // 구글 클라이언트

    }

    // ======================================= 로그인 =======================================
    // 로그인 버튼 클릭
    private fun initLoginBtn() {
        /* 일반 로그인
           1. 우선 이메일이 비었는지
           2. 비밀번호가 비었는지
           3. 비어있지 않다면 로그인 시도
           4. 로그인 정보가 없다면 회원가입 알람
           5. DB에 유저가 닉네임이 있는 유저인지 판별
           6. 닉네임이 있다면 컨텐츠가 있는 페이지, 없다면 닉네임 생성 페이지 이동*/
        loginBtn.setOnClickListener {
            Log.d("tag-Login", "일반 로그인 - 일반 로그인 버튼 클릭 -> 일반 로그인을 진행합니다.")
            if(email.text.isEmpty()) {
                initEmailEmpty()
            } else if (pw.text.isEmpty()) {
                initPwEmpty()
            } else {
                initSignIn()
            }
        }
    }

    // 로그인
    private fun initSignIn() {
        auth.signInWithEmailAndPassword(email.text.toString(), pw.text.toString()).addOnCompleteListener {
                task ->
            if (task.isSuccessful) {
                Log.d("tag-Login", "일반 로그인 - 일반 로그인에 성공했습니다. 닉네임의 존재여부를 확인합니다.")
                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                initNicknameSelect(task.result?.user)
            } else {
                initLoginFail()
            }
        }
    }

    // 일반 로그인시 닉네임 존재 여부
    private fun initNicknameSelect(user: FirebaseUser?) {
        val loginEmail = email.text.toString()
        firestore.collection("user").whereEqualTo("userId", loginEmail).get().addOnSuccessListener {
            result ->
            for(item in result.documentChanges) {
                Log.d("tag-Login", "일반 로그인 - 닉네임이 있는 사용자입니다. 메인 엑티비티로 이동합니다.")
                moveMain(user)
            }
            if(result.size() == 0) {
                Log.d("tag-Login", "일반 로그인 - 닉네임이 없는 사용자입니다. 닉네임만들기 엑티비티로 이동합니다.")
                moveCreateNickname(user)
            }
        }

    }

    // ======================================= 구글 클라이언트 =======================================
    // 구글 클라이언트 설정 및 생성
    private fun initGoogleLoginClient() {
        // 구글 로그인 클라이언트 설정 및 생성
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // 구글 API키 -> ID Token 가져온다.
            .requestEmail()
            .build()
        // 옵션값 구글로그인 클라이언트 세팅
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    // ======================================= 구글 로그인 =======================================
    // 구글 로그인 버튼 클릭
    private fun initGoogleLoginBtn() {
        googleLoginBtn.setOnClickListener {
            Log.d("tag-GoogleLogin", "구글 로그인 - 구글 로그인 버튼 클릭 -> 구글로그인을 진행합니다.")
            googleLogin()
        }
    }

    // 구글 로그인
    private fun googleLogin() {
        val intent = googleSignInClient.signInIntent
        googleLoginResult.launch(intent)
    }

    // 구글 로그인 결과
    private var googleLoginResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data = result.data // ActivityResult객체 result로 data를 받아온다.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            firebaseAuthGoogle(account.idToken)
            /*initGoogleNicknameSelect(account.idToken)*/
        }

    private fun firebaseAuthGoogle(idToken: String?) { // 구글 로그인 결과
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
                task ->
            if (task.isSuccessful) {
                if (auth.currentUser!!.isEmailVerified) {
                    // 구글 로그인 인증되었을때
                    Log.d("tag-GoogleLogin", "구글 로그인 - 구글로그인이 성공했습니다. 닉네임의 존재여부를 확인합니다.")
                    Toast.makeText(this, "구글 로그인 성공", Toast.LENGTH_SHORT).show()
                    initGoogleNicknameSelect(auth.currentUser)
                } else {
                    // 구글 로그인 인증 실패
                    Log.d("tag-GoogleLogin", "구글 로그인 - 구글로그인이 실패했습니다. 구글 로그인인증에 문제가 있습니다.")
                    Toast.makeText(this, "구글 로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // 구글 로그인시 닉네임 존재 여부
    private fun initGoogleNicknameSelect(user: FirebaseUser?) {
        val loginEmail = auth.currentUser?.email
        firestore.collection("user").whereEqualTo("userId", loginEmail).get().addOnSuccessListener {
                result ->
            for(item in result.documentChanges) {
                Log.d("tag-GoogleLogin", "구글 로그인 - 구글로그인의 닉네임이 있습니다. 메인 엑티비티로 이동합니다.")
                moveMain(user)
            }
            if(result.size() == 0) {
                Log.d("tag-GoogleLogin", "구글 로그인 - 구글로그인의 닉네임이 없습니다. 닉네임만들기 엑티비티로 이동합니다.")
                moveCreateNickname(user)
            }
        }

    }

    // =======================================닉네임 있는 사용자 로그인=======================================
    // 일반 로그인
    private fun moveMain(user: FirebaseUser?) {
        if(user != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // =======================================닉네임 없는 사용자 로그인=======================================
    // 일반 로그인
    private fun moveCreateNickname(user: FirebaseUser?) {
        if(user != null) {
            val intent = Intent(this, CreateNameActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // =======================================회원가입=======================================
    private fun initSignUpBtnClicked() { // 회원가입 버튼 클릭 -> 회원가입 페이지 이동
        signUpBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    // ============================================================Dialog============================================================
    /*<<<<<<<<<< 로그인 >>>>>>>>>>*/
    // =======================================로그인 이메일 입력창이 비었다면 Dialog창=======================================
    private fun initEmailEmpty() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("로그인 에러")
            .setMessage("이메일 입력 후 시도해주세요.")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { _, _ ->
                    email.requestFocus()
                    Log.d("tag-Login", "로그인 - 로그인 이메일 입력창이 비었습니다.")
                })
            .setCancelable(false)
        builder.show()
    }

    // =======================================로그인 비밀번호 입력창이 비었다면 Dialog창=======================================
    private fun initPwEmpty() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("로그인 에러")
            .setMessage("비밀번호 입력 후 시도해주세요.")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { _, _ ->
                    pw.requestFocus()
                    Log.d("tag-Login", "로그인 - 로그인 비밀번호 입력창이 비었습니다.")
                })
            .setCancelable(false)
        builder.show()
    }

    // =======================================로그인 로그인에 실패했다면 Dialog창=======================================
    private fun initLoginFail() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("로그인 실패")
            .setMessage("존재하지 않는 계정이거나 이메일 또는 비밀번호가 틀렸습니다.")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { _, _ ->
                    Log.d("tag-Login", "로그인 - 로그인실패!! 계정이 없거나 이메일, 비밀번호 틀림")
                })
            .setCancelable(false)
        builder.show()
    }

}