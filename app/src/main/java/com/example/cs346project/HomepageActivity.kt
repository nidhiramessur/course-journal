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
import com.google.firebase.auth.FirebaseAuth


class HomepageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            Homepage()
        }

    }

}



@Composable
fun Homepage() {
    Text("Course Journal", color = Color.LightGray, fontSize = 40.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 100.dp, vertical = 140.dp),
        textAlign = TextAlign.Center)
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(vertical = 190.dp),
        verticalArrangement = Arrangement.Bottom
    ){
        val context = LocalContext.current

        Button(
            onClick = {
                context.startActivity(Intent(context, CurrentTermActivity::class.java))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp)
        ) {
            Text("Current Term")
        }
        Button(
            onClick = {
                context.startActivity(Intent(context, SearchTermActivity::class.java))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp)
        ) {
            Text("Search or Add Term")
        }
        Button(
            onClick = {
                context.startActivity(Intent(context, UserFetchTermCoursesActivity::class.java))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp)
        ) {
            Text("Demo Backend") // Temporary (Needs to be in term page)
        }
        Button(
            onClick = {
                context.startActivity(Intent(context, NotesActivity::class.java))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp)
        ) {
            Text("Notes") // Temporary (Needs to be in course management page)
        }
        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                context.startActivity(Intent(context, MainActivity::class.java))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp)
        ) {
            Text("Logout")
        }
    }
}
