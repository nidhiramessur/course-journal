package com.example.cs346project.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.cs346project.APIService
import com.example.cs346project.ClassScheduleData
import com.example.cs346project.CourseInfoData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.UUID

class CourseInfoViewModel : ViewModel() {

    private var _courseInfo = mutableListOf<CourseInfoData>()
    var errorMessage: String by mutableStateOf("")
    val courseInfo: MutableList<CourseInfoData>
        get() = _courseInfo

    private var _classScheduleInfo = mutableListOf<ClassScheduleData>()
    var apiErrorMessage: String by mutableStateOf("")
    var classScheduleInfo: MutableList<ClassScheduleData>
        get() = _classScheduleInfo
        set(value) {
            _classScheduleInfo = value
        }

    suspend fun getCourseInfoAPIData(subject: String, courseNumber: String) {
        try {
            withContext(Dispatchers.IO) {
                Log.d("API_REQUEST 1", "Before API call")
                _courseInfo = APIService.apiService.getCourseInfo(subject.lowercase(), courseNumber).toMutableList()
                delay(1000)
                Log.d("JSON_RESPONSE 1", courseInfo.toString())
            }
        } catch (e: Exception) {
            Log.e("API_REQUEST 1", "Error: ${e.message}")
            errorMessage = e.message.toString()
        }
    }

    suspend fun getClassScheduleInfoAPIData(subject: String, courseNumber: String) {
        try {
            withContext(Dispatchers.IO) {
                Log.d("API_REQUEST 2", "Before API call $subject $courseNumber")
                val response = APIService.apiService.getClassScheduleInfo(subject.lowercase(), courseNumber)
                delay(1000)
                // Log the raw JSON response
                Log.d("CLASS SCHEDULE 2", classScheduleInfo.toString())

                // Convert the response to a mutable list
                _classScheduleInfo = response.toMutableList()
                Log.d("CLASS SCHEDULE 2", classScheduleInfo.toString())
            }
        } catch (e: Exception) {
            Log.e("API_REQUEST 2", "Error: ${e.message}")
            apiErrorMessage = e.message.toString()
        }
    }

    fun addCourse(
        courseName: String,
        lectureLocation: String,
        lectureDateTime: String,
        instructorName: String,
        title: String,
        requirements: String
    ) {
        // Temporarily hardcoding the term UUID - "Fall 2023" in db (user test4)
        // Every course added will go under the Fall 2023 term for user test4
        val currentTermUUID = "5ee51a2c-022a-46f5-851e-558ad9a14a05"

        // Real functionality is when the user is on the term page and clicks on add course
        // it will navigate to this activity where the user can search for their course
        // and add the course to the term page
        // workflow: user signs in, goes to term page, clicks on add course, gets navigated to this page
        // in the "add course" button on the term page, it should setCurrentTermUUID
        // and we can get rid of the above line

        val courseUUID = UUID.randomUUID().toString()
//        val currentCourseUUID = courseUUID

        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val courseMap = hashMapOf(
                "UUID" to courseUUID,
                "lecturedatetime" to lectureDateTime,
                "lecturelocation" to lectureLocation,
                "name" to courseName,
                "title" to title,
                "requirements" to requirements,
                "notes" to listOf<String>(),
                "professorname" to instructorName,
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