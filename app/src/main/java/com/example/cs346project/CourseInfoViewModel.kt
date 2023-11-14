package com.example.cs346project

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

    suspend fun getCourseInfoAPIData() {
        try {
            withContext(Dispatchers.IO) {
                Log.d("API_REQUEST 1", "Before API call")
                _courseInfo = APIService.apiService.getCourseInfo().toMutableList()
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


}