package com.duran.selfmg.ui.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import com.duran.selfmg.R
import com.duran.selfmg.databinding.FragmentScheduleBinding
import com.duran.selfmg.ui.view.activity.LoginActivity

class ScheduleFragment : Fragment() {

    lateinit var binding: FragmentScheduleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule, container, false)

        initToolBarSetting()

        return binding.root
    }


    // ======================================= 일정 시간표 툴바 =======================================
    private fun initToolBarSetting() {
        val toolbar = binding.toolbarSchedule
        toolbar.inflateMenu(R.menu.item_toolbar_schedule)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.item_toolbar_schedule, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


}