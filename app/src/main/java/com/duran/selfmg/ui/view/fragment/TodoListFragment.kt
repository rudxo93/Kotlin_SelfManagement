package com.duran.selfmg.ui.view.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.duran.selfmg.R
import com.duran.selfmg.databinding.FragmentTodoListBinding
import com.duran.selfmg.ui.adapter.TodoListAdapter
import com.duran.selfmg.ui.viewmodel.TodoListVIewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoListFragment : Fragment() {

    private lateinit var binding: FragmentTodoListBinding

    private val btnAddTodoList by lazy { binding.btnAddTravelPlan }
    private val todoListRecyclerView by lazy { binding.rvTodolist }


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
        btnAddTodoList.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("type", "Add") // add 버튼 클릭 -> Dialog로 type을 add로 넘긴다.

            val dialog = DialogAddTodoFragment()
            dialog.arguments = bundle
            childFragmentManager.beginTransaction()
            dialog.show(childFragmentManager, "TodoListDialog")

        }
    }


    // ======================================= Todo리사이클러뷰 =======================================
    private fun initRecyclerViewSetting() {
        todoViewModel.todoList.observe(viewLifecycleOwner) {
            todoListAdapter.update(it)
        }

        todoListAdapter = context?.let { TodoListAdapter(it) }!!
        todoListRecyclerView.layoutManager = LinearLayoutManager(context)
        todoListRecyclerView.adapter = todoListAdapter

        // todoContent 클릭
        todoListAdapter.setItemClickListener(object : TodoListAdapter.ItemClickListener {
            override fun onClick(view: View, position: Int, itemId: Long) {
                CoroutineScope(Dispatchers.IO).launch {
                    val todo = todoViewModel.getTodo(itemId) // 해당 게시글 id로 조회
                    val bundle = Bundle()
                    bundle.putString("type", "Update") // 해당 게시글 클릭 -> Dialog로 type을 update로 넘긴다.
                    bundle.putSerializable("item", todo) // 조회된 해당 게시글을 object로 넘긴다.

                    val dialog = DialogAddTodoFragment()
                    dialog.arguments = bundle
                    childFragmentManager.beginTransaction()
                    dialog.show(childFragmentManager, "TodoListDialog")
                }

            }

        })

        // todoChekcBox클릭 이벤트
        todoListAdapter.setItemCheckBoxClickListener(object : TodoListAdapter.ItemCheckBoxClickListener {
            override fun onClick(view: View, position: Int, itemId: Long) {
                CoroutineScope(Dispatchers.IO).launch {
                    val todoIsChecked = todoViewModel.getTodo(itemId)
                    todoIsChecked.isChecked = !todoIsChecked.isChecked // checked 토글
                    todoViewModel.todoUpdate(todoIsChecked)
                }
            }
        })

        // todo의 삭제 이미지 클릭 이벤트
        todoListAdapter.setItemDeleteImageClickListener(object : TodoListAdapter.ItemDeleteImageClickListener {
            override fun onClick(view: View, position: Int, itemId: Long) {
                Toast.makeText(context, "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                CoroutineScope(Dispatchers.IO).launch {
                    todoViewModel.todoDelete(itemId)
                }
            }
        })
    }

}

