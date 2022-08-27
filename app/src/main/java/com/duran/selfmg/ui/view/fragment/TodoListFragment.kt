package com.duran.selfmg.ui.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.duran.selfmg.R
import com.duran.selfmg.databinding.FragmentTodoListBinding

class TodoListFragment : Fragment() {

    private lateinit var binding: FragmentTodoListBinding

    private val btnAddTodoList by lazy { binding.btnAddTravelPlan }
    private val addTodoListLayout by lazy { binding.linearAddTodoListView }
    private val btnTodoListCancel by lazy { binding.btnTodoListCancel }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_todo_list, container, false)

        initBtnAddTodoList() // 할일 추가하기 버튼
        initBtnAddTodoListCancel() // 할일 추가하기 -> 취소하기 버튼

        return binding.root
    }

    // ======================================= 할일 추가하기 버튼 클릭 =======================================
    private fun initBtnAddTodoList(){
        btnAddTodoList.setOnClickListener {
            btnAddTodoList.isVisible = false
            addTodoListLayout.isVisible = true
        }
        /*btnAddTodoList.setOnClickListener {
            val intent = Intent(context, WriteTodoListActivity::class.java)
            startActivity(intent)
        }*/
    }

    // ======================================= 할일 추가하기에서 취소하기 버튼 클릭 =======================================
    private fun initBtnAddTodoListCancel() {
        btnTodoListCancel.setOnClickListener {
            btnAddTodoList.isVisible = true
            addTodoListLayout.isVisible = false
        }
    }
}