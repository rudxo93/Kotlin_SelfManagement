package com.duran.selfmg.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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

class LoginActivity : AppCompatActivity() {

    // 데이터바인딩 사용
    lateinit var binding: ActivityLoginBinding
    // 계정인증
    lateinit var auth: FirebaseAuth
    // 구글 로그인
    lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        // auth를 사용하기 전에 싱글톤패턴으로 인스턴스 받아온다.
        auth = FirebaseAuth.getInstance()

        // 구글 로그인
        binding.googleLoginButton.setOnClickListener {
            googleLogin()
        }
        // 구글 로그인 클라이언트
        initGoogleLoginClient()

        // 일반 로그인
        binding.btnEmailLogin.setOnClickListener {
            initSignIn()
        }
        // 회원가입
        initSignUpBtnClicked()
    }

    // =======================================회원가입=======================================
    /* 회원가입 버튼 클릭 -> 회원가입 페이지 이동 */
    private fun initSignUpBtnClicked() {
        binding.tvSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    /* 회원가입 버튼 클릭 -> 회원가입 페이지 이동 */
    // =======================================회원가입=======================================

    // =======================================일반 로그인=======================================
    private fun initSignIn() {
        val email = binding.edittextId.text.toString()
        val pw = binding.edittextPassword.text.toString()

        auth.signInWithEmailAndPassword(email, pw).addOnCompleteListener {
                task ->
            if (task.isSuccessful) {
                moveNickname(task.result?.user)
                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
            }
        }
    }
    // =======================================일반 로그인=======================================

    // =======================================구글 로그인 관련=======================================
    private fun initGoogleLoginClient() {
        // 구글 로그인 클라이언트 설정 및 생성
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // 구글 API키 -> ID Token 가져온다.
            .requestEmail()
            .build()
        // 옵션값 구글로그인 클라이언트 세팅
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }
    /* 구글 로그인 */
    private fun googleLogin() {
        val intent = googleSignInClient.signInIntent
        googleLoginResult.launch(intent)
    }
    /* 구글 로그인 */
    /* 구글 로그인 결과 */
    var googleLoginResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data = result.data // ActivityResult객체 result로 data를 받아온다.
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        val account = task.getResult(ApiException::class.java)
        firebaseAuthGoogle(account.idToken)
    }
    /* 구글 로그인 결과 */
    /* 구글 로그인 */
    private fun firebaseAuthGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener {
                task->
            if(task.isSuccessful){
                if(auth.currentUser!!.isEmailVerified){
                    // 구글 로그인 인증되었을때
                    moveNickname(auth.currentUser)
                    Toast.makeText(this, "구글 로그인 성공", Toast.LENGTH_SHORT).show()
                } else {
                    // 구글 로그인 인증 실패
                    Toast.makeText(this, "구글 로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    /* 구글 로그인 */
    // =======================================구글 로그인 관련=======================================

    // =======================================로그인 성공=======================================
    /* 로그인 성공했다면 Main Activity로 이동 */
    private fun moveNickname(user: FirebaseUser?) {
        if(user != null) {
            val intent = Intent(this, CreateNameActivity::class.java)
            val uid = auth.currentUser?.uid
            // 로그인 성공 후 닉네임 생성 페이지 이동 시 현재 유저의 uid정보를 dUid에 담아서 전달한다.
            intent.putExtra("dUid", uid)
            startActivity(intent)
            finish()
        }
    }
    /* 로그인 성공했다면 Main Activity로 이동 */
    // =======================================로그인 성공=======================================
}