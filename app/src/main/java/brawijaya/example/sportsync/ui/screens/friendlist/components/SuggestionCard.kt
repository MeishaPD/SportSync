package brawijaya.example.sportsync.ui.screens.friendlist.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import brawijaya.example.sportsync.R

@Composable
fun SuggestionCard(
    image: Int,
    name: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(5.dp),
        color = Color(0xFFF5F5F5),
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier.padding(end = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 12.dp).padding(top = 20.dp, bottom = 6.dp)
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier.clip(CircleShape).size(65.dp)
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = onClick,
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Black,
                    containerColor = Color(0xFFFCCD78)
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.PersonAdd,
                    contentDescription = "Add Friend",
                    modifier = Modifier.size(12.dp)
                )

                Spacer(Modifier.width(4.dp))

                Text(
                    text = "Add Friend",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSuggestion() {
    SuggestionCard(
        image = R.drawable.chris_profile,
        name = "Christ Evan",
        onClick = {}
    )
}