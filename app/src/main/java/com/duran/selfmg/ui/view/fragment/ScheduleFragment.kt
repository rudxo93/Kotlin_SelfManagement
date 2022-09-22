package com.duran.selfmg.ui.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.duran.selfmg.R
import com.duran.selfmg.databinding.FragmentScheduleBinding
import com.duran.selfmg.ui.view.activity.AddScheduleActivity
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.*

class ScheduleFragment : Fragment() {

    private lateinit var binding: FragmentScheduleBinding

    private val calendar by lazy { binding.calendarView } // 캘린더 뷰
    private val selectDate by lazy { binding.tvAddScheduleSelectDate } // 선택한 날짜 보여주기
    private val scheduleContent by lazy { binding.tvScheduleContent } // 선택한 날짜에 저장된 content
    private val addScheduleContent by lazy { binding.etAddScheduleContent } // 선택한 날짜에 content 작성
    private val updateBtnLayout by lazy { binding.buttonLinearUpdate } // update시 보여줄 버튼 layout
    private val btnUpdateSchedule by lazy { binding.btnScheduleUpeate } // 수정하기 버튼
    private val btnDeleteSchedule by lazy { binding.btnScheduleDelete } // 삭제하기 버튼
    private val insertBtnLayout by lazy { binding.buttonLinearInsert } // insert시 보여줄 버튼 layout
    private val btnInsertSchedule by lazy { binding.btnScheduleInsert } // 작성하기 버튼

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule, container, false)

        /*initToolBarSetting()*/
        initCalendarSetting()
        initCalendarClicked()

        return binding.root
    }

    // ======================================= 일정 시간표 툴바 =======================================
    /*private fun initToolBarSetting() {
        val toolbar = binding.toolbarSchedule
        toolbar.inflateMenu(R.menu.item_toolbar_schedule)

        toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.menu_item_schedule_add -> {
                    val intent = Intent(context, AddScheduleActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.item_toolbar_schedule, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }*/

    private fun initCalendarSetting() {
        calendar.state().edit()
            .setFirstDayOfWeek(Calendar.SUNDAY)
            .setMinimumDate(CalendarDay.from(2017, 0, 1))
            .setMaximumDate(CalendarDay.from(2030, 11, 31))
            .setCalendarDisplayMode(CalendarMode.MONTHS)
            .commit()

        calendar.addDecorators(SaturdayDecorator(),SundayDecorator(),
            context?.let { OneDayDecorator(it) }) // 달력 요일별 색 변경
    }

    private fun initCalendarClicked() {
        calendar.setOnDateChangedListener { widget, date, selected ->
            selectDate.visibility = View.VISIBLE
            selectDate.text =String.format("%d / %d / %d", date.year, date.month + 1, date.day)
            addScheduleContent.visibility = View.VISIBLE
            insertBtnLayout.visibility = View.VISIBLE
            btnInsertSchedule.visibility = View.VISIBLE
        }
    }

}

// ======================================= CalendarView의 토요일 날짜 색 =======================================
class SaturdayDecorator : DayViewDecorator {
    private val calendar: Calendar = Calendar.getInstance()

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        day?.copyTo(calendar)
        val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
        return weekDay == Calendar.SATURDAY
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(ForegroundColorSpan(Color.BLUE))
    }
}

// ======================================= CalendarView의 일요일 날짜 색 =======================================
class SundayDecorator : DayViewDecorator {
    private val calendar: Calendar = Calendar.getInstance()

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        day?.copyTo(calendar)
        val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
        return weekDay == Calendar.SUNDAY
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(ForegroundColorSpan(Color.RED))
    }

}

// ======================================= CalendarView의 당일 날짜 색 =======================================
class OneDayDecorator(context: Context) : DayViewDecorator {
    private var date = CalendarDay.today()
    @SuppressLint("UseCompatLoadingForDrawables")
    val drawable = context.resources.getDrawable(R.drawable.radius_today_style)

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day?.equals(date)!!
    }

    override fun decorate(view: DayViewFacade?) {
        view?.setBackgroundDrawable(drawable)
    }

}