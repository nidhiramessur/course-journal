package com.example.cs346project

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog


class CourseInfoDisplayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            CourseInput(
                courseData = CourseData(
                    courseCode = "CS101",
                    courseName = "Introduction to Computer Science",
                    lectureDays = "Monday, Wednesday, Friday",
                    lectureTimes = "9:00 AM - 10:30 AM",
                    instructorName = "Dr. Smith",
                    lectureLocation = "Room 123"
                )
            )
        }
    }
}

@Composable
fun CourseInput(courseData: CourseData) {

    var courseCodeInput by remember { mutableStateOf(courseData.courseCode) }
    var isPopupVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val context = LocalContext.current
        IconButton(
            onClick = {
                context.startActivity(Intent(context, HomepageActivity::class.java))
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Close")
        }

        OutlinedTextField(
            value = courseCodeInput,
            onValueChange = { courseCodeInput = it },
            label = { Text("Course Code") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = { isPopupVisible = true },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Search course")
        }

        if (isPopupVisible) {
            Dialog(
                onDismissRequest = { isPopupVisible = false }
            ){
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(580.dp),
                    shape = RoundedCornerShape(13.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        IconButton(
                            onClick = { isPopupVisible = false },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                        }

                        Text(
                            text =  courseData.courseCode,
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            modifier = Modifier.padding(bottom = 20.dp),
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text =  courseData.courseName,
                            fontWeight = FontWeight.Normal,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 16.dp),
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = courseData.lectureDays,
                            fontWeight = FontWeight.Normal,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = courseData.lectureTimes,
                            fontWeight = FontWeight.Normal,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )

                        // Input fields for instructor name and lecture location
                        var instructorName by remember { mutableStateOf(courseData.instructorName) }
                        var lectureLocation by remember { mutableStateOf(courseData.lectureLocation) }

                        TextField(
                            value = instructorName,
                            onValueChange = { instructorName = it },
                            label = { Text("Instructor name (optional)") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )

                        TextField(
                            value = lectureLocation,
                            onValueChange = { lectureLocation = it },
                            label = { Text("Lecture location (optional)") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )

                        Button(
                            onClick = { /* TODO */ },
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text("Add course")
                        }
                    }
                }
            }
        }

    }

}
