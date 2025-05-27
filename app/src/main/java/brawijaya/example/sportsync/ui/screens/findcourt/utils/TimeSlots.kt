package brawijaya.example.sportsync.ui.screens.findcourt.utils

import brawijaya.example.sportsync.data.models.TimeSlot


object TimeSlots {
    fun generateTimeSlots(): List<TimeSlot> {
        val times = listOf(
            "00:00", "01:00", "02:00", "03:00",
            "04:00", "05:00", "06:00", "07:00",
            "08:00", "09:00", "10:00", "11:00",
            "12:00", "13:00", "14:00", "15:00",
            "16:00", "17:00", "18:00", "19:00",
            "20:00", "21:00", "22:00", "23:00"
        )

        return times.map { time ->
            TimeSlot(
                time = time,
                isAvailable = time != "14:00" && time != "15:00" // Some slots unavailable for now
            )
        }
    }
}