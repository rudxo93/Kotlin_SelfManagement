package com.duran.selfmg.ui.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.duran.selfmg.R
import com.duran.selfmg.data.entity.TodoListEntity
import com.duran.selfmg.databinding.FragmentTodoListBinding
import com.duran.selfmg.ui.adapter.TodoListAdapter
import com.duran.selfmg.ui.viewmodel.TodoListVIewModel
import java.text.SimpleDateFormat

class TodoListFragment : Fragment() {

    private lateinit var binding: FragmentTodoListBinding

    private val btnAddTodoList by lazy { binding.btnAddTravelPlan }
    private val addTodoListLayout by lazy { binding.linearAddTodoListView }
    private val btnTodoListCancel by lazy { binding.btnTodoListCancel }
    private val btnAddTodo by lazy { binding.btnTodoListAdd }
    private val todoContent by lazy { binding.edAddTodoListContent }

    lateinit var todoViewModel: TodoListVIewModel
    lateinit var todoListAdapter: TodoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_todo_list, container, false)

        todoViewModel = ViewModelProvider(this).get(TodoListVIewModel::class.java)

        todoViewModel.getAllTodoList()

        initBtnAddTodoList() // 할일 추가하기 버튼
        initBtnAddTodoListCancel() // 할일 추가하기 -> 취소하기 버튼
        initBtnAddTodoInsert() // 할 일 추가하기에서 추가하기 버튼 클릭 -> DB에 저장하기
        initRecyclerViewSetting()


        return binding.root
    }

    // ======================================= 할일 추가하기 버튼 클릭 =======================================
    private fun initBtnAddTodoList() {
        btnAddTodoList.setOnClickListener {
            btnAddTodoList.isVisible = false
            addTodoListLayout.isVisible = true
        }
    }

    // ======================================= 할일 추가하기에서 취소하기 버튼 클릭 =======================================
    private fun initBtnAddTodoListCancel() {
        btnTodoListCancel.setOnClickListener {
            btnAddTodoList.isVisible = true
            addTodoListLayout.isVisible = false
        }
    }

    // ======================================= 할일 추가하기 버튼 클릭 -> DB에 저장하기 =======================================
    private fun initBtnAddTodoInsert() {
        btnAddTodo.setOnClickListener {
            val content = todoContent.text.toString()
            val currentDate =
                SimpleDateFormat("yyyy-MM-dd HH:mm").format(System.currentTimeMillis())

            if (todoContent.text.isNotEmpty()) {
                val todoListDao = TodoListEntity(0, content, currentDate, false)
                todoViewModel.todoInsert(todoListDao)
                Toast.makeText(context, "할 일이 추가되었습니다.", Toast.LENGTH_SHORT).show()
                todoContent.text.clear()
                // 키패드 내리기
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view?.windowToken, 0)
                addTodoListLayout.isVisible = false // 할일 추가하기 레이아웃 숨기기
                btnAddTodoList.isVisible = true // add버튼 보이게 하기
            } else {
                Toast.makeText(context, "할 일을 작성 후 추가해주세요.", Toast.LENGTH_SHORT).show()
            }


        }
    }

    // ======================================= Todo리사이클러뷰 =======================================
    private fun initRecyclerViewSetting() {
        todoViewModel.todoList.observe(viewLifecycleOwner, Observer {
            todoListAdapter = TodoListAdapter(it)
            binding.rvTodolist.adapter = todoListAdapter
            binding.rvTodolist.layoutManager = LinearLayoutManager(context)

        })
    }

}