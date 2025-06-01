package brawijaya.example.sportsync.data.models

import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Serializable
data class Challenge(
    val id: String? = null,
    val created_by: String? = null,
    val accepted_by: String? = null,
    val declaration: String,
    val description: String? = null,
    val category: String,
    val gender: String,
    val type: String,
    val date: String,
    val time: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val location_name: String? = null,
    val created_at: String? = null
) {
    fun getDisplayDate(): String {
        return try {
            val localDate = LocalDate.parse(date)
            localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        } catch (e: Exception) {
            date
        }
    }

    fun getDisplayTime(): String {
        return try {
            val localTime = LocalTime.parse(time)
            localTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        } catch (e: Exception) {
            time
        }
    }

    fun getDisplayLocation(): String {
        return location_name ?: "Location not specified"
    }

    fun isAccepted(): Boolean {
        return accepted_by != null
    }

    companion object {
        fun convertDisplayDateToIso(displayDate: String): String {
            return try {
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val localDate = LocalDate.parse(displayDate, formatter)
                localDate.toString()
            } catch (e: Exception) {
                displayDate
            }
        }

        fun convertDisplayTimeToDatabase(displayTime: String): String {
            return try {
                val formatter = DateTimeFormatter.ofPattern("HH:mm")
                val localTime = LocalTime.parse(displayTime, formatter)
                localTime.toString()
            } catch (e: Exception) {
                "$displayTime:00"
            }
        }
    }
}