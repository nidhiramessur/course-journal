package com.example.cs346project

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.Text
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.text.font.FontWeight


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
           PreviewHomePage()
        }
    }
}

@Preview
@Composable
fun PreviewHomePage(){
    Text("Course Journal", color = Color.LightGray, fontSize = 40.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 120.dp, vertical = 140.dp), textAlign = TextAlign.Center)
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(120.dp), verticalArrangement = Arrangement.Bottom){

        Button(onClick = { /*TODO*/ }) {
            Text("Current Term")
        }
        Button(onClick = { /*TODO*/ }) {
            Text("Search Term")
        }
        Button(onClick = { /*TODO*/ }) {
            Text("  Add Term ")
        }
    }
}


