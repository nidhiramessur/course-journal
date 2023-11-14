package com.example.cs346project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID


class CourseInfoDisplayActivity : AppCompatActivity() {

    // Use this to track the current term's UUID for the user
    private var currentTermUUID: String by mutableStateOf("")
    // Use this to track the last course's UUID for the user
    private var currentCourseUUID: String by mutableStateOf("")

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

    @Composable
    fun CourseInput(courseData: CourseData) {

        var subject by remember { mutableStateOf("") }
        var courseNumber by remember { mutableStateOf("") }
        var isPopupVisible by remember { mutableStateOf(false) }
        val viewModel: CourseInfoViewModel = viewModel()
        var isLoading by remember { mutableStateOf(false) }

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
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            IconButton(
                                onClick = { isPopupVisible = false },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                            }


                            LaunchedEffect(Unit, block = {
                                isLoading = true
                                viewModel.getCourseInfoAPIData()
                                viewModel.getClassScheduleInfoAPIData(subject, courseNumber)
                                isLoading = false
                            })

                            // Display loading indicator if the API call is in progress
                            if (isLoading) {
                                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                            }

                            // Display course info from UW Open API if available
                            if (viewModel.errorMessage.isEmpty()) {
                                val specificCourse = viewModel.courseInfo.find { it.subjectCode == subject.uppercase() && it.catalogNumber == courseNumber }

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
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text =  specificCourse.requirementsDescription,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 15.sp,
                                        modifier = Modifier
                                            .padding(bottom = 16.dp),
                                        textAlign = TextAlign.Center
                                    )

                                    val courseID = specificCourse.courseId

                                    var lastCourseComponent: String? = null
                                    var lastLectureDay: List<String>? = null

                                    val specificCourseLectureInfoAPI = viewModel.classScheduleInfo
                                    // Display class Schedule info from UW Open API
                                    for (specificCourseLectureInfo in specificCourseLectureInfoAPI) {
                                        if (specificCourseLectureInfo.courseId == courseID) {
                                            val scheduleData = specificCourseLectureInfo.scheduleData[0]
                                            val classMeetingDayPatternCode = scheduleData.classMeetingDayPatternCode
                                            if (classMeetingDayPatternCode.isEmpty()) {
                                                Text(text = "Course schedule not found")
                                                break
                                            }
                                            val lectureDays = mapDayCodeToDayNames(classMeetingDayPatternCode)

                                            val classMeetingStartTime = scheduleData.classMeetingStartTime
                                            val startTime = classMeetingStartTime.split("T")[1]
                                            val startTimeWithoutSeconds = startTime.substring(0, 5)

                                            val classMeetingEndTime = scheduleData.classMeetingEndTime
                                            val endTime = classMeetingEndTime.split("T")[1]
                                            val endTimeWithoutSeconds = endTime.substring(0, 5)

                                            val courseComponent = specificCourseLectureInfo.courseComponent

                                            // Display course component once if it repeats
                                            if (courseComponent != lastCourseComponent) {
                                                Text(
                                                    text = "$courseComponent:",
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 18.sp,
                                                    textAlign = TextAlign.Left
                                                )
                                                lastCourseComponent = courseComponent
                                            }

                                            // Display lecture day once if it repeats and corresponding lecture times
                                            if (lectureDays != lastLectureDay) {
                                                Text(
                                                    text = "${lectureDays.joinToString(", ")}:",
                                                    fontWeight = FontWeight.Normal,
                                                    fontSize = 18.sp,
                                                    textAlign = TextAlign.Center
                                                )
                                                lastLectureDay = lectureDays
                                            }
                                            Text(
                                                text = "$startTimeWithoutSeconds - $endTimeWithoutSeconds",
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 18.sp,
                                                textAlign = TextAlign.Center
                                            )
                                        } else {
                                            Text(text = "Course schedule not found")
                                        }
                                    }
                                } else {
                                    if (!isLoading) {
                                        Text(text = "Course not found")
                                    }
                                }
                            } else {
                                Text(text = "error fetching API data: ${viewModel.errorMessage}")
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

                            val courseName = subject + courseNumber
                            Button(
                                onClick = {
                                    addCourse(courseName)
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

    private fun addCourse(courseName: String) {
        // Temporarily hardcoding the term UUID - "Fall 2023" in db (user test4)
        // Every course added will go under the Fall 2023 term for user test4
        currentTermUUID = "5ee51a2c-022a-46f5-851e-558ad9a14a05"

        // Real functionality is when the user is on the term page and clicks on add course
        // it will navigate to this activity where the user can search for their course
        // and add the course to the term page
        // workflow: user signs in, goes to term page, clicks on add course, gets navigated to this page
        // in the "add course" button on the term page, it should setCurrentTermUUID
        // and we can get rid of the above line

        val courseUUID = UUID.randomUUID().toString()
        currentCourseUUID = courseUUID

        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val courseMap = hashMapOf(
                "UUID" to courseUUID,
                "name" to courseName,
                "notes" to listOf<String>(),
                "todo" to listOf<String>()
            )

            db.collection("Users").document(it.uid)
                .collection("Terms").document(currentTermUUID)
                .collection("Courses").document(courseUUID)
                .set(courseMap)
                .addOnSuccessListener {
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error adding course document", e)
                }
        }
    }

}