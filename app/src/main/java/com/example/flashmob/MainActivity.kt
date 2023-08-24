package com.example.flashmob

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flashmob.ui.theme.FlashMobTheme
import com.example.flashmob.ui.theme.darkGray
import com.example.flashmob.ui.theme.green
import kotlin.properties.ObservableProperty

class MainActivity : ComponentActivity() {

    var curr = 2f

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlashMobTheme {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(darkGray),
                    contentAlignment = Alignment.Center
                ) {

                    CustomCircularProgressIndicator(
                        initialValue= 50,
                        primaryColor= green,
                        secondaryColor= Color.Yellow,
                        circleRadius = 250f,
                        onPositionChange = { position ->
                            //Do something with position

                        },
                        modifier = Modifier
                            .size(250.dp)
                            .background(darkGray)
                    )
                }

            }
        }
    }

}