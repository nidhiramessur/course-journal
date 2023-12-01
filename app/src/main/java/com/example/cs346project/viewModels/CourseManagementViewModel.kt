package com.example.cs346project.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs346project.CourseInfoDbData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CourseManagementViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _courseInfoState = MutableStateFlow<List<CourseInfoDbData>>(emptyList())
    val courseInfoState = _courseInfoState.asStateFlow()

    private suspend fun fetchTermUUIDByName(termName: String): String? {
        val user = auth.currentUser
        if (user != null) {
            val termQuerySnapshot = db
                .collection("Users")
                .document(user.uid)
                .collection("Terms")
                .whereEqualTo("name", termName)
                .get()
                .await()

            return termQuerySnapshot.documents.firstOrNull()?.id
        }
        return null
    }

    private suspend fun fetchCourseUUIDByName(termUUID: String, courseName: String): String? {
        val user = auth.currentUser
        if (user != null) {
            val courseQuerySnapshot = db
                .collection("Users")
                .document(user.uid)
                .collection("Terms")
                .document(termUUID)
                .collection("Courses")
                .whereEqualTo("name", courseName)
                .get()
                .await()

            return courseQuerySnapshot.documents.firstOrNull()?.id
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
                            // Fetch a single document from Firestore
                            val documentSnapshot = db
                                .collection("Users")
                                .document(user.uid)
                                .collection("Terms")
                                .document(termUUID)
                                .collection("Courses")
                                .document(courseUUID)
                                .get()
                                .await()

                            // Check if the document exists and map it to CourseInfoDbData object
                            val courseInfo: CourseInfoDbData? = if (documentSnapshot.exists()) {
                                documentSnapshot.toObject(CourseInfoDbData::class.java)
                            } else {
                                null
                            }

                            // Update the state; if the course is not found, update with an empty list or handle accordingly
                            _courseInfoState.value = courseInfo?.let { listOf(it) } ?: emptyList()
                        }
                    }
                }
            } catch (e: Exception) {
                // Handle exceptions such as network issues or Firebase errors
                e.printStackTrace()
                // Update the state to indicate an error, if necessary
            }
        }
    }

}