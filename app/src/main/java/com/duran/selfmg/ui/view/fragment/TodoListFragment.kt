package com.duran.selfmg.ui.view.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.duran.selfmg.R
import com.duran.selfmg.databinding.FragmentTodoListBinding
import com.duran.selfmg.ui.view.activity.LoginActivity

class TodoListFragment : Fragment() {

    private lateinit var binding: FragmentTodoListBinding

    private val btnAddTodoList by lazy { binding.btnAddTravelPlan }

    /*private lateinit var todoViewModel: TodoListVIewModel
    lateinit var todoListAdapter: TodoListAdapter*/

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

        /*todoViewModel = ViewModelProvider(this)[TodoListVIewModel::class.java]*/

        /*todoViewModel.getAllTodoList()*/

        initBtnAddTodoList() // 할일 추가하기 버튼
        /*initRecyclerViewSetting()*/
    }

    // ======================================= 할일 추가하기 버튼 클릭 =======================================
    private fun initBtnAddTodoList() {
        btnAddTodoList.setOnClickListener{
            val addTodoDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_todo, null)
            val addTodoBuilder = AlertDialog.Builder(context)
                .setView(addTodoDialogView)
                .setTitle("할 일 추가하기")

            addTodoBuilder.show()
        }
    }

    // ======================================= Todo리사이클러뷰 =======================================
 /*   private fun initRecyclerViewSetting() {
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
    }*/


}