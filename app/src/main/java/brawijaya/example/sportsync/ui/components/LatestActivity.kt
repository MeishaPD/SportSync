package brawijaya.example.sportsync.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material.icons.rounded.Sports
import androidx.compose.material.icons.rounded.Star
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
import brawijaya.example.sportsync.data.models.ActivityType
import brawijaya.example.sportsync.data.models.LatestActivity

@Composable
fun LatestActivity(activity: LatestActivity) {
    val activityIcon = when (activity.activityType) {
        ActivityType.MATCHMAKING -> Icons.Rounded.Sports
        ActivityType.TOURNAMENT -> Icons.Rounded.EmojiEvents
        ActivityType.TRAINING -> Icons.Rounded.FitnessCenter
        ActivityType.SOCIAL -> Icons.Rounded.Group
        ActivityType.ACHIEVEMENT -> Icons.Rounded.Star
    }

    val iconTint = if (activity.isSuccess) Color(0xFF4CAF50) else Color(0xFFFF9800)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(Color(0xFFFCCD78))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (activity.isSuccess) Icons.Rounded.CheckCircle else activityIcon,
                contentDescription = "Activity Status",
                tint = iconTint,
                modifier = Modifier.size(30.dp)
            )
            Spacer(Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = activity.title,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
                Text(
                    text = activity.description,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                    color = Color(0xFF616264)
                )
            }
            Text(
                text = activity.timeAgo,
                fontSize = 10.sp,
                color = Color(0xFF616264)
            )
        }
    }
    Spacer(Modifier.height(8.dp))
}