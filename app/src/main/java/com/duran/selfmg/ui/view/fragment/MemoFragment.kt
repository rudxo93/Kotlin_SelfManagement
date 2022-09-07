package com.duran.selfmg.ui.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.duran.selfmg.R
import com.duran.selfmg.data.model.MemoListEntity
import com.duran.selfmg.databinding.FragmentMemoBinding
import com.duran.selfmg.ui.adapter.MemoListAdapter
import com.duran.selfmg.ui.viewmodel.MemoListViewModel
import com.duran.selfmg.ui.viewmodel.TodoListVIewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.text.SimpleDateFormat

class MemoFragment : Fragment() {

    private lateinit var binding: FragmentMemoBinding

    private lateinit var memoViewModel: MemoListViewModel
    lateinit var memoListAdapter: MemoListAdapter

    private val bottomSheet by lazy { binding.memoBottomSheet }
    private val memoListRecyclerView by lazy { binding.rvMemolist }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_memo, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        memoViewModel = ViewModelProvider(this)[MemoListViewModel::class.java]

        initRecyclerViewSetting()
        initBottomBehaviorEvent()

    }

    //
    private fun initRecyclerViewSetting() {
        memoViewModel.memoList.observe(viewLifecycleOwner) {
            memoListAdapter.update(it)
        }

        memoListAdapter = context?.let { MemoListAdapter(it) }!!
        memoListRecyclerView.layoutManager = GridLayoutManager(context, 2)
        memoListRecyclerView.adapter = memoListAdapter
    }

    // ======================================= Bottom Sheet Behavior =======================================
    @SuppressLint("SimpleDateFormat")
    private fun initBottomBehaviorEvent() {
        val bottomBehavior = BottomSheetBehavior.from(bottomSheet.root)

        bottomBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        bottomBehavior.apply {
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_COLLAPSED -> { // 접힘 -> 키패드 숨기기
                            initKeyboardEvent()
                        }
                        BottomSheetBehavior.STATE_EXPANDED -> {} // 펼쳐짐
                        BottomSheetBehavior.STATE_HIDDEN -> {} //숨겨짐
                        BottomSheetBehavior.STATE_HALF_EXPANDED -> {} //절반 펼쳐짐
                        BottomSheetBehavior.STATE_DRAGGING -> {}  //드래그하는 중
                        BottomSheetBehavior.STATE_SETTLING -> {}  //(움직이다가) 안정화되는 중
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) { // 화살표 전환
                    //슬라이드 될때 offset / hide -1.0 ~ collapsed 0.0 ~ expended 1.0
                    binding.memoBottomSheet.apply {
                        tvBottomWriteMemoArrow.rotation = slideOffset * -180F
                    }
                }
            })
        }

        // ======================================= 메모 작성하기 클릭 =======================================
        bottomSheet.btnBottomWriteMemo.setOnClickListener {
            bottomBehavior.state =
                if (bottomBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) { // bottomBehavior상태가 접힌 상태라면
                    BottomSheetBehavior.STATE_EXPANDED // 펼쳐준다.
                } else
                    BottomSheetBehavior.STATE_COLLAPSED // 아니라면 -> 펼쳐져있다면 접어준다.
                    // 메모 제목과 내용 지워주기
                    binding.memoBottomSheet.edWriteMemoContent.text.clear()
                    binding.memoBottomSheet.edWriteMemoTitle.text.clear()
                }

        // ======================================= 메모 저장 클릭 =======================================
        bottomSheet.tvBtnWriteMemoSave.setOnClickListener {
            val memoTitle = bottomSheet.edWriteMemoTitle.text
            val memoContent = bottomSheet.edWriteMemoContent.text
            val saveDate = SimpleDateFormat("yyyy년 M월 d일").format(System.currentTimeMillis())
            // 타이틀이 비었다면 임의의 타이틀로 저장
            val emptyTitledDate = SimpleDateFormat("MMdd").format(System.currentTimeMillis())
            val emptyTitle = "텍스트 노트 $emptyTitledDate"
            // 메모 conetent는 필수
            if(bottomSheet.edWriteMemoContent.text.isNotEmpty()){
                // 타이틀이 비었다면
                if(bottomSheet.edWriteMemoTitle.text.isEmpty()) {
                    val memoListEntity = MemoListEntity(0, emptyTitle, memoContent.toString(), saveDate, false)
                    memoViewModel.memoInsert(memoListEntity)
                    bottomBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    Toast.makeText(context, "메모가 저장되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    val memoListEntity = MemoListEntity(0, memoTitle.toString(), memoContent.toString(), saveDate, false)
                    memoViewModel.memoInsert(memoListEntity)
                    bottomBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    Toast.makeText(context, "메모가 저장되었습니다.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "메모가 비어있습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // ======================================= 메모 취소 클릭 =======================================
        bottomSheet.tvBtnWriteMemoCancel.setOnClickListener {
            bottomBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            Toast.makeText(context, "취소", Toast.LENGTH_SHORT).show()
        }
    }

    // ======================================= KeyBoard Down Event =======================================
    private fun initKeyboardEvent() {
        view?.let { activity?.hideKeyboard(it) }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}