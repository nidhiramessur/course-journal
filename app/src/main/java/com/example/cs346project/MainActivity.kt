package com.example.cs346project

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            Welcome()
        }
    }
}

@Composable
fun Welcome() {
    Text("Course Journal", color = Color.LightGray, fontSize = 40.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 120.dp, vertical = 140.dp),
        textAlign = TextAlign.Center)
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(120.dp), verticalArrangement = Arrangement.Center){

        val context = LocalContext.current
        Button(onClick = {
            context.startActivity(Intent(context, UserSignupActivity::class.java))
        }) {
            Text("  Sign Up  ")
        }
        Button(onClick = {
            context.startActivity(Intent(context, UserSignInActivity::class.java))
        }) {
            Text("  Sign In  ")
        }
    }
}

