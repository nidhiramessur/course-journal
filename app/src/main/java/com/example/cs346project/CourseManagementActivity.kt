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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class CourseManagementActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            CourseInfo(
                courseData = CourseData(
                    courseCode = "CS101",
                    courseName = "Introduction to Computer Science",
                    lectureDays = "Monday, Wednesday, Friday",
                    lectureTimes = "9:00 AM - 10:30 AM",
                    instructorName = "Dr. Smith",
                    lectureLocation = "Room 123",
                    courseRating = 0,
                    instructorRating = 0
                )
            )
        }
    }

}

@Composable
fun CourseInfo(courseData: CourseData) {

    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 20.dp, vertical = 50.dp),
        verticalArrangement = Arrangement.Top
    ){
        val context = LocalContext.current
        IconButton(
            onClick = {
                context.startActivity(Intent(context, HomepageActivity::class.java))
            },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Close")
        }

        Text(courseData.courseCode, fontSize = 30.sp,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center)
        Text(courseData.courseName, fontSize = 25.sp,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center)
        Text("Course Rating: ${courseData.courseRating}/5", fontSize = 15.sp,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center)

        // **can add complexity -
        // it displays the lecture times and days that the student attends
        // can make student choose lecture day/time when adding a course...

        Text(courseData.lectureDays, fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            textAlign = TextAlign.Center)
        Text(courseData.lectureTimes, fontSize = 15.sp,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center)
        Text(courseData.lectureLocation, fontSize = 15.sp,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center)

        Text("Instructor: ${courseData.instructorName}", fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            textAlign = TextAlign.Center)
        Text("Instructor Rating: ${courseData.instructorRating}/5", fontSize = 15.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 50.dp),
            textAlign = TextAlign.Center)

        Button(
            onClick = {
                context.startActivity(Intent(context, ToDoListActivity::class.java)) },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(" To-do list")
        }
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Notes")
        }

    }
}