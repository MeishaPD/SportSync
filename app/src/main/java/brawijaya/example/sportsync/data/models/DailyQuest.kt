package brawijaya.example.sportsync.data.models

data class DailyQuest(
    val id: Int,
    val title: String,
    val description: String,
    val isCompleted: Boolean,
    val xpReward: Int,
    val iconType: QuestIconType
)

enum class QuestIconType {
    MATCH, EXERCISE, SOCIAL, ACHIEVEMENT, TRAINING
}
