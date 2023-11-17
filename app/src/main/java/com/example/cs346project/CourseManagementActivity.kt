package com.example.cs346project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cs346project.viewModels.CourseManagementViewModel

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

    val viewModel: CourseManagementViewModel = viewModel()
    val courseInfoState = viewModel.courseInfoState.collectAsState()
    val termUUID = "5ee51a2c-022a-46f5-851e-558ad9a14a05"
    val courseUUID = "b6bb6d62-342c-4419-a3b7-58d1363f3ad2"

    LaunchedEffect(key1 = termUUID, key2 = courseUUID) {
        viewModel.fetchCourseInfo(termUUID, courseUUID)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 20.dp, vertical = 50.dp)
            .verticalScroll(rememberScrollState()),
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

        val courseDBData = courseInfoState.value.firstOrNull()
        Log.d("COURSE STATE", courseInfoState.value.toString())

        if (courseDBData != null) {
            Text(courseDBData.name, fontSize = 30.sp,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center)
            Text(courseDBData.title, fontSize = 25.sp,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center)
            if (courseDBData.requirements.isNotEmpty()) {
                Text(courseDBData.requirements, fontSize = 15.sp,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center)
            }

            Text("Course Rating: ${courseData.courseRating}/5", fontSize = 15.sp,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center)

            // **can add complexity -
            // it displays the lecture times and days that the student attends
            // can make student choose lecture day/time when adding a course...

            if (courseDBData.lecturedatetime.isNotEmpty()) {
                Text(courseDBData.lecturedatetime, fontSize = 15.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp),
                    textAlign = TextAlign.Center)
            }

            if (courseDBData.lecturelocation.isNotEmpty()) {
                Text(courseDBData.lecturelocation, fontSize = 15.sp,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center)
            }

            if (courseDBData.professorname.isNotEmpty()) {
                Text("Instructor: ${courseDBData.professorname}", fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp),
                    textAlign = TextAlign.Center)
                Text("Instructor Rating: ${courseData.instructorRating}/5", fontSize = 15.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 50.dp),
                    textAlign = TextAlign.Center)
            }
        } else {
            Text(text = "Error loading course")
        }

        Button(
            onClick = {
                context.startActivity(Intent(context, ToDoListActivity::class.java)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp)
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