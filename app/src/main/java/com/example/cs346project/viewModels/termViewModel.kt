package com.example.cs346project.viewModels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TermViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _coursesState = MutableStateFlow<List<String>>(emptyList())
    val coursesState = _coursesState.asStateFlow()

    suspend fun fetchCoursesForTerm(termUUID: String) {
        viewModelScope.launch {
            val user = auth.currentUser
            if (user != null) {
                val documents = db.collection("Users")
                    .document(user.uid)
                    .collection("Terms")
                    .document(termUUID)
                    .collection("Courses")
                    .get()
                    .await()

                val courseList = documents.documents.mapNotNull { it.getString("name") }
                _coursesState.value = courseList
            }
        }
    }
}