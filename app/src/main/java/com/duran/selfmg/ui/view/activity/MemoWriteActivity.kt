package com.duran.selfmg.ui.view.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.duran.selfmg.R
import com.duran.selfmg.data.model.MemoListEntity
import com.duran.selfmg.databinding.ActivityMemoWriteBinding
import com.duran.selfmg.ui.viewmodel.MemoListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class MemoWriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMemoWriteBinding
    private lateinit var memoViewModel: MemoListViewModel

    private val writeMemoTitle by lazy { binding.edWriteMemoTitle }
    private val writeMemoContent by lazy { binding.edWriteMemoContent }
    private val btnSave by lazy { binding.btnWriteMemoSave }
    private val btnCancel by lazy { binding.btnWriteMemoCancel }

    private var memo: MemoListEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_memo_write)

        memoViewModel = ViewModelProvider(this)[MemoListViewModel::class.java]

        val result = intent.getStringExtra("type")
        initBtnChangeText(result)
        initBtnCancel()

    }

    // ======================================= type에 따른 버튼 Text Change =======================================
    private fun initBtnChangeText(result: String?) {
        if(result == "Add") {
            btnSave.text = "저장"
            initBtnSave()
        } else {
            memo = intent.getSerializableExtra("item") as MemoListEntity?
            writeMemoTitle.setText(memo!!.memoTitle)
            writeMemoContent.setText(memo!!.memoContent)
            btnSave.text = "변경"
            initBtnUpdate()
        }
    }

    // ======================================= Save Btn 클릭 =======================================
    @SuppressLint("SimpleDateFormat")
    private fun initBtnSave() {
        btnSave.setOnClickListener {
            val memoTitle = writeMemoTitle.text
            val memoContent = writeMemoContent.text
            val saveDate = SimpleDateFormat("yyyy년 M월 d일").format(System.currentTimeMillis())
            // 타이틀이 비었다면 임의의 타이틀로 저장
            val emptyTitledDate = SimpleDateFormat("MMdd").format(System.currentTimeMillis())
            val emptyTitle = "텍스트 노트 $emptyTitledDate"
            if(memoContent.isNotEmpty()){ // 컨텐츠가 비어있다면 -> 완료 x
                if(memoTitle.isEmpty()) { // 타이틀이 비었다면 -> 임의의 제목 부여( 텍스트 노트 저장날짜 )
                    val memoListEntity = MemoListEntity(0, emptyTitle, memoContent.toString(), saveDate, false)
                    memoViewModel.memoInsert(memoListEntity)
                    Toast.makeText(this, "메모가 저장되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                } else { // 타이틀이 존재한다면 -> 타이틀로 저장
                    val memoListEntity = MemoListEntity(0, memoTitle.toString(), memoContent.toString(), saveDate, false)
                    memoViewModel.memoInsert(memoListEntity)
                    Toast.makeText(this, "메모가 저장되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                Toast.makeText(this, "메모가 비어있습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ======================================= Update Btn 클릭 =======================================
    private fun initBtnUpdate() {
        btnSave.setOnClickListener {
            val updateTitle = writeMemoTitle.text
            val updateContent = writeMemoContent.text
            val updateDate = SimpleDateFormat("yyyy년 M월 d일").format(System.currentTimeMillis())
            if(updateContent.isNotEmpty()) { // 메모를 수정했다면
                val updateMemo = MemoListEntity(memo!!.id, updateTitle.toString(), updateContent.toString(), updateDate, memo!!.isChecked)
                CoroutineScope(Dispatchers.IO).launch {
                    memoViewModel.memoUpdate(updateMemo)
                }
                Toast.makeText(this, "메모가 수정되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            } else if(updateContent.isEmpty()) {
                Toast.makeText(this, "메모가 비어있습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ======================================= Cancel 버튼 클릭 =======================================
    private fun initBtnCancel() {
        btnCancel.setOnClickListener {
            Toast.makeText(this, "작성하기를 취소했습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}