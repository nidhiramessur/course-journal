package com.example.cs346project.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs346project.CourseInfoDbData
import com.google.firebase.auth.FirebaseAuth
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.encodeURLPath
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import android.util.Log


class CourseManagementViewModel : ViewModel() {

    public val httpClient = HttpClient()
    public val auth = FirebaseAuth.getInstance()

    public val _courseInfoState = MutableStateFlow<List<CourseInfoDbData>>(emptyList())
    val courseInfoState = _courseInfoState.asStateFlow()

    public suspend fun fetchTermUUIDByName(termName: String): String? {
    val user = auth.currentUser
    if (user != null) {
        // Encode the termName and replace "+" with "%20"
        val encodedTermName = URLEncoder.encode(termName, StandardCharsets.UTF_8.toString()).replace("+", "%20")
        val url = "http://10.0.2.2:8080/users/${user.uid}/terms/uuid/$encodedTermName"
        Log.d("CourseManagementViewModel", "Fetching Term UUID from URL: $url")

        return try {
            httpClient.get(url)
        } catch (e: Exception) {
            Log.e("CourseManagementViewModel", "Error fetching Term UUID: $e")
            e.printStackTrace()
            null
        }
    }
    return null
}


    public suspend fun fetchCourseUUIDByName(termUUID: String, courseName: String): String? {
        val user = auth.currentUser
        if (user != null) {
            // Encode the courseName to ensure the URL is correctly formatted.
            val encodedCourseName = URLEncoder.encode(courseName, StandardCharsets.UTF_8.toString())
            val url = "http://10.0.2.2:8080/users/${user.uid}/terms/$termUUID/courses/uuid/$encodedCourseName"
            return try {
                httpClient.get(url)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
        return null
    }
    
    suspend fun fetchCourseInfo(termName: String, courseName: String) {
        viewModelScope.launch {
            try {
                val user = auth.currentUser
                if (user != null) {
                    val termUUID = fetchTermUUIDByName(termName)
                    if (termUUID != null) {
                        val courseUUID = fetchCourseUUIDByName(termUUID, courseName)
                        if (courseUUID != null) {
                            val url = "http://10.0.2.2:8080/users/${user.uid}/terms/$termUUID/courses/$courseUUID/info"
                            val response: String = httpClient.get(url)
                            
                            // Assuming the response is a pipe-delimited string of key:value pairs
                            val courseInfo = parseCourseInfoData(response)
                            _courseInfoState.value = listOfNotNull(courseInfo)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _courseInfoState.value = emptyList()
            }
        }
    }

// Util function to parse the string response into CourseInfoDbData
    public fun parseCourseInfoData(data: String): CourseInfoDbData {
    val dataMap = data.split("|").associate {
        val parts = it.split(":")
        if (parts.size >= 2) parts[0] to parts[1] else parts[0] to ""
    }
    return CourseInfoDbData(
        name = dataMap["name"].orEmpty(),
        title = dataMap["title"].orEmpty(),
        requirements = dataMap["requirements"].orEmpty(),
        lecturedatetime = dataMap["lecturedatetime"].orEmpty(),
        lecturelocation = dataMap["lecturelocation"].orEmpty(),
        professorname = dataMap["professorname"].orEmpty(),
        courserating = dataMap["courserating"].orEmpty(),
        professorrating = dataMap["professorrating"].orEmpty(),
        // Handle arrays like todo and notes
        // todo = dataMap["todo"]?.split(",") ?: emptyList(),
        // notes = dataMap["notes"]?.split(",") ?: emptyList(),
        // UUID or any other fields can be added similarly
        // UUID = dataMap["UUID"].orEmpty()
    )
    }


}