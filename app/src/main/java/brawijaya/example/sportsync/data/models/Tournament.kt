package brawijaya.example.sportsync.data.models

import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@Serializable
data class Tournament(
    val id: String? = null,
    val team_1: String,
    val team_2: String,
    val date: String,
    val time: String,
    val type: String,
    val created_at: String? = null
) {
    fun getDisplayDate(): String {
        return try {
            val localDate = LocalDate.parse(date)
            localDate.format(DateTimeFormatter.ofPattern("EEE, dd MMM", Locale.ENGLISH))
        } catch (e: Exception) {
            date
        }
    }

    fun getDisplayTime(): String {
        return try {
            val localTime = LocalTime.parse(time)
            localTime.format(DateTimeFormatter.ofPattern("HH.mm"))
        } catch (e: Exception) {
            time
        }
    }

    fun getFormattedTime(): String {
        return "${getDisplayTime()} ${if (getDisplayTime().split(".")[0].toInt() < 12) "AM" else "PM"}"
    }
}