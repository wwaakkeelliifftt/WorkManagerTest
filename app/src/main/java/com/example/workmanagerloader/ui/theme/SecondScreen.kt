package com.example.workmanagerloader.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.workmanagerloader.WindowInfo
import com.example.workmanagerloader.rememberWindowInfo

@Composable
fun SecondScreen(
    navController: NavController
) {

    // self-made utility fun for the wide screen mode
    val windowInfo = rememberWindowInfo()

    if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact) {
        SecondScreen_Compact()
    } else {
        SecondScreen_Medium()
    }


}

@Composable
fun SecondScreen_Compact() {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(count = 10) { id1 ->
            Text(
                text = "first list -- item #$id1",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF9FA8DA))
                    .padding(16.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Light
            )
        }
        items(count = 10) { id2 ->
            Text(
                text = "second list -- item #$id2",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEF9A9A))
                    .padding(16.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Thin
            )
        }
    }
}

@Composable
fun SecondScreen_Medium() {
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(count = 10) { id1 ->
                Text(
                    text = "first list -- item #$id1",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF9FA8DA))
                        .padding(16.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(count = 10) { id2 ->
                Text(
                    text = "second list -- item #$id2",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFEF9A9A))
                        .padding(16.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Thin
                )
            }

        }
    }
}