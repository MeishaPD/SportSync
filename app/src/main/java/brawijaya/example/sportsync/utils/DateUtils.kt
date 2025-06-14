package brawijaya.example.sportsync.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {
    private val dateFormatThreadLocal = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue(): SimpleDateFormat {
            return SimpleDateFormat("dd/MM/yyyy", Locale("id", "ID"))
        }
    }

    private fun getDateFormat(): SimpleDateFormat {
        return dateFormatThreadLocal.get()
    }

    fun parseDate(dateString: String): Date? {
        if (dateString.isEmpty()) return null

        return runCatching {
            getDateFormat().parse(dateString)
        }.getOrNull()
    }

    fun getMillisFromDate(dateString: String): Long? {
        return parseDate(dateString)?.time
    }

    fun formatDate(date: Date): String {
        return getDateFormat().format(date)
    }

    fun formatDate(year: Int, month: Int, dayOfMonth: Int): String {
        return formatDate(getDateFromComponents(year, month, dayOfMonth))
    }

    fun getDateFromComponents(year: Int, month: Int, dayOfMonth: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        return calendar.time
    }

    fun getCurrentDate(): Date {
        return Calendar.getInstance().time
    }
}