package brawijaya.example.sportsync.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Sports
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import brawijaya.example.sportsync.data.models.DailyQuest
import brawijaya.example.sportsync.data.models.QuestIconType

@Composable
fun DailyQuestCard(quest: DailyQuest) {
    val iconVector = when (quest.iconType) {
        QuestIconType.MATCH -> Icons.Rounded.Sports
        QuestIconType.EXERCISE -> Icons.Rounded.FitnessCenter
        QuestIconType.SOCIAL -> Icons.Rounded.Group
        QuestIconType.ACHIEVEMENT -> Icons.Rounded.EmojiEvents
        QuestIconType.TRAINING -> Icons.Rounded.School
    }

    val iconTint = if (quest.isCompleted) Color(0xFF4CAF50) else Color(0xFFC8E6C9)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(25.dp))
            .background(Color(0xFFFCCD78))
            .height(80.dp)
            .width(315.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (quest.isCompleted) Icons.Rounded.CheckCircle else iconVector,
                contentDescription = "Quest Icon",
                tint = iconTint,
                modifier = Modifier.size(45.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = quest.title,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    lineHeight = 20.sp
                )
                Text(
                    text = "+${quest.xpReward} XP",
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = Color(0xFF616264)
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = "View More",
                modifier = Modifier.size(28.dp)
            )
        }
    }
    Spacer(Modifier.size(8.dp))
}
