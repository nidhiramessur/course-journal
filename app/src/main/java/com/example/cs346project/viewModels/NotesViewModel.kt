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
import java.util.UUID


class NotesViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _notesState = MutableStateFlow<List<Note>>(emptyList())
    val notesState = _notesState.asStateFlow()

    fun fetchCourseNotes(termUUID: String?, CourseUUID: String?) {
        viewModelScope.launch {
            val user = auth.currentUser
            if (user != null && termUUID != null && CourseUUID !=null) {
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
                    val nuid = document.getString("nuid")
                    data?.let { Note(data, nuid ?: "") }
                }

                _notesState.value = notesList
            }
        }
    }

    fun addNote(termUUID: String?, CourseUUID: String?,note: String){
        viewModelScope.launch {
            val user = auth.currentUser
            if ((user != null && termUUID != null && CourseUUID !=null)) {
                val notesUID = UUID.randomUUID().toString()
                try {
                    val mynote = mapOf("data" to note, "nuid" to notesUID)

                    db.collection("Users")
                        .document(user.uid)
                        .collection("Terms")
                        .document(termUUID)
                        .collection("Courses")
                        .document(CourseUUID)
                        .collection("Notes")
                        .document(notesUID)
                        .set(mynote)
                        .await()
                } catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
    }

    fun updateNote(termUUID: String?, CourseUUID: String?,note: Note) {
        viewModelScope.launch {
            val user = auth.currentUser
            if ((user != null && termUUID != null && CourseUUID !=null)) {
                try {
                    db.collection("Users")
                        .document(user.uid)
                        .collection("Terms")
                        .document(termUUID)
                        .collection("Courses")
                        .document(CourseUUID)
                        .collection("Notes")
                        .document(note.nuid)
                        .set(note)
                        .await()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun deleteNote(termUUID: String?, CourseUUID: String?,note: Note) {
        viewModelScope.launch {
            val user = auth.currentUser
            if ((user != null && termUUID != null && CourseUUID !=null)) {
                try {
                    db.collection("Users")
                        .document(user.uid)
                        .collection("Terms")
                        .document(termUUID)
                        .collection("Courses")
                        .document(CourseUUID)
                        .collection("Notes")
                        .document(note.nuid)
                        .delete()
                        .await()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}