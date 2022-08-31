package com.duran.selfmg.ui.view.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
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
import com.duran.selfmg.ui.view.activity.LoginActivity
import com.duran.selfmg.ui.viewmodel.TodoListVIewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

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
            val addTodoDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_todo, null)
            val addTodoBuilder = AlertDialog.Builder(context)
                .setView(addTodoDialogView)
                .setTitle("할 일 추가하기")

            val addTodoDialog = addTodoBuilder.show()

            val btnSave = addTodoDialogView.findViewById<Button>(R.id.btn_add_todo_save)
            btnSave.setOnClickListener {
                val content = addTodoDialogView.findViewById<EditText>(R.id.ed_add_todoList_content)
                val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm").format(System.currentTimeMillis())

                if (content.text.isNotEmpty()) { // 할 일이 작성되어 있음
                    val todoListDao = TodoListEntity(0, content.text.toString(), currentDate, false)
                    todoViewModel.todoInsert(todoListDao)
                    Toast.makeText(context, "저장되었습니다.", Toast.LENGTH_SHORT).show()
                    addTodoDialog.dismiss()
                } else {
                    Toast.makeText(context, "할 일을 작성후 저장하기를 눌러주세요.", Toast.LENGTH_SHORT).show()
                }
            }

            val btnCancel = addTodoDialogView.findViewById<Button>(R.id.btn_add_todo_cancel)
            btnCancel.setOnClickListener {
                Toast.makeText(context, "할 일 추가하기가 취소되었습니다.", Toast.LENGTH_SHORT).show()
                addTodoDialog.dismiss()
            }
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

        todoListAdapter.setItemClickListener(object : TodoListAdapter.ItemClickListener {
            override fun onClick(view: View, position: Int, itemId: Long) {
                Toast.makeText(context, "$itemId", Toast.LENGTH_SHORT).show()
                Log.e("tag", "$itemId")
                val todo = todoViewModel.getTodo(itemId)
                Log.e("tag", "${todo.isActive}")
            }

        })

        todoListAdapter.setItemCheckBoxClickListener(object : TodoListAdapter.ItemCheckBoxClickListener {
            override fun onClick(view: View, position: Int, itemId: Long) {
                CoroutineScope(Dispatchers.IO).launch {
                    val todo = todoViewModel.getTodo(itemId)
                }
            }
        })
    }


}