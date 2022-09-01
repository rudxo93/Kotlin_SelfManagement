package com.duran.selfmg.ui.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.duran.selfmg.R
import com.duran.selfmg.data.model.TodoListEntity
import com.duran.selfmg.databinding.FragmentTodoListBinding
import com.duran.selfmg.ui.adapter.TodoListAdapter
import com.duran.selfmg.ui.viewmodel.TodoListVIewModel
import kotlinx.coroutines.*
import java.text.SimpleDateFormat

class TodoListFragment : Fragment() {

    private lateinit var binding: FragmentTodoListBinding

    private val btnAddTodoList by lazy { binding.btnAddTravelPlan }

    private lateinit var todoViewModel: TodoListVIewModel
    lateinit var todoListAdapter: TodoListAdapter

    private var todoList: TodoListEntity? = null

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
            /*val transaction = childFragmentManager.beginTransaction()*/
            val dialog = DialogAddTodoFragment()
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
                Toast.makeText(context, "$itemId", Toast.LENGTH_SHORT).show()
                Log.e("tag", "$itemId")
                CoroutineScope(Dispatchers.IO).launch {
                    val todo = todoViewModel.getTodo(itemId)
                    val args = Bundle()
                    initUpdateTodoList()
                }
            }

        })

        // todoChekcBox클릭
        todoListAdapter.setItemCheckBoxClickListener(object : TodoListAdapter.ItemCheckBoxClickListener {
            override fun onClick(view: View, position: Int, itemId: Long) {
                CoroutineScope(Dispatchers.IO ).launch {

                }
            }
        })
    }

    // ======================================= Todo클릭 -> TodoUpdate =======================================
    private fun initUpdateTodoList() {

    }


}