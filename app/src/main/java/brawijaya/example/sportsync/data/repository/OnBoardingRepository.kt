package brawijaya.example.sportsync.data.repository

import brawijaya.example.sportsync.data.models.OnboardingPage

class OnboardingRepository {
    fun getOnBoardingItems(): List<OnboardingPage> {
        return listOf(
            OnboardingPage(
                title = "Your Sports Life,\nAll in One Place",
                description = "Welcome to your personal sports dashboard! From finding matches to booking courts—everything starts here. One app, endless action."
            ),
            OnboardingPage(
                title = "Auto-Match,\nZero Hastle",
                description = "SportSync pairs you with players who match your skill level, location, and favorite sports. Just tap, match & play!"
            ),
            OnboardingPage(
                title = "Level Up\nYour Game",
                description = "Show off your stats, achievements, and badges. Climb the leaderboard and make every match count with SportSync’s gamified experience."
            ),
            OnboardingPage(
                title = "Book Courts\nin Seconds",
                description = "Search by location, price, or availability—then lock in your spot. No more calling or guessing."
            ),
            OnboardingPage(
                title = "Compete\nConquer",
                description = "Stay updated with upcoming tournaments, sign up with your team, and chase the trophy. It's time to show what you're made of!"
            )
        )
    }
}