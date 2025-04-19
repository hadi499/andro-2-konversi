package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.pages.SeribuHari
import com.example.myapplication.ui.pages.UnitConverter
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay


class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme{
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var showSplash by remember { mutableStateOf(true) }
                    val navController = rememberNavController()
                    if (showSplash) {
                        SplashScreenWithFade(
                            onLoadingComplete = { showSplash = false }
                        )
                    } else {
                        NavigationGraph(navController = navController)
                    }
                }
            }

        }
    }
}


@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") { HomeScreen(navController) }
        composable("1000hari") { SeribuHari() }
        composable("unit_converter") { UnitConverter() }
    }
}


@Composable
fun SplashScreenWithFade(
    onLoadingComplete: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 0f else 1f,
        animationSpec = tween(1000)
    )

    LaunchedEffect(Unit) {
        delay(1500) // Durasi tampil
        startAnimation = true
        delay(1000) // Durasi fade out
        onLoadingComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { alpha = alphaAnim.value }
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.launcher),
            contentDescription = null,
            modifier = Modifier.size(180.dp)
        )
    }
}





@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Selamat Datang",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { navController.navigate("1000hari") },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("1000 Hari Converter")
        }

        Button(
            onClick = { navController.navigate("unit_converter") },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Unit Converter")
        }
    }
}



@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MyApplicationTheme {
        val navController = rememberNavController()
        HomeScreen(navController = navController)
    }
}