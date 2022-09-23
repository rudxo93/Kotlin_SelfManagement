package com.duran.selfmg.ui.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.duran.selfmg.R
import com.duran.selfmg.data.model.ScheduleEntity
import com.duran.selfmg.databinding.FragmentScheduleBinding
import com.duran.selfmg.ui.viewmodel.ScheduleViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.thread


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

    private lateinit var scheduleViewModel: ScheduleViewModel

    private var schedule: ScheduleEntity? = null
    lateinit var scheduleDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule, container, false)

        scheduleViewModel = ViewModelProvider(this)[ScheduleViewModel::class.java]

        initCalendarSetting()
        initCalendarClicked()

        return binding.root
    }

    // ======================================= CalendarView 세팅 =======================================
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

    // ======================================= CalendarView 날짜 클릭 시 이벤트 =======================================
    private fun initCalendarClicked() {
        calendar.setOnDateChangedListener { widget, date, selected ->
            val year = date.year
            val month = date.month + 1
            val day = date.day
            scheduleDate = "$year/$month/$day" // 저장할 날짜 만들어주기

            // 날짜 클릭 시 현재 년/월/일 표시해주기
            selectDate.visibility = View.VISIBLE
            selectDate.text =String.format("%d / %d / %d", year, month, day)
            addScheduleContent.text.clear() // 다른날짜 클릭할 때 마다 지워주기 -> 수정중이거나 작성중일떄 남아있기 때문에

            // 날짜 클릭 시 db에 content가 존재하는지 조회
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    schedule = scheduleViewModel.getSchedule(scheduleDate) // 해당 날짜에 content가 있다면 content보여주고 update와 delete
                    initGetSchedule(schedule!!)
                    initBtnUpdateSchedule(schedule!!)
                    initBtnDeleteSchedule(schedule!!.scheduleDate)

                } catch (e: NullPointerException) { // 해당 날짜에 content가 없다면 view세팅과 content 저장
                    initInsertSchedule()
                    initBtnInsertSchedule()
                }

            }
        }
    }

    // ======================================= 수정하기 버튼 클릭 시 이벤트 =======================================
    private fun initBtnUpdateSchedule(schedule: ScheduleEntity) {
        btnUpdateSchedule.setOnClickListener {
            /*val updateSchedule = ScheduleEntity(schedule.id, scheduleDate, addScheduleContent.text.toString())
            scheduleViewModel.scheduleUpdate(updateSchedule)*/
            initUpdateSchedule(schedule)
            initBtnUpdateSaveSchedule(schedule)
        }
    }

    // ======================================= 삭제하기 버튼 클릭 시 이벤트 =======================================
    private fun initBtnDeleteSchedule(scheduleDate: String) {
        btnDeleteSchedule.setOnClickListener {
            scheduleViewModel.scheduleDelete(scheduleDate)
            Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
            initInsertSchedule()
            initBtnInsertSchedule()
        }
    }

    // ======================================= 클릭한 날짜에 content가 없다면 -> insert 할 수 있는 view 세팅 =======================================
    private fun initInsertSchedule() {
        thread(start = true) {
            Thread.sleep(50)
            activity?.runOnUiThread {
                addScheduleContent.visibility = View.VISIBLE
                insertBtnLayout.visibility = View.VISIBLE
                btnInsertSchedule.visibility = View.VISIBLE
                scheduleContent.visibility = View.GONE
                updateBtnLayout.visibility = View.GONE
                btnUpdateSchedule.visibility = View.GONE
                btnDeleteSchedule.visibility = View.GONE
            }
        }
    }

    // ======================================= 클릭한 날짜에 content를 update 한다면 -> update 할 수 있는 view 세팅 =======================================
    private fun initUpdateSchedule(schedule: ScheduleEntity) {
        thread(start = true) {
            Thread.sleep(50)
            activity?.runOnUiThread {
                addScheduleContent.visibility = View.VISIBLE
                insertBtnLayout.visibility = View.VISIBLE
                btnInsertSchedule.visibility = View.VISIBLE
                scheduleContent.visibility = View.GONE
                updateBtnLayout.visibility = View.GONE
                btnUpdateSchedule.visibility = View.GONE
                btnDeleteSchedule.visibility = View.GONE
                addScheduleContent.setText(schedule.scheduleContent)
            }
        }
    }

    // ======================================= 클릭한 날짜에 content가 있다면 -> db에 조회한 content view 세팅 =======================================
    private fun initGetSchedule(schedule: ScheduleEntity) {
        thread(start = true) {
            Thread.sleep(50)
            activity?.runOnUiThread {
                scheduleContent.visibility = View.VISIBLE
                updateBtnLayout.visibility = View.VISIBLE
                btnUpdateSchedule.visibility = View.VISIBLE
                btnDeleteSchedule.visibility = View.VISIBLE
                addScheduleContent.visibility = View.GONE
                insertBtnLayout.visibility = View.GONE
                btnInsertSchedule.visibility = View.GONE
                scheduleContent.text = schedule.scheduleContent
            }
        }
    }

    // ======================================= content에 text가 있는지 판별 후 insert 진행 -> insert후 바로 db조회 후 view 바꿔주기 =======================================
    private fun initBtnInsertSchedule() {
        btnInsertSchedule.setOnClickListener {
            if(addScheduleContent.text.isNotEmpty()) {
                val scheduleEntity = ScheduleEntity(0, scheduleDate, addScheduleContent.text.toString())
                scheduleViewModel.scheduleInsert(scheduleEntity)
                addScheduleContent.text.clear()
                CoroutineScope(Dispatchers.IO).launch {
                    schedule = scheduleViewModel.getSchedule(scheduleDate) // db에 존재한다면 잘 나오지만 db에 없는 날짜를 클릭 시 에러 발생
                    initGetSchedule(schedule!!)
                }
                Toast.makeText(context, "저장되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "스케줄이 비어있습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ======================================= content에 text가 있는지 판별 후 update 진행 -> update후 바로 db조회 후 view 바꿔주기 =======================================
    private fun initBtnUpdateSaveSchedule(schedule: ScheduleEntity) {
        btnInsertSchedule.setOnClickListener {
            if(addScheduleContent.text.isNotEmpty()) {
                val updateSchedule = ScheduleEntity(schedule.id, scheduleDate, addScheduleContent.text.toString())
                scheduleViewModel.scheduleUpdate(updateSchedule)
                addScheduleContent.text.clear()
                CoroutineScope(Dispatchers.IO).launch {
                    val updateSchedule = scheduleViewModel.getSchedule(scheduleDate) // db에 존재한다면 잘 나오지만 db에 없는 날짜를 클릭 시 에러 발생
                    initGetSchedule(updateSchedule)
                }
                Toast.makeText(context, "변경되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "스케줄이 비어있습니다.", Toast.LENGTH_SHORT).show()
            }
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