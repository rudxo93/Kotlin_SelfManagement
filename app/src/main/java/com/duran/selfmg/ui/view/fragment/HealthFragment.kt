package com.duran.selfmg.ui.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.duran.selfmg.R
import com.duran.selfmg.data.model.HealthBmiEntity
import com.duran.selfmg.databinding.FragmentHealthBinding
import com.duran.selfmg.ui.view.activity.BmiCalculationActivity
import com.duran.selfmg.ui.viewmodel.HealthBmiViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HealthFragment : Fragment() {

    private lateinit var binding: FragmentHealthBinding
    private lateinit var auth: FirebaseAuth // 계정인증
    private lateinit var healthBmiViewModel: HealthBmiViewModel

    private val btnBmi by lazy { binding.btnBmi }
    private val tvBmiResultNumValue by lazy { binding.tvBmiResultNumValue }
    private val tvBmiResultRangeValue by lazy { binding.tvBmiResultRangeValue }

    private val btnBmiasdf by lazy { binding.btnBmi12312312 }

    private var userBmi: HealthBmiEntity? = null

    var userBmiNum: String? = null
    var userBmiRange: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_health, container, false)

        auth = FirebaseAuth.getInstance()
        healthBmiViewModel = ViewModelProvider(this)[HealthBmiViewModel::class.java]

        /*initChangeText()*/
        initToolBarSetting()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*val currentUser = auth.currentUser?.email*/

        initBtnBmiClick()
        /*initLivedata()*/

        val currentUser = auth.currentUser?.email
        CoroutineScope(Dispatchers.IO).launch {
            userBmi = healthBmiViewModel.getBmi(currentUser.toString())
            /*Log.e("tag", userBmi.bmiNum)
            Log.e("tag", userBmi.userEmail)
            Log.e("tag", userBmi.bmiRange)*/
        }
        userBmi?.let { Log.e("tag", it.bmiNum) }

        userBmiNum = userBmi?.bmiNum
        userBmiRange = userBmi?.bmiRange

        btnBmiasdf.setOnClickListener {
            userBmi?.let { it1 -> Log.e("tag", it1.bmiNum) }
        }


    }

    // ======================================= 자기관리 툴바 =======================================
    private fun initToolBarSetting() {
        val toolbar = binding.healthToolbar
        toolbar.inflateMenu(R.menu.item_toolbar_health)


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.item_toolbar_schedule, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    // ======================================= MBI 측정하기 버튼 클릭 =======================================
    private fun initBtnBmiClick() {
        btnBmi.setOnClickListener {
            val intent = Intent(activity, BmiCalculationActivity::class.java)
            startActivity(intent)
        }
    }

    // =======================================  =======================================
    /*private fun initChangeText() {
        val currentUser = auth.currentUser?.email
    }*/


}