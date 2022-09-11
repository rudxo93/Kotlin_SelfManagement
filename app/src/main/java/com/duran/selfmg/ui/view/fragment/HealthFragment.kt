package com.duran.selfmg.ui.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.duran.selfmg.R
import com.duran.selfmg.databinding.FragmentHealthBinding
import com.duran.selfmg.ui.view.activity.BmiCalculationActivity

class HealthFragment : Fragment() {

    lateinit var binding: FragmentHealthBinding

    private val btnBmi by lazy { binding.btnBmi }
    private val tvBmiResult by lazy { binding.tvBmiResult }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_health, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBtnBmiClick()
    }

    private fun initBtnBmiClick() {
        btnBmi.setOnClickListener {
            val intent = Intent(activity, BmiCalculationActivity::class.java)
            startActivity(intent)
        }
    }
}