package com.example.cs346project.viewModels


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class TermViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _coursesState = MutableStateFlow<List<String>>(emptyList())
    val coursesState = _coursesState.asStateFlow()

    private val _termsState = MutableStateFlow<List<String>>(emptyList())
    val termsState = _termsState.asStateFlow()

    fun fetchTermUUIDFromName(termName: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val user = auth.currentUser
                if (user != null) {
                    // Query to find the term UUID based on term name
                    val termQuerySnapshot = db
                        .collection("Users")
                        .document(user.uid)
                        .collection("Terms")
                        .whereEqualTo("name", termName) // Assuming 'name' is the field for term name
                        .get()
                        .await()

                    // Assuming that term names are unique and there's only one document
                    val termUUID = termQuerySnapshot.documents.firstOrNull()?.id
                    if (termUUID != null) {
                        onResult(termUUID)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchCoursesForTerm(termUUID: String) {
        viewModelScope.launch {
            val user = auth.currentUser
            if (user != null) {
                val documents = db
                    .collection("Users")
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

    suspend fun fetchTerms() {
        viewModelScope.launch {
            val user = auth.currentUser
            if (user != null) {
                val documents = db.collection("Users")
                    .document(user.uid)
                    .collection("Terms")
                    .get()
                    .await()

                val termList = documents.documents.mapNotNull { it.getString("name") }
                _termsState.value = termList
            }
        }
    }

    fun addTerm(termName: String) {
        // Temporarily hardcoding the term UUID - "Fall 2023" in db (user test4)
        // Every course added will go under the Fall 2023 term for user test4
//        val currentTermUUID = "5ee51a2c-022a-46f5-851e-558ad9a14a05"

        // Real functionality is when the user is on the term page and clicks on add course
        // it will navigate to this activity where the user can search for their course
        // and add the course to the term page
        // workflow: user signs in, goes to term page, clicks on add course, gets navigated to this page
        // in the "add course" button on the term page, it should setCurrentTermUUID
        // and we can get rid of the above line

//        val courseUUID = UUID.randomUUID().toString()
//        val currentCourseUUID = courseUUID
        val termUUID = UUID.randomUUID().toString()

        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val termMap = hashMapOf(
                "UUID" to termUUID,
                "name" to termName
            )

            db.collection("Users").document(it.uid)
                .collection("Terms").document(termUUID)
                .set(termMap)
                .addOnSuccessListener {
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error adding course document", e)
                }
        }
    }
}