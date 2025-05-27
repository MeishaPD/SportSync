package brawijaya.example.sportsync.ui.screens.onboarding

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import brawijaya.example.sportsync.ui.screens.onboarding.components.OnboardingContent
import brawijaya.example.sportsync.ui.screens.onboarding.components.OnboardingHeader
import brawijaya.example.sportsync.ui.screens.onboarding.components.PageIndicator
import brawijaya.example.sportsync.ui.viewmodels.OnboardingViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoardingScreen(
    navController: NavController,
    viewModel: OnboardingViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    when {
        uiState.onboardingItems.isNotEmpty() -> {
            val pagerState = rememberPagerState(pageCount = { uiState.onboardingItems.size })

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.95f)
                        .align(Alignment.BottomCenter)
                        .background(
                            Color(0xFFCDE4F4),
                            shape = RoundedCornerShape(
                                topStart = 50.dp,
                                topEnd = 50.dp,
                                bottomStart = 0.dp,
                                bottomEnd = 0.dp
                            )
                        )
                        .padding(bottom = 24.dp)
                        .statusBarsPadding()
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.Transparent
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            OnboardingHeader(
                                onSkipClick = {
                                    navController.navigate("auth") {
                                        popUpTo("onboarding") { inclusive = true }
                                    }
                                }
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            ) {
                                HorizontalPager(
                                    state = pagerState,
                                    modifier = Modifier.fillMaxSize()
                                ) { page ->
                                    OnboardingContent(
                                        data = uiState.onboardingItems[page]
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 40.dp, vertical = 32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                PageIndicator(
                                    pageCount = uiState.onboardingItems.size,
                                    currentPage = pagerState.currentPage,
                                    modifier = Modifier.padding(bottom = 32.dp)
                                )

                                Button(
                                    onClick = {
                                        scope.launch {
                                            if (pagerState.currentPage == uiState.onboardingItems.size - 1) {
                                                navController.navigate("auth") {
                                                    popUpTo("onboarding") { inclusive = true }
                                                }
                                            } else {
                                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFFCCD78)
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                ) {
                                    Text(
                                        text = if (pagerState.currentPage == uiState.onboardingItems.size - 1) "Get Started!" else "Next",
                                        color = Color.Black,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}