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

    suspend fun getCourseInfoDatas() {
        try {
            withContext(Dispatchers.IO) {
                Log.d("API_REQUEST", "Before API call")
                _courseInfo = APIService.apiService.getCourseInfoData().toMutableList()
                Log.d("JSON_RESPONSE", courseInfo.toString())
            }
        } catch (e: Exception) {
            Log.e("API_REQUEST", "Error: ${e.message}")
            errorMessage = e.message.toString()
        }
    }


}