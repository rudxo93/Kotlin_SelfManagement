package com.duran.selfmg.ui.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.duran.selfmg.R
import com.duran.selfmg.databinding.FragmentPlanBinding
import com.duran.selfmg.ui.adapter.PlanFragmentPagerAdapter
import com.duran.selfmg.util.ZoomOutPageTransformer
import com.google.android.material.tabs.TabLayoutMediator

class PlanFragment : Fragment() {

    private lateinit var binding: FragmentPlanBinding

    // TabLayout에  들어갈 title 리스트
    private val tabTitle = listOf(
        R.string.item_plan_navigationView_todoList,
        R.string.item_plan_navigationView_memo,
        R.string.item_plan_navigationView_schedule
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_plan, container, false)

        initTablayout() // 탭 레리아웃과 뷰페이저 기능

        return binding.root
    }

    // ======================================= 탭 레이아웃과 뷰페이저 기능 =======================================
    private fun initTablayout() {
        binding.planViewpager.apply {
            adapter = PlanFragmentPagerAdapter(context as FragmentActivity)
            setPageTransformer(ZoomOutPageTransformer()) // viewpagerTransFormer 효과
        }

        TabLayoutMediator(binding.planTabs, binding.planViewpager) {
                tab, position ->
            tab.setText(tabTitle[position])
        }.attach()
    }



}