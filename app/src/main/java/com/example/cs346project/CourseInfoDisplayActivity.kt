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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.viewmodel.compose.viewModel


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
                    lectureLocation = "Room 123",
                    courseRating = 0,
                    instructorRating = 0
                )
            )
        }
    }
}

fun mapDayCodeToDayName(dayCode: String): String {
    return when (dayCode) {
        "M" -> "Mondays"
        "T" -> "Tuesdays"
        "W" -> "Wednesdays"
        "R" -> "Thursdays"
        "F" -> "Fridays"
        else -> "Unknown"
    }
}

@Composable
fun CourseInput(courseData: CourseData) {

    var subject by remember { mutableStateOf("") }
    var courseNumber by remember { mutableStateOf("") }
    var isPopupVisible by remember { mutableStateOf(false) }
    val viewModel: CourseInfoViewModel = viewModel()
    
    LaunchedEffect(Unit, block = {
        viewModel.getCourseInfoAPIData()
    })

    LaunchedEffect(Unit, block = {
        viewModel.getClassScheduleInfoAPIData()
    })

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
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Close")
        }

        OutlinedTextField(
            value = subject,
            onValueChange = { subject = it },
            placeholder = { Text("e.g CS, BIOL etc") },
            label = { Text("Subject") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = courseNumber,
            onValueChange = { courseNumber = it },
            label = { Text("Course Number") },
            placeholder = { Text("e.g 100, 201, 346 etc") },
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

                        if (viewModel.errorMessage.isEmpty()) {
                            val specificCourse = viewModel.courseInfo.find { it.subjectCode == subject && it.catalogNumber == courseNumber }

                            if (specificCourse != null) {
                                Text(
                                    text =  "${specificCourse.subjectCode} ${specificCourse.catalogNumber}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 30.sp,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text =  specificCourse.title,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 22.sp,
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text =  specificCourse.requirementsDescription,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 15.sp,
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    textAlign = TextAlign.Center
                                )

                                val courseID = specificCourse.courseId

                                val specificCourseLectureInfo = viewModel.classScheduleInfo.find { it.courseId == courseID }

                                if (specificCourseLectureInfo != null) {
                                    val scheduleData = specificCourseLectureInfo.scheduleData[0]

                                    val classMeetingDayPatternCode = scheduleData.classMeetingDayPatternCode
                                    val lectureDays = mapDayCodeToDayName(classMeetingDayPatternCode)

                                    val classMeetingStartTime = scheduleData.classMeetingStartTime
                                    val startTime = classMeetingStartTime.split("T")[1]
                                    val startTimeWithoutSeconds = startTime.substring(0,5)

                                    val classMeetingEndTime = scheduleData.classMeetingEndTime
                                    val endTime = classMeetingEndTime.split("T")[1]
                                    val endTimeWithoutSeconds = endTime.substring(0,5)

                                    Text(
                                        text = "$lectureDays: $startTimeWithoutSeconds-$endTimeWithoutSeconds",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 18.sp,
                                        modifier = Modifier.padding(16.dp),
                                        textAlign = TextAlign.Center
                                    )
                                } else {
                                    Text(text = viewModel.apiErrorMessage)
                                }
                            } else {
                                Text(text = "Course not found")
                            }
                        } else {
                            Text(text = viewModel.errorMessage)
                        }

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
