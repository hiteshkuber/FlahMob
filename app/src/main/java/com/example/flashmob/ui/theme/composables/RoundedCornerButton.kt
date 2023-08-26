package com.example.flashmob.ui.theme.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flashmob.ui.theme.darkGray

@Composable
fun RoundedCornerButton(
    onButtonToggle: (Boolean) -> Unit
) {

    var isOn by remember { mutableStateOf(false) }

    val buttonText = if (isOn) "ON" else "OFF"
    val buttonColor = if (isOn) Color.Green else Color.Yellow

    Button(
        onClick = {
            isOn = !isOn
            onButtonToggle(isOn)
        },
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = buttonColor,
                    contentColor = darkGray
                )
    ) {
        Text(
            text = buttonText,
            style = TextStyle(
                        fontWeight = FontWeight.ExtraBold
                    )
        )
    }
}


@Preview
@Composable
fun App() {
//    RoundedCornerButton()
}