package com.duran.selfmg.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.duran.selfmg.ui.view.fragment.MemoFragment
import com.duran.selfmg.ui.view.fragment.PlanFragment
import com.duran.selfmg.ui.view.fragment.ScheduleFragment
import com.duran.selfmg.ui.view.fragment.TodoListFragment

class PlanFragmentPagerAdapter(fa: FragmentActivity): FragmentStateAdapter(fa) {

    private val PAGES_NUM = 3

    override fun getItemCount(): Int = PAGES_NUM

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> TodoListFragment()
            1 -> MemoFragment()
            else -> ScheduleFragment()
        }
    }
}