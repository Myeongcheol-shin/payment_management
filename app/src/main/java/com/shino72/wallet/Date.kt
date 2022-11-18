package com.shino72.wallet

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Date {
    var year = 0
    var month = 0
    var day = 0
    var cal : Calendar = Calendar.getInstance()
    var lastdate = 0;
    var NowLocalDateTime : LocalDateTime = LocalDateTime.now()

    fun getNowLocalTime() : LocalDateTime?
    {
        return LocalDateTime.now()
    }
    fun getLastDate() : Int
    {
        cal.set(year, month-1, day);
        lastdate = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        return lastdate
    }
    fun setDate()
    {
        NowLocalDateTime = getNowLocalTime()!!
        year = NowLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy")).toInt()
        month = NowLocalDateTime.format(DateTimeFormatter.ofPattern("MM")).toInt()
        day = NowLocalDateTime.format(DateTimeFormatter.ofPattern("dd")).toInt()
        cal.set(year,month - 1,day)
    }
    init {
        setDate()
    }
}