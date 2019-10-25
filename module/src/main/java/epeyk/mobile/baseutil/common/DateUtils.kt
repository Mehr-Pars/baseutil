package epeyk.mobile.baseutil.common

import epeyk.mobile.baseutil.common.calendar.CalendarTool
import epeyk.mobile.baseutil.common.calendar.ShamsiCalendar
import java.util.*

object DateUtils {
    private const val dateFormat = "yyyy/MM/dd HH:mm:ss"

    fun getPersianDateFromTimeStamp(timeStamp: Long, outputFormat: String = dateFormat): String? {
        val timeMillis = timeStamp * 1000
        return getPersianDate(timeMillis, outputFormat)
    }

    fun getTimeStampFromPersianDate(
        stringDate: String,
        format: String = dateFormat
    ): Long {
        try {
            val simpleDate = SimpleDate.getSimpleDate(stringDate, format)
            val calendarTool = CalendarTool()
            calendarTool.setIranianDate(simpleDate.year, simpleDate.month, simpleDate.date)
            simpleDate.year = calendarTool.gregorianYear
            simpleDate.month = calendarTool.gregorianMonth
            simpleDate.date = calendarTool.gregorianDay

            return simpleDate.getDate().time / 1000
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return 0
    }

    fun getPersianDate(timeMillis: Long, outputFormat: String = dateFormat): String? {
        val date = Date(timeMillis)
        return getPersianDate(date, outputFormat)
    }

    fun getPersianDate(
        stringDate: String, inputFormat: String = dateFormat, outputFormat: String = dateFormat
    ): String? {
        val simpleDate = SimpleDate.getSimpleDate(stringDate, inputFormat)
        return getPersianDate(simpleDate.getDate(), outputFormat)
    }

    fun getPersianDate(date: Date, outputFormat: String = dateFormat): String? {
        try {
            val simpleDate = SimpleDate.getSimpleDate(date)
            val solarCalendar = ShamsiCalendar().SolarCalendar(date)

            simpleDate.year = solarCalendar.year
            simpleDate.month = solarCalendar.month
            simpleDate.date = solarCalendar.date
            return simpleDate.getStringDate(outputFormat)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun getGregorianDate(
        stringDate: String,
        inputFormat: String = dateFormat,
        outputFormat: String = dateFormat
    ): String? {
        try {
            val simpleDate = SimpleDate.getSimpleDate(stringDate, inputFormat)
            val calendarTool = CalendarTool()
            calendarTool.setIranianDate(simpleDate.year, simpleDate.month, simpleDate.date)

            simpleDate.year = calendarTool.gregorianYear
            simpleDate.month = calendarTool.gregorianMonth
            simpleDate.date = calendarTool.gregorianDay
            return simpleDate.getStringDate(outputFormat)
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

    @Throws(NumberFormatException::class)
    private fun getTimeNumber(s: String, index: Pair<Int, Int>): Int {
        val number = if (index.first >= 0 && index.second <= s.length)
            s.substring(index.first, index.second)
        else
            "0"
        return number.toInt()
    }

    class SimpleDate(
        var year: Int = 0,
        var month: Int = 0,
        var date: Int = 0,
        var hours: Int = 0,
        var minutes: Int = 0,
        var seconds: Int = 0
    ) {
        fun getStringDate(format: String = dateFormat): String {
            return format.replace("yyyy", "$year")
                .replace("MM", "%02d".format(month))
                .replace("dd", "%02d".format(date))
                .replace("HH", "%02d".format(hours))
                .replace("mm", "%02d".format(minutes))
                .replace("ss", "%02d".format(seconds))
        }

        fun getDate(): Date {
            return Date().apply {
                year = this@SimpleDate.year - 1900
                month = this@SimpleDate.month - 1
                date = this@SimpleDate.date
                hours = this@SimpleDate.hours
                minutes = this@SimpleDate.minutes
                seconds = this@SimpleDate.seconds
            }
        }

        override fun toString(): String {
            return getStringDate()
        }

        companion object {
            fun getSimpleDate(date: String, format: String = dateFormat): SimpleDate {
                return try {
                    SimpleDate().apply {
                        seconds =
                            getTimeNumber(
                                date,
                                Pair(format.indexOf("s"), format.lastIndexOf('s') + 1)
                            )
                        minutes =
                            getTimeNumber(
                                date,
                                Pair(format.indexOf("m"), format.lastIndexOf('m') + 1)
                            )
                        hours =
                            getTimeNumber(
                                date,
                                Pair(format.indexOf("H"), format.lastIndexOf('H') + 1)
                            )
                        this.date =
                            getTimeNumber(
                                date,
                                Pair(format.indexOf("d"), format.lastIndexOf('d') + 1)
                            )
                        month =
                            getTimeNumber(
                                date,
                                Pair(format.indexOf("M"), format.lastIndexOf('M') + 1)
                            )
                        year =
                            getTimeNumber(
                                date,
                                Pair(format.indexOf("y"), format.lastIndexOf('y') + 1)
                            )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    throw java.lang.Exception("Invalid Date Format! -> ($date) does not match with format ($format)")
                }
            }

            fun getSimpleDate(date: Date): SimpleDate {
                return SimpleDate(
                    date.year,
                    date.month,
                    date.date,
                    date.hours,
                    date.minutes,
                    date.seconds
                )
            }
        }
    }
}