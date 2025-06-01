package brawijaya.example.sportsync.data.models

data class UpcomingEvent(
    val id: Int,
    val title: String,
    val date: String,
    val time: String,
    val duration: String,
    val imageRes: Int
)
