package com.example.cs346project.viewModels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.example.cs346project.models.Note



class NotesViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _notesState = MutableStateFlow<List<Note>>(emptyList())
    val notesState = _notesState.asStateFlow()

    suspend fun fetchCourseNotes(termUUID: String, CourseUUID: String) {
        viewModelScope.launch {
            val user = auth.currentUser
            if (user != null) {
                val documents = db.collection("Users")
                    .document(user.uid)
                    .collection("Terms")
                    .document(termUUID)
                    .collection("Courses")
                    .document(CourseUUID)
                    .collection("Notes")
                    .get()
                    .await()

                val notesList = documents.documents.mapNotNull {document ->
                    val data = document.getString("data")
                    data?.let { Note(data) }
                }

                _notesState.value = notesList
            }
        }
    }
}