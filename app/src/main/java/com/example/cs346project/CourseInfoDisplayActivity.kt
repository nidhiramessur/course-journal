package com.example.cs346project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
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
import com.example.cs346project.viewModels.CourseInfoViewModel
import com.example.cs346project.viewModels.NavigationViewModel


class CourseInfoDisplayActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

         setContent {
            val selectedItem = NavigationViewModel.selectedItem.value
            BaseScreen(selectedItem, onItemSelected = { index ->
                NavigationViewModel.selectedItem.value = index
            }) {
                CourseInput()
            }
        }
    }
}

    @Composable
    fun CourseInput() {

        var subject by remember { mutableStateOf("") }
        var courseNumber by remember { mutableStateOf("") }
        var isPopupVisible by remember { mutableStateOf(false) }
        val viewModel: CourseInfoViewModel = viewModel()
        var isLoading by remember { mutableStateOf(false) }
        var lectureDateTime = ""
        var title = ""
        var requirements = ""
        val lectureDateTimeSet = mutableSetOf<String>()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Course search fields and button
            val context = LocalContext.current
            IconButton(
                onClick = {
                    // Navigate to current term because user can only add courses in current term not in previous terms
                    context.startActivity(Intent(context, CurrentTermActivity::class.java))
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
                    .width(320.dp)
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = courseNumber,
                onValueChange = { courseNumber = it },
                label = { Text("Course Number") },
                placeholder = { Text("e.g 100, 201, 346 etc") },
                modifier = Modifier
                    .width(320.dp)
                    .padding(bottom = 16.dp)
            )

            Button(
                onClick = { isPopupVisible = true },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Search course")
            }

            // Pop up that appears when a course is searched
            if (isPopupVisible) {
                Dialog(
                    onDismissRequest = { isPopupVisible = false }
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(580.dp),
                        shape = RoundedCornerShape(13.dp),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            IconButton(
                                onClick = { isPopupVisible = false },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close"
                                )
                            }


                            LaunchedEffect(Unit, block = {
                                isLoading = true
                                viewModel.getCourseInfoAPIData(subject, courseNumber)
                                viewModel.getClassScheduleInfoAPIData(subject, courseNumber)
                                isLoading = false
                            })

                            // Display loading indicator if the API call is in progress
                            if (isLoading) {
                                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                            }

                            // Display course info from UW Open API if available
                            if (viewModel.errorMessage.isEmpty()) {
                                if (viewModel.courseInfo.isNotEmpty()) {
                                    val specificCourse = viewModel.courseInfo.find { it.subjectCode == subject.uppercase() && it.catalogNumber == courseNumber }

                                    if (specificCourse != null) {
                                        Text(
                                            text = "${specificCourse.subjectCode} ${specificCourse.catalogNumber}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 30.sp,
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = specificCourse.title,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 22.sp,
                                            textAlign = TextAlign.Center
                                        )
                                        title = specificCourse.title
                                        if (specificCourse.requirementsDescription != null) { // this is needed
                                            Text(
                                                text = specificCourse.requirementsDescription,
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 15.sp,
                                                modifier = Modifier
                                                    .padding(bottom = 16.dp),
                                                textAlign = TextAlign.Center
                                            )
                                            requirements = specificCourse.requirementsDescription
                                        }

                                        val courseID = specificCourse.courseId

                                        // Display class Schedule info from UW Open API
                                        if (viewModel.apiErrorMessage.isEmpty()) {
                                            if (viewModel.classScheduleInfo.isNotEmpty()) {
                                                val specificCourseLectureInfoAPI = viewModel.classScheduleInfo
                                                for (specificCourseLectureInfo in specificCourseLectureInfoAPI) {
                                                    if (specificCourseLectureInfo.courseId == courseID) {
                                                        if (specificCourseLectureInfo.scheduleData.isEmpty()) {
                                                            Text(text = "Course schedule not found")
                                                            break
                                                        }
                                                        val scheduleData = specificCourseLectureInfo.scheduleData[0]
                                                        val classMeetingDayPatternCode = scheduleData.classMeetingDayPatternCode
                                                        if (classMeetingDayPatternCode.isNullOrEmpty()) { // this is needed
                                                            Text(text = "Course schedule not found")
                                                            break
                                                        }
                                                        val lectureDays = mapDayCodeToDayNames(classMeetingDayPatternCode)

                                                        val classMeetingStartTime = scheduleData.classMeetingStartTime
                                                        if (classMeetingStartTime.isNullOrEmpty()) { // this is needed
                                                            Text(text = "Course schedule not found")
                                                            break
                                                        }
                                                        val startTime = classMeetingStartTime.split("T")[1]
                                                        val startTimeWithoutSeconds = startTime.substring(0, 5)

                                                        val classMeetingEndTime = scheduleData.classMeetingEndTime
                                                        if (classMeetingEndTime.isNullOrEmpty()) { // this is needed
                                                            Text(text = "Course schedule not found")
                                                            break
                                                        }
                                                        val endTime = classMeetingEndTime.split("T")[1]
                                                        val endTimeWithoutSeconds = endTime.substring(0, 5)

                                                        val courseComponent = specificCourseLectureInfo.courseComponent
                                                        if (courseComponent.isNullOrEmpty()) { // this is needed
                                                            Text(text = "Course schedule not found")
                                                            break
                                                        }
                                                        Text(
                                                            text = "$courseComponent: ${lectureDays.joinToString(", ")}: $startTimeWithoutSeconds - $endTimeWithoutSeconds",
                                                            fontWeight = FontWeight.Normal,
                                                            fontSize = 18.sp,
                                                            textAlign = TextAlign.Left
                                                        )
                                                        lectureDateTime = courseComponent + ": " + lectureDays.joinToString(", ") + startTimeWithoutSeconds + "-" + endTimeWithoutSeconds
                                                        if (lectureDateTime !in lectureDateTimeSet) {
                                                            lectureDateTimeSet.add(lectureDateTime + "\n")
                                                        }
                                                        Log.d("LECTUREDATETIME", lectureDateTime)
                                                        Log.d("LECTUREDATETIMELIST", lectureDateTimeSet.toString())
                                                    } else {
                                                        Text(text = "Course schedule not found")
                                                    }
                                                }
                                            } else {
                                                Text(text = "Course schedule not found")
                                            }
                                        } else {
                                            Text(text = "Course schedule not found")
                                        }
                                    }
                                } else {
                                    Text(text = "Course not found")
                                }
                            } else {
                                Text(text = "Course not found")
                            }

                            // Input fields for instructor name and lecture location
                            var instructorName by remember { mutableStateOf("") }
                            var lectureLocation by remember { mutableStateOf("") }

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

                            val courseName = subject.uppercase() + courseNumber
                            val sortedLectureDateTimes = lectureDateTimeSet.sorted()
                            Button(
                                onClick = {
                                    viewModel.addCourse(
                                        courseName,
                                        lectureLocation,
                                        sortedLectureDateTimes.joinToString(separator = "\n"),
                                        instructorName,
                                        title,
                                        requirements
                                    )
                                    isPopupVisible = false
                                },
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

    private fun mapDayCodeToDayNames(dayCode: String): List<String> {
        val dayNames = mutableListOf<String>()

        for (char in dayCode) {
            val dayName = when (char) {
                'M' -> "Mondays"
                'T' -> "Tuesdays"
                'W' -> "Wednesdays"
                'R' -> "Thursdays"
                'F' -> "Fridays"
                else -> "Unknown"
            }
            dayNames.add(dayName)
        }

        return dayNames
    }

