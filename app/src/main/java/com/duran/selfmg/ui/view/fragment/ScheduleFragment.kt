package com.duran.selfmg.ui.view.fragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.duran.selfmg.R
import com.duran.selfmg.databinding.FragmentScheduleBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.*

class ScheduleFragment : Fragment() {

    private lateinit var binding: FragmentScheduleBinding

    private val calendar by lazy { binding.calendarView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule, container, false)

        initCalendarSetting()

        return binding.root
    }

    private fun initCalendarSetting() {
        calendar.state().edit()
            .setFirstDayOfWeek(Calendar.SUNDAY)
            .setMinimumDate(CalendarDay.from(2017, 0, 1))
            .setMaximumDate(CalendarDay.from(2030, 11, 31))
            .setCalendarDisplayMode(CalendarMode.MONTHS)
            .commit()

        calendar.addDecorators(SaturdayDecorator(),SundayDecorator(), OneDayDecorator())
    }

}

// ======================================= CalendarView의 토요일 날짜 색 =======================================
class SaturdayDecorator : DayViewDecorator {

    private companion object {
        val calendar: Calendar = Calendar.getInstance()
    }

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        day?.copyTo(calendar)
        val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
        return weekDay == Calendar.SATURDAY
    }

    override fun decorate(view: DayViewFacade?) {
        view!!.addSpan(ForegroundColorSpan(Color.BLUE))
    }
}

// ======================================= CalendarView의 일요일 날짜 색 =======================================
class SundayDecorator : DayViewDecorator {

    private companion object {
        val calendar: Calendar = Calendar.getInstance()
    }

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        day?.copyTo(calendar)
        val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
        return weekDay == Calendar.SUNDAY
    }

    override fun decorate(view: DayViewFacade?) {
        view!!.addSpan(ForegroundColorSpan(Color.RED))
    }

}

// ======================================= CalendarView의 당일 날짜 색 =======================================
class OneDayDecorator : DayViewDecorator {
    private var date: CalendarDay

    init {
        date = CalendarDay.today()
    }

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day?.equals(date)!!
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(StyleSpan(Typeface.BOLD))
        view?.addSpan(RelativeSizeSpan(1.4f))
        view?.addSpan(ForegroundColorSpan(Color.GREEN))
    }

    fun setDate(date: Date) {
        this.date = CalendarDay.from(date)
    }
}