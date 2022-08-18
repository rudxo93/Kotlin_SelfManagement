package com.duran.selfmg.ui.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.duran.selfmg.R
import com.duran.selfmg.databinding.FragmentMainBinding
import com.duran.selfmg.ui.view.MainActivity

class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding
    // Fragment
    private val currentFragment by lazy { CurrentFragment() }
    private val healthFragment by lazy { HealthFragment() }
    private val planFragment by lazy { PlanFragment() }
    private val settingFragment by lazy { SettingFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)

        // 기본적으로 보여줄 fragment
        initBaseFragment()
        // 하단 navigation 이동
        initNavigationBar()

        return binding.root
    }

    // =======================================Base로 보여줄 Fragment=======================================
    private fun initBaseFragment() {
        replaceFragment(currentFragment)
    }
    // =======================================Base로 보여줄 Fragment=======================================

    // =======================================하단 Navigation 이동=======================================
    private fun initNavigationBar() {
        binding.navigationView.run {
            setOnNavigationItemSelectedListener {
                when(it.itemId) {
                    R.id.current -> {
                        replaceFragment(currentFragment)
                    }
                    R.id.myHealth -> {
                        replaceFragment(healthFragment)
                    }
                    R.id.myPlan -> {
                        replaceFragment(planFragment)
                    }
                    R.id.setting -> {
                        replaceFragment(settingFragment)
                    }
                }
                true
            }
        }
    }
    // =======================================하단 Navigation 이동=======================================

    // =======================================FRagment 전환=======================================
    private fun replaceFragment(fragment: Fragment){
        childFragmentManager
            .beginTransaction()
            .replace(R.id.fm_container, fragment)
            .addToBackStack(null)
            .commit()
    }
    // =======================================FRagment 전환=======================================

}