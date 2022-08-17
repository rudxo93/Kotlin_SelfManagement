package com.duran.selfmg.ui.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.duran.selfmg.R
import com.duran.selfmg.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        // 회원가입
        initSignUpBtnClicked()

        return binding.root
    }

    // =======================================회원가입 이동=======================================
    /* 회원가입 버튼 클릭 -> 회원가입(핸드폰 인증) 페이지 이동 */
    private fun initSignUpBtnClicked() {
        binding.tvSignUp.setOnClickListener {
            view?.let { it -> Navigation.findNavController(it).navigate((R.id.action_loginFragment_to_signUpPhFragment))
            }
        }
    }
    /* 회원가입 버튼 클릭 -> 회원가입(핸드폰 인증) 페이지 이동 */
    // =======================================회원가입 이동=======================================

}