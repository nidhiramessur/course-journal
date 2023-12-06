package com.example.cs346project.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.cs346project.APIService
import com.example.cs346project.ClassScheduleData
import com.example.cs346project.CourseInfoAPIData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.util.UUID

class CourseInfoViewModel : ViewModel() {

    private var _courseInfo = mutableListOf<CourseInfoAPIData>()
    private val _errorMessage = MutableStateFlow<String>("")
    val errorMessage = _errorMessage.asStateFlow()

    val courseInfo: MutableList<CourseInfoAPIData>
        get() = _courseInfo

    private var _classScheduleInfo = mutableListOf<ClassScheduleData>()
    private val _apiErrorMessage = MutableStateFlow<String>("")
    val apiErrorMessage = _apiErrorMessage.asStateFlow()
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
            _errorMessage.value = e.message.toString()
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
            _apiErrorMessage.value = e.message.toString()
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
        val user = FirebaseAuth.getInstance().currentUser
        user?.let { firebaseUser ->
            FirebaseFirestore.getInstance().collection("Users").document(firebaseUser.uid).get()
                .addOnSuccessListener { document ->
                    val currentTermUUID = document.getString("currentTerm") ?: return@addOnSuccessListener
                    // Proceed with adding the course under this term UUID
                    addCourseToTerm(courseName, lectureLocation, lectureDateTime, instructorName, title, requirements, currentTermUUID)
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error fetching current term UUID", e)
                }
        }
    }

    private fun addCourseToTerm(
        courseName: String,
        lectureLocation: String,
        lectureDateTime: String,
        instructorName: String,
        title: String,
        requirements: String,
        termUUID: String
    ) {
        val courseUUID = UUID.randomUUID().toString()
        val db = FirebaseFirestore.getInstance()

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

        db.collection("Users").document(FirebaseAuth.getInstance().currentUser?.uid ?: "")
            .collection("Terms").document(termUUID)
            .collection("Courses").document(courseUUID)
            .set(courseMap)
            .addOnSuccessListener {
                // Handle success
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding course document", e)
            }
    }

}