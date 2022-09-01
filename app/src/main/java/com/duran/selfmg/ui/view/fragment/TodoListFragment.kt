package com.duran.selfmg.ui.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.duran.selfmg.R
import com.duran.selfmg.databinding.FragmentTodoListBinding
import com.duran.selfmg.ui.adapter.TodoListAdapter
import com.duran.selfmg.ui.viewmodel.TodoListVIewModel

class TodoListFragment : Fragment() {

    private lateinit var binding: FragmentTodoListBinding

    private val btnAddTodoList by lazy { binding.btnAddTravelPlan }

    private lateinit var todoViewModel: TodoListVIewModel
    lateinit var todoListAdapter: TodoListAdapter

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        todoViewModel = ViewModelProvider(this)[TodoListVIewModel::class.java]

        initBtnAddTodoList() // 할일 추가하기 버튼
        initRecyclerViewSetting()
    }

    // ======================================= Add 버튼 클릭 =======================================
    private fun initBtnAddTodoList() {
        btnAddTodoList.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("type", "Add")

            val dialog = DialogAddTodoFragment()
            dialog.arguments = bundle
            parentFragmentManager.beginTransaction()
            dialog.show(childFragmentManager, "TodoListDialog")

        }
    }


    // ======================================= Todo리사이클러뷰 =======================================
    private fun initRecyclerViewSetting() {
        todoViewModel.todoList.observe(viewLifecycleOwner) {
            todoListAdapter.update(it)
        }

        todoListAdapter = context?.let { TodoListAdapter(it) }!!
        binding.rvTodolist.layoutManager = LinearLayoutManager(context)
        binding.rvTodolist.adapter = todoListAdapter

        // todoContent 클릭
        todoListAdapter.setItemClickListener(object : TodoListAdapter.ItemClickListener {
            override fun onClick(view: View, position: Int, itemId: Long) {
                Log.e("tag", "$itemId")
                initUpdateTodo(itemId) // position의 위치에 있는 게시글을 업데이트 해야한다 , update의 구분값을 같이 넘겨준다.
            }

        })

        // todoChekcBox클릭
        todoListAdapter.setItemCheckBoxClickListener(object : TodoListAdapter.ItemCheckBoxClickListener {
            override fun onClick(view: View, position: Int, itemId: Long) {

            }
        })
    }

    // ======================================= 게시글 클릭 시 게시글 update =======================================
    private fun initUpdateTodo(itemId: Long) {
        val bundle = Bundle()
        bundle.putString("type", "Update")
        bundle.putString("itemId", "$itemId")

        val dialog = DialogAddTodoFragment()
        dialog.arguments = bundle
        parentFragmentManager.beginTransaction()
        dialog.show(childFragmentManager, "TodoListDialog")

    }


}