package com.duran.selfmg.ui.view.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.duran.selfmg.R
import com.duran.selfmg.databinding.ActivitySignUpPhBinding
import com.google.android.material.snackbar.Snackbar

class SignUpPhActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpPhBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up_ph)

        // 인증하기 버튼 클릭 시 인증번호 보내기
        initSendSMS()
        // 권한 체크
        checkPermission()

    }

    // =======================================인증번호 보내기=======================================
    private fun initSendSMS() {
        binding.btnSendSms.setOnClickListener {
            val phoneNum = binding.etInputPhoneNum.text.toString()
            val msg = "테스트 메시지"
            val smsManager = SmsManager.getDefault()

            smsManager.sendTextMessage(phoneNum, null, msg, null, null)
            Toast.makeText(this, "인증메세지 전송", Toast.LENGTH_SHORT).show()
        }
    }
    // =======================================인증번호 보내기=======================================

    // =======================================권한 체크=======================================
    // 권한 체크
    private fun checkPermission() {
        // 권한ID를 가져온다.
        val permissionCheck = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)

        // 권한이 열려있는지 확인 PERMISSION_GRANTED -> 이미 권한을 가지고 있다면
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) { // 권한을 가지고 있지 않다면
            // 만약 사용자가 권한을 거부한 적이 있다면
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {
                Snackbar.make(binding.root, "인증을 위해 SMS권한이 필요합니다.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("확인", View.OnClickListener() {
                        // 다시 사용자에게 요청을 합니다. 이 요청결과는 onRequestPermissionsResult으로 수신
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECEIVE_SMS), 1) // 권한 요청 창
                    }).show()
            } else { // 거부한적이 없다면
                // 마쉬멜로우 이상 버전부터 권한 물어본다
                // 권한 체크
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECEIVE_SMS), 1) // 권한 요청 창
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // 만약 requestcode가 1이고 요청한 권한 갯수만큼 수신했다면
        if (requestCode == 1) {

            // 권한을 허용했는지 체크
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // 권한 승인됨

            } else { // 권한 승인되지 않음
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {
                    Snackbar.make(binding.root, "권한이 거부되었습니다. 인증을 위해 다시 실행하여 권한을 허용해주세요.", Snackbar.LENGTH_INDEFINITE)
                        .setAction("확인", View.OnClickListener() {
                            // 다시 사용자에게 요청을 합니다. 이 요청결과는 onRequestPermissionsResult으로 수신
                            finish()
                        }).show()
                } else {
                    // 다시 묻지 않음 을 사용자가 체크하고 거부를 선택한 경우 설정에서 권한 허용해야 앱을 사용할 수 있다.
                    Snackbar.make(binding.root, "권한이 거부되었습니다. 설정에서 권한을 허용해야 합니다.", Snackbar.LENGTH_INDEFINITE)
                        .setAction("확인", View.OnClickListener() {
                            finish()
                        }).show()
                }
            }
        }
    }
    // =======================================권한 체크=======================================
}