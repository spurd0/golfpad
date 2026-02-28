package com.roman.golfpad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.roman.domain.DistanceProvider
import com.roman.golfpad.ui.GolfCourseScreen
import com.roman.golfpad.ui.theme.GolfPadTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var distanceProvider: DistanceProvider

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GolfPadTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GolfCourseScreen(modifier = Modifier.padding(innerPadding), distanceProvider)
                }
            }
        }
    }
}