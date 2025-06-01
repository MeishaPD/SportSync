package brawijaya.example.sportsync.data.models

import brawijaya.example.sportsync.R

object DummyData {

    val upcomingEvents = listOf(
        UpcomingEvent(
            id = 1,
            title = "Mini-Soc Community Cup",
            date = "Wed, 12th Jun",
            time = "14:00",
            duration = "2 h 10 m",
            imageRes = R.drawable.minsoc
        ),
        UpcomingEvent(
            id = 2,
            title = "Basketball Tournament",
            date = "Fri, 14th Jun",
            time = "16:30",
            duration = "3 h 0 m",
            imageRes = R.drawable.basketball
        ),
        UpcomingEvent(
            id = 3,
            title = "Volleyball Championship",
            date = "Sat, 15th Jun",
            time = "09:00",
            duration = "4 h 30 m",
            imageRes = R.drawable.volleyball
        ),
        UpcomingEvent(
            id = 4,
            title = "Badminton Singles",
            date = "Sun, 16th Jun",
            time = "10:00",
            duration = "1 h 45 m",
            imageRes = R.drawable.badminton
        )
    )

    val dailyQuests = listOf(
        DailyQuest(
            id = 1,
            title = "Complete a match today",
            description = "Play and finish any sports match",
            isCompleted = true,
            xpReward = 50,
            iconType = QuestIconType.MATCH
        ),
        DailyQuest(
            id = 2,
            title = "Exercise for 30 minutes",
            description = "Complete a 30-minute workout session",
            isCompleted = false,
            xpReward = 35,
            iconType = QuestIconType.EXERCISE
        ),
        DailyQuest(
            id = 3,
            title = "Join a team practice",
            description = "Participate in team training",
            isCompleted = false,
            xpReward = 40,
            iconType = QuestIconType.TRAINING
        ),
        DailyQuest(
            id = 4,
            title = "Connect with 3 players",
            description = "Add new friends to your network",
            isCompleted = true,
            xpReward = 25,
            iconType = QuestIconType.SOCIAL
        ),
        DailyQuest(
            id = 5,
            title = "Win 2 matches",
            description = "Achieve victory in competitive matches",
            isCompleted = false,
            xpReward = 75,
            iconType = QuestIconType.ACHIEVEMENT
        )
    )

    val latestActivities = listOf(
        LatestActivity(
            id = 1,
            title = "Matchmaking",
            description = "Successfully",
            timeAgo = "3h ago",
            isSuccess = true,
            activityType = ActivityType.MATCHMAKING
        ),
        LatestActivity(
            id = 2,
            title = "Tournament Registration",
            description = "Completed",
            timeAgo = "5h ago",
            isSuccess = true,
            activityType = ActivityType.TOURNAMENT
        ),
        LatestActivity(
            id = 3,
            title = "Training Session",
            description = "Finished",
            timeAgo = "1d ago",
            isSuccess = true,
            activityType = ActivityType.TRAINING
        ),
        LatestActivity(
            id = 4,
            title = "Friend Request",
            description = "Sent",
            timeAgo = "2d ago",
            isSuccess = true,
            activityType = ActivityType.SOCIAL
        ),
        LatestActivity(
            id = 5,
            title = "Achievement Unlocked",
            description = "First Victory",
            timeAgo = "3d ago",
            isSuccess = true,
            activityType = ActivityType.ACHIEVEMENT
        ),
        LatestActivity(
            id = 6,
            title = "Match",
            description = "Lost",
            timeAgo = "4d ago",
            isSuccess = false,
            activityType = ActivityType.MATCHMAKING
        ),
        LatestActivity(
            id = 7,
            title = "Team Practice",
            description = "Attended",
            timeAgo = "5d ago",
            isSuccess = true,
            activityType = ActivityType.TRAINING
        )
    )
}