package brawijaya.example.sportsync.ui.screens.friendlist.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import brawijaya.example.sportsync.R
import brawijaya.example.sportsync.ui.components.FriendListCard
import brawijaya.example.sportsync.ui.screens.friendlist.FriendListScreen

@Composable
fun FriendListContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Suggestion",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(Modifier.height(16.dp))

        LazyRow(
            modifier = Modifier.padding(start = 24.dp)
        ) {
            items(4) {
                SuggestionCard(
                    image = R.drawable.chris_profile,
                    name = "Christ Hemsworth",
                    onClick = {}
                )
            }
        }

        Text(
            text = "Friend List",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxHeight()
        ) {
            items(14) {
                FriendListCard()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    FriendListScreen(
        navController = rememberNavController()
    )
}