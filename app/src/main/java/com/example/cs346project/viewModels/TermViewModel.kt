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

    fun updateUserCurrentTerm(termUUID: String) {
        viewModelScope.launch {
            val user = auth.currentUser
            if (user != null) {
                db.collection("Users").document(user.uid)
                    .update("currentTerm", termUUID)
                    .addOnSuccessListener {
                        Log.d("Firestore", "Successfully updated current term to $termUUID")
                    }
                    .addOnFailureListener { e ->
                        Log.w("Firestore", "Error updating current term", e)
                    }
            } else {
                Log.w("TermViewModel", "No authenticated user found")
            }
        }
    }

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

    fun fetchCoursesUUIDFromName(termUUID: String, courseName:String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val user = auth.currentUser
                if (user != null) {
                    // Query to find the term UUID based on term name
                    val termQuerySnapshot = db
                        .collection("Users")
                        .document(user.uid)
                        .collection("Terms")
                        .document(termUUID)
                        .collection("Courses")
                        .whereEqualTo("name", courseName) // Assuming 'name' is the field for term name
                        .get()
                        .await()

                    // Assuming that term names are unique and there's only one document
                    val courseUUID = termQuerySnapshot.documents.firstOrNull()?.id
                    if (courseUUID != null) {
                        onResult(courseUUID)
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

    suspend fun fetchCurrentTermUUID(onResult: (String) -> Unit) {
        viewModelScope.launch {
            val user = auth.currentUser
            if (user != null) {
                val documents = db.collection("Users")
                    .document(user.uid)
                    .get()
                    .await()

                val currentTermUUID = documents.getString("currentTerm")
                Log.d("CURRENT_TERM_NAME", currentTermUUID?:"")
                if (currentTermUUID != null) {
                    onResult(currentTermUUID)
                }
            }
        }
    }

    fun fetchNamefromCurrentTermUUID(termUUID: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val user = auth.currentUser
                if (user != null) {
                    // Query to find the term UUID based on term name
                    val documents = db.collection("Users")
                        .document(user.uid)
                        .collection("Terms")
                        .document(termUUID)
                        .get()
                        .await()

                    val termName = documents.getString("name")
                    Log.d("CURRENT_TERM_NAME_NEW", termName?:"")
                    if (termName != null) {
                        onResult(termName)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addTerm(termName: String) {
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