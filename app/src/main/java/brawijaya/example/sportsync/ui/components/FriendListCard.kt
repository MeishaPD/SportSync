package brawijaya.example.sportsync.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import brawijaya.example.sportsync.R

@Composable
fun FriendListCard(

) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFF5F5F5),
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(bottom = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = painterResource(id = R.drawable.rdj_profile),
                contentDescription = "RDJ Profile",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(60.dp)
            )
            Column {
                Text(
                    text = "Robert Downey Jr.",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "robertdowneyjr@gmail.com",
                    fontSize = 14.sp,
                )
            }
            Text(
                text = "Last Match, 28 Okt",
                fontSize = 10.sp
            )
        }
    }
}