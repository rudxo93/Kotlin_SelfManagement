package com.duran.selfmg.ui.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.duran.selfmg.R
import com.duran.selfmg.data.model.TodoListEntity
import com.duran.selfmg.databinding.FragmentDialogAddTodoBinding
import com.duran.selfmg.ui.viewmodel.TodoListVIewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class DialogAddTodoFragment : DialogFragment() {

    private lateinit var binding: FragmentDialogAddTodoBinding

    private val tvTitle by lazy { binding.tvAddTodoTitle }
    private val editContent by lazy { binding.edAddTodoListContent}
    private val btnSave by lazy { binding.btnAddTodoSave }
    private val btnCancel by lazy { binding.btnAddTodoCancel }

    private lateinit var todoViewModel: TodoListVIewModel
    private var todo: TodoListEntity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dialog_add_todo, container, false)

        todoViewModel = ViewModelProvider(this)[TodoListVIewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val resultType = arguments?.getString("type")

        initTypeSetting(resultType) // type에 따른 버튼 셋팅
        initBtnCancel() // 취소하기 버튼 클릭
    }

    // ======================================= type에 따른 버튼 셋팅 =======================================
    private fun initTypeSetting(resultType: String?) {
        if(resultType == "Add") {
            btnSave.text = "저장하기"
            tvTitle.text = "할 일 추가하기"
            initBtnSave()
        } else if(resultType == "Update") {
            todo = arguments?.getSerializable("item") as TodoListEntity // 해당 게시글
            editContent.setHint(todo!!.todocontent)
            btnSave.text = "수정하기"
            tvTitle.text = "할 일 수정하기"
            initBtnUpdate()
        }
    }

    // ======================================= Add 저장하기 버튼 클릭 =======================================
    @SuppressLint("SimpleDateFormat")
    private fun initBtnSave() {
        btnSave.setOnClickListener {
            val content = editContent.text
            val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm").format(System.currentTimeMillis())
            if(content.isNotEmpty()) { // 할 일이 작성되어 있다.
                // insert
                val todoListEntity = TodoListEntity(0, content.toString(), currentDate, false)
                todoViewModel.todoInsert(todoListEntity)
                Toast.makeText(context, "할 일을 저장했습니다.", Toast.LENGTH_SHORT).show()
                dismiss()
            } else {
                Toast.makeText(context, "할 일을 작성후 저장하기를 눌러주세요.", Toast.LENGTH_SHORT).show()
            }

        }
    }

    // ======================================= Update 저장하기 버튼 클릭 =======================================
    @SuppressLint("SimpleDateFormat")
    private fun initBtnUpdate() {
        btnSave.setOnClickListener {
            val updateContent = editContent.text
            val updateDate = SimpleDateFormat("yyyy-MM-dd HH:mm").format(System.currentTimeMillis())

            if(updateContent.isNotEmpty()) { // 수정할 할 일을 작성했다면
                val updateTodo = TodoListEntity(todo!!.id, updateContent.toString(), updateDate, todo!!.isChecked)

                CoroutineScope(Dispatchers.IO).launch {
                    todoViewModel.todoUpdate(updateTodo)
                }
                Toast.makeText(context, "할 일이 변경되었습니다.", Toast.LENGTH_SHORT).show()
                dismiss()
            } else {
                Toast.makeText(context, "할 일을 작성후 수정하기를 눌러주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ======================================= 취소하기 버튼 클릭 =======================================
    private fun initBtnCancel() {
        btnCancel.setOnClickListener {
            Toast.makeText(context, "할 일을 취소했습니다.", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }

}