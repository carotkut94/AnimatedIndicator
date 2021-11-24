package com.death.indicator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.death.indicator.ui.theme.AnimatedIndicator
import com.death.indicator.ui.theme.AnimatedIndicatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimatedIndicatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ProgressBar()
                }
            }
        }
    }
}

@Composable
fun ProgressBar() {
    Box(contentAlignment = Alignment.Center) {
        AnimatedIndicator(
            multilayer = false,
            strokeWidth = 24.dp,
            progressColor = Color.Red,
            progressBackgroundColor = Color.White,
            roundedBorder = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AnimatedIndicatorTheme {
        ProgressBar()
    }
}