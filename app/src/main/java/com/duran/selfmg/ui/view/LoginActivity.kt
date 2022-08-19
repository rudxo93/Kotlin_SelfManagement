package com.duran.selfmg.ui.view

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
import com.duran.selfmg.data.model.UserModel
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

    // 데이터바인딩 사용
    lateinit var binding: ActivityLoginBinding

    // 계정인증
    lateinit var auth: FirebaseAuth

    // Firebase DB
    lateinit var firestore: FirebaseFirestore

    // 구글 로그인
    lateinit var googleSignInClient: GoogleSignInClient

    private val email by lazy { binding.edittextId.text } // 이메일 입력
    private val pw by lazy { binding.edittextPassword.text } // 비밀번호 입력
    private val loginBtn by lazy { binding.btnEmailLogin } // 일반 로그인
    private val googleLoginBtn by lazy { binding.googleLoginButton } // 구글 로그인 버튼
    private val signUpBtn by lazy { binding.tvSignUp } // 회원가입입

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        // auth를 사용하기 전에 싱글톤패턴으로 인스턴스 받아온다.
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // 일반 로그인
        loginBtn.setOnClickListener {
            if (email.isEmpty()) { // 이메일이 비었다면
                initEmailEmpty()
            } else if (pw.isEmpty()) { // 비밀번호가 비었다면
                initPasswordEmpty()
            } else { // 이메일과 비밀번호가 입력되었다면
                initSignIn()
            }
        }

        // 구글 로그인
        googleLoginBtn.setOnClickListener {
            googleLogin()
        }
        // 구글 로그인 클라이언트
        initGoogleLoginClient()

        // 회원가입
        initSignUpBtnClicked()
    }

    // =======================================일반 로그인=======================================
    private fun initSignIn() {
        auth.signInWithEmailAndPassword(email.toString(), pw.toString()).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                loginSuccess(task.result?.user)
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

    private fun googleLogin() { // 구글 로그인
        val intent = googleSignInClient.signInIntent
        googleLoginResult.launch(intent)
    }

    // 구글 로그인 결과
    var googleLoginResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data = result.data // ActivityResult객체 result로 data를 받아온다.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            firebaseAuthGoogle(account.idToken)
        }

    private fun firebaseAuthGoogle(idToken: String?) { // 구글 로그인 결과
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (auth.currentUser!!.isEmailVerified) {
                    // 구글 로그인 인증되었을때
                    loginSuccess(auth.currentUser)
                    Toast.makeText(this, "구글 로그인 성공", Toast.LENGTH_SHORT).show()
                } else {
                    // 구글 로그인 인증 실패
                    Toast.makeText(this, "구글 로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    // =======================================구글 로그인 관련=======================================

    // =======================================로그인 성공=======================================
    /* 로그인 성공했다면 DB에 user컬렉션으로 데이터 등록 */
    private fun loginSuccess(user: FirebaseUser?) {
        val uid = auth.currentUser?.uid
        Log.d("tag", "==== 구분자 ====")

        // user가 null이 아니다? -> 정상적인 로그인
        if (user != null) {
            Log.e("tag", "==== user컬렉션을 감시합니다. ====")
            Log.d("tag", "==== 구분자 ====")
            firestore.collection("user").addSnapshotListener { value, error ->

                if (value!!.documentChanges.isEmpty()) {
                    Log.e("tag", "==== 최초로그인 입니다. ====")
                    Log.d("tag", "==== 구분자 ====")
                    // DB가 존재하지 않기 때문에 새로 생성 후 로그인 정보를 담아준다.
                    var createNickname = UserModel()

                    createNickname.userId = user.email // 로그인 후 현재 유저의 이메일
                    createNickname.uid = user.uid // uid값
                    createNickname.timestamp = System.currentTimeMillis()

                    Log.e("tag", "==== 유저정보 생성합니다. ====")
                    Log.d("tag", "==== 구분자 ====")
                    firestore.collection("user").document(uid!!).set(createNickname)
                    return@addSnapshotListener
                }

                for (item in value.documentChanges) {
                    Log.e("tag", "==== 최초로그인이 아닙니다. ====")
                    Log.d("tag", "==== 구분자 ====")
                    if (item.document.id == user.uid) {
                        Log.e("tag", "==== 로그인한 계정의 uid와 일치하는게 있습니다. 닉네임 유무에 따른 페이지 이동입니다. ====")
                        Log.d("tag", "==== 구분자 ====")

                    } else {
                        Log.e("tag", "==== 해당 컬렉션에 현재 계정의 uid와 일치하는 문서가 없습니다. ====")
                        Log.d("tag", "==== 구분자 ====")
                        var createNickname = UserModel()

                        createNickname.userId = user.email // 로그인 후 현재 유저의 이메일
                        createNickname.uid = uid // uid값
                        createNickname.timestamp = System.currentTimeMillis()
                        firestore.collection("user").document(uid!!).set(createNickname)
                        Log.e("tag", "==== 해당 컬렉션에 현재 계정의 uid와 일치하는 문서가 없기때문에 생성합니다. ====")
                        Log.d("tag", "==== 구분자 ====")
                    }

                }
            }

        }
    }

    // =======================================로그인 성공=======================================

    // =======================================일반 로그인 성공 시 유저 정보 저장=======================================
    /*  private fun initLoginUserInfo(uid: String) {

          firestore.collection("user").get().addOnSuccessListener {
              result ->
              if(result.isEmpty) {
                  // DB가 존재하지 않기 때문에 새로 생성 후 로그인 정보를 담아준다.
                  var createNickname = UserModel()

                  createNickname.userId = auth.currentUser?.email // 로그인 후 현재 유저의 이메일
                  createNickname.uid = uid // uid값
                  createNickname.timestamp = System.currentTimeMillis()

                  firestore.collection("user").document(uid).set(createNickname)
              } else {
                  // DB가 존재한다면 user에 추가만 진행 -> 이때 이미 조회 후 이미 존재하는 uid라면 생성x
                  // user 컬렉션 조회
                  firestore.collection("user").get().addOnSuccessListener {
                      result ->
                      for(doc in result){
                          // DB에 조회한 uid와 지금 로그인을 시도한 uid와 같지않다면 -> DB에 생성해야한다.
                          if(doc.id != uid) {
                              // DB가 존재하지 않기 때문에 새로 생성 후 로그인 정보를 담아준다.
                              var createNickname = UserModel()

                              createNickname.userId = auth.currentUser?.email // 로그인 후 현재 유저의 이메일
                              createNickname.uid = uid // uid값
                              createNickname.timestamp = System.currentTimeMillis()

                              firestore.collection("user").document(uid).set(createNickname)
                          }
                      }
                  }
              }
          }

      }*/

    // =======================================회원가입=======================================
    private fun initSignUpBtnClicked() { // 회원가입 버튼 클릭 -> 회원가입 페이지 이동
        signUpBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // ============================================================Dialog============================================================
    // =======================================로그인 이메일 입력창이 비었다면 Dialog창=======================================
    private fun initEmailEmpty() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("로그인 에러")
            .setMessage("이메일 입력 후 시도해주세요.")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, which ->

                })
            .setCancelable(false)
        builder.show()
    }

    // =======================================로그인 비밀번호 입력창이 비었다면 Dialog창=======================================
    private fun initPasswordEmpty() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("로그인 에러")
            .setMessage("비밀번호 입력 후 시도해주세요.")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, which ->

                })
            .setCancelable(false)
        builder.show()
    }
}