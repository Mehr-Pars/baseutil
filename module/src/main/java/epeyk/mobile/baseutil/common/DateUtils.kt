package epeyk.mobile.baseutil.common

import epeyk.mobile.baseutil.common.calendar.CalendarTool
import epeyk.mobile.baseutil.common.calendar.ShamsiCalendar
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun getPersianDate(time: Long): String {
        val date = Date(time)
        val calendar = ShamsiCalendar()
        val solarCalendar = calendar.SolarCalendar(date)

        val t = String.format("%2d:%2d:%2d", date.hours, date.minutes, date.seconds)
        return  "${solarCalendar.year}-${solarCalendar.month}-${solarCalendar.date}_$t"
    }

    fun getPersianDate(stringDate: String): String? {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("en"))
        try {
            val date = sdf.parse(stringDate.replace("T", " "))

            val calendar = ShamsiCalendar()
            val solarCalendar = calendar.SolarCalendar(date)

            return  "${solarCalendar.year}-${solarCalendar.month}-${solarCalendar.date}"
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun getPersianDateComplete(stringDate: String): String? {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("en"))
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        try {
            val date = sdf.parse(stringDate.replace("T", " "))
            sdf.timeZone = TimeZone.getTimeZone("GMT+4:30")
            val calendar = ShamsiCalendar()
            val solarCalendar = calendar.SolarCalendar(date)

            return String.format(
                Locale.US, "%02d:%02d , %d-%02d-%02d", date.hours, date.minutes,
                solarCalendar.year, solarCalendar.month, solarCalendar.date
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun getGregorianDate(stringDate: String): String? {

        try {
            val array =
                if (stringDate.contains("-")) stringDate.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray() else stringDate.split(
                    "/".toRegex()
                ).dropLastWhile { it.isEmpty() }.toTypedArray()
            val calendarTool = CalendarTool()
            calendarTool.setIranianDate(
                Integer.parseInt(array[0]),
                Integer.parseInt(array[1]),
                Integer.parseInt(array[2])
            )

            return calendarTool.gregorianDate
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun getTimeString(millis: Int): String {
        val second = (millis / 1000 % 60).toLong()
        val minute = (millis / (1000 * 60) % 60).toLong()
        val hour = (millis / (1000 * 60 * 60) % 24).toLong()
        return if (hour > 0) String.format("%02d:%02d:%02d", hour, minute, second)
        else String.format("%02d:%02d", minute, second)
    }
}