package com.duran.selfmg.ui.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.duran.selfmg.ui.view.activity.MemoWriteActivity
import com.duran.selfmg.ui.viewmodel.MemoListViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class MemoFragment : Fragment() {

    private lateinit var binding: FragmentMemoBinding

    private lateinit var memoViewModel: MemoListViewModel
    lateinit var memoListAdapter: MemoListAdapter

    /*private val bottomSheet by lazy { binding.memoBottomSheet }*/
    private val memoListRecyclerView by lazy { binding.rvMemolist }
    private val btnAddMemo by lazy { binding.btnAddMemoList }

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

        initRecyclerViewSetting() // RecyclerView
        initBtnAddMemo()
        /*initBottomBehaviorEvent() // Bottom Sheet*/

    }

    // ======================================= Memo RecyclerView Setting =======================================
    private fun initRecyclerViewSetting() {
        memoViewModel.memoList.observe(viewLifecycleOwner) {
            memoListAdapter.update(it)
        }

        memoListAdapter = context?.let { MemoListAdapter(it) }!!
        memoListRecyclerView.layoutManager = GridLayoutManager(context, 2)
        memoListRecyclerView.adapter = memoListAdapter

        memoListAdapter.setItemClickListener(object : MemoListAdapter.ItemClickListener {
            override fun onClick(view: View, position: Int, itemId: Long) {
                CoroutineScope(Dispatchers.IO).launch {
                    val memo = memoViewModel.getMemo(itemId)
                    val intent = Intent(activity, MemoWriteActivity::class.java)
                    intent.putExtra("type", "Update")
                    intent.putExtra("item", memo)
                    startActivity(intent)
                }
            }
        })

        memoListAdapter.setItemCheckBoxClickListener(object : MemoListAdapter.ItemCheckBoxClickListener {
            override fun onClick(view: View, position: Int, itemId: Long) {
                CoroutineScope(Dispatchers.IO).launch {
                    val memoIsChecked = memoViewModel.getMemo(itemId)
                    memoIsChecked.isChecked = !memoIsChecked.isChecked // 토글
                    memoViewModel.memoUpdate(memoIsChecked)
                }
            }
        })
    }

    // ======================================= Add Memo Button Click =======================================
    private fun initBtnAddMemo() {
        btnAddMemo.setOnClickListener {
            val intent = Intent(activity, MemoWriteActivity::class.java)
            intent.putExtra("type", "Add")
            startActivity(intent)
        }
    }

   /* // ======================================= Bottom Sheet Behavior =======================================
    @SuppressLint("SimpleDateFormat")
    private fun initBottomBehaviorEvent() {
        val bottomBehavior = BottomSheetBehavior.from(bottomSheet.root)

        bottomBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        bottomBehavior.apply {
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_COLLAPSED -> { // 접힘 -> 키패드 숨기기
                            // keyboard down
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


    }*/

}