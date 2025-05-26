package brawijaya.example.sportsync.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import brawijaya.example.sportsync.R
import brawijaya.example.sportsync.ui.navigation.Screen

@Composable
fun BottomNavItem(
    icon: ImageVector? = null,
    painterRes: Int? = null,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    filledIcon: ImageVector? = null
) {

    val itemColor = if (isSelected) Color(0xFFFAA916) else Color.Black
    val displayIcon = if (isSelected && filledIcon != null) filledIcon else icon

    IconButton(
        onClick = onClick,
        modifier = Modifier.size(48.dp)
    ) {
        when {
            icon != null -> {
                Icon(
                    imageVector = displayIcon!!,
                    contentDescription = label,
                    tint = if (isSelected) Color(0xFFFAA916) else itemColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            painterRes != null -> {
                Icon(
                    painter = painterResource(id = painterRes),
                    contentDescription = label,
                    tint = if (isSelected) Color(0xFFFAA916) else itemColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun BottomNavigation(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        modifier = modifier
            .fillMaxWidth()
            .zIndex(1f)
            .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)),
        containerColor = Color(0xFFCDE4F4),
        contentColor = Color.Black,
        tonalElevation = 0.dp,
        contentPadding = PaddingValues(0.dp)
    ) {
        Surface(
            color = Color(0xFFCDE4F4),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem(
                    icon = Icons.Rounded.Home,
                    filledIcon = Icons.Rounded.Home,
                    label = "Home",
                    isSelected = currentRoute == Screen.Home.route,
                    onClick = { onNavigate(Screen.Home.route) }
                )

                BottomNavItem(
                    painterRes = R.drawable.ic_teamup,
                    label = "TeamUp",
                    isSelected =  currentRoute == Screen.FindMatch.route,
                    onClick = { onNavigate(Screen.FindMatch.route) }
                )
                BottomNavItem(
                    painterRes = R.drawable.ic_court,
                    label = "Court",
                    isSelected = false,
                    onClick = { }
                )
                BottomNavItem(
                    painterRes = R.drawable.ic_gamezone,
                    label = "GameZone",
                    isSelected = currentRoute == Screen.GameZone.route,
                    onClick = { onNavigate(Screen.GameZone.route) }
                )
                BottomNavItem(
                    painterRes = R.drawable.ic_profile,
                    label = "Profile",
                    isSelected = false,
                    onClick = { }
                )
            }
        }
    }
}