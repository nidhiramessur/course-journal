package com.example.cs346project

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    // Input field for course search
    var courseCode by remember { mutableStateOf(courseData.courseCode) }
    var isButtonClicked by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        OutlinedTextField(
            value = courseCode,
            onValueChange = { courseCode = it },
            label = { Text("Course Code") }
        )

        Button(
            onClick = { isButtonClicked = true }
        ) {
            Text("Search Course")
        }

        if (isButtonClicked) {
            CourseInfoDisplay(courseData)
        }

    }

}

@Composable
fun CourseInfoDisplay(courseData: CourseData){
    var isButtonClicked by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = { isButtonClicked = true }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            // Close Button to close the pop up
            IconButton(
                onClick = { isButtonClicked = true },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
            }

            // Course Info from API
            Text(text = courseData.courseCode, fontSize = 24.sp)
            Text(text = courseData.courseName, fontSize = 18.sp)
            Text(text = "Lecture Days: ${courseData.lectureDays}")
            Text(text = "Lecture Times: ${courseData.lectureTimes}")

            // Input fields for instructor name and lecture location
            var instructorName by remember { mutableStateOf(courseData.instructorName) }
            var lectureLocation by remember { mutableStateOf(courseData.lectureLocation) }

            TextField(
                value = instructorName,
                onValueChange = { instructorName = it },
                label = { Text("Instructor Name") }
            )

            TextField(
                value = lectureLocation,
                onValueChange = { lectureLocation = it },
                label = { Text("Lecture Location") }
            )

            // Add course button
            Button(
                onClick = { isButtonClicked = !isButtonClicked }
            ) {
                Text("Add Course")
            }

        }
    }

    if (isButtonClicked) {
        // need to use nav controller instead
    }
}


