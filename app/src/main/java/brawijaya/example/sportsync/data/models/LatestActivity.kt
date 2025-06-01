package brawijaya.example.sportsync.data.models

data class LatestActivity(
    val id: Int,
    val title: String,
    val description: String,
    val timeAgo: String,
    val isSuccess: Boolean,
    val activityType: ActivityType
)

enum class ActivityType {
    MATCHMAKING, TOURNAMENT, TRAINING, SOCIAL, ACHIEVEMENT
}
