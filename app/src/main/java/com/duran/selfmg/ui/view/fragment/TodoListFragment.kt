package com.duran.selfmg.ui.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.duran.selfmg.R
import com.duran.selfmg.databinding.FragmentTodoListBinding
import com.duran.selfmg.ui.view.activity.WriteTodoListActivity

class TodoListFragment : Fragment() {

    private lateinit var binding: FragmentTodoListBinding

    private val btnAddTodoList by lazy { binding.btnAddTravelPlan }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_todo_list, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initBtnAddTodoList()
    }

    // ======================================= 할일 추가하기 버튼 클릭 =======================================
    private fun initBtnAddTodoList(){
        btnAddTodoList.setOnClickListener {
            activity.let {
                val intent = Intent(context, WriteTodoListActivity::class.java)
                startActivity(intent)
            }
        }
    }

}