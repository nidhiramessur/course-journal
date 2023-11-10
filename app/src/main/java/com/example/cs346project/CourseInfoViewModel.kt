package com.example.cs346project

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CourseInfoViewModel: ViewModel() {

    private var _courseInfo = mutableListOf<CourseInfoData>()
    var errorMessage: String by mutableStateOf("")
    val courseInfo: MutableList<CourseInfoData>
        get() = _courseInfo

    private var _courseAPIInfo = mutableListOf<CourseInfoAPIData>()
    var apiErrorMessage: String by mutableStateOf("")
    val courseAPIInfo: MutableList<CourseInfoAPIData>
        get() = _courseAPIInfo

    suspend fun getCourseInfoSomeData() {
        try {
            withContext(Dispatchers.IO) {
                Log.d("API_REQUEST 1", "Before API call")
                _courseInfo = APIService.apiService.getCourseInfoDataFromGET().toMutableList()
                Log.d("JSON_RESPONSE 1", courseInfo.toString())
            }
        } catch (e: Exception) {
            Log.e("API_REQUEST 1", "Error: ${e.message}")
            errorMessage = e.message.toString()
        }
    }

    suspend fun getCourseInfoAPIData() {
        try {
            withContext(Dispatchers.IO) {
                Log.d("API_REQUEST 2", "Before API call")
                val response = APIService.apiService.getCourseInfoAPIDataFromGET()
                // Log the raw JSON response
                Log.d("RAW_JSON_RESPONSE 2", response.toString())

                // Convert the response to a mutable list
                _courseAPIInfo = response.toMutableList()
                Log.d("JSON_RESPONSE 2", courseAPIInfo.toString())
            }
        } catch (e: Exception) {
            Log.e("API_REQUEST 2", "Error: ${e.message}")
            apiErrorMessage = e.message.toString()
        }
    }


}