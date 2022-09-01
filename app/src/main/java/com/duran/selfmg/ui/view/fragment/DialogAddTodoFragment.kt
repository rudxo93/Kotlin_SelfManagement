package com.duran.selfmg.ui.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.duran.selfmg.R
import com.duran.selfmg.databinding.FragmentDialogAddTodoBinding

class DialogAddTodoFragment : DialogFragment() {

    private lateinit var binding: FragmentDialogAddTodoBinding

    private val tvTitle by lazy { binding.tvAddTodoTitle }
    private val editContent by lazy { binding.edAddTodoListContent}
    private val btnSave by lazy { binding.btnAddTodoSave }
    private val btnCancel by lazy { binding.btnAddTodoCancel }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dialog_add_todo, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val resultType = arguments?.getString("type")
        val resultItemId = arguments?.getString("itemId")

        initTypeSetting(resultType) // type에 따른 버튼 셋팅
        initBtnSave() // 저장하기 버튼 클릭
        initBtnCancel() // 취소하기 버튼 클릭
    }

    // ======================================= type에 따른 버튼 셋팅 =======================================
    private fun initTypeSetting(resultType: String?) {
        if(resultType == "Add") {
            btnSave.setText("저장하기")
            tvTitle.setText("할 일 추가하기")
        } else if(resultType == "Update") {
            btnSave.setText("수정하기")
            tvTitle.setText("할 일 수정하기")
        }
    }

    // ======================================= 저장하기 버튼 클릭 =======================================
    private fun initBtnSave() {
        btnSave.setOnClickListener {
            Toast.makeText(context, "할 일을 저장했습니다.", Toast.LENGTH_SHORT).show()
            dismiss()
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