package com.duran.selfmg.ui.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.duran.selfmg.R
import com.duran.selfmg.databinding.FragmentMemoBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MemoFragment : Fragment() {

    private lateinit var binding : FragmentMemoBinding


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

        initBottomBehaviorEvent()

    }

    // ======================================= Bottom Sheet Behavior =======================================
    private fun initBottomBehaviorEvent() {
        val bottomBehavior = BottomSheetBehavior.from(binding.memoBottomSheet.root)

        bottomBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        bottomBehavior.apply {
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    /*when(newState) {
                        BottomSheetBehavior.STATE_COLLAPSED -> {} // 접힘
                        BottomSheetBehavior.STATE_EXPANDED -> {} // 펼쳐짐
                        BottomSheetBehavior.STATE_HIDDEN -> {}    //숨겨짐
                        BottomSheetBehavior.STATE_HALF_EXPANDED -> {} //절반 펼쳐짐
                        BottomSheetBehavior.STATE_DRAGGING -> {}  //드래그하는 중
                        BottomSheetBehavior.STATE_SETTLING -> {}  //(움직이다가) 안정화되는 중
                    }*/
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) { // 화살표 전환
                    //슬라이드 될때 offset / hide -1.0 ~ collapsed 0.0 ~ expended 1.0
                    binding.memoBottomSheet.apply {
                        tvBottomWriteMemoArrow.rotation = slideOffset * -180F
                    }
                }
            })
        }

        binding.memoBottomSheet.btnBottomWriteMemo.setOnClickListener {
            bottomBehavior.state = if (bottomBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) { // bottomBehavior상태가 접힌 상태라면
                BottomSheetBehavior.STATE_EXPANDED // 펼쳐준다.
            } else
                BottomSheetBehavior.STATE_COLLAPSED // 아니라면 -> 펼쳐져있다면 접어준다.
        }
    }



}