package com.example.cs346project.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs346project.models.TodoItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class TodoViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _todoState = MutableStateFlow<List<TodoItem>>(emptyList())
    val todoState = _todoState.asStateFlow()

    suspend fun fetchTodoList(termUUID: String, CourseUUID: String) {
        viewModelScope.launch {
            val user = auth.currentUser
            if (user != null) {
                val todoDocuments = db.collection("Users")
                    .document(user.uid)
                    .collection("Terms")
                    .document(termUUID)
                    .collection("Courses")
                    .document(CourseUUID)
                    .collection("Todo")
                    .get()
                    .await()

                val todoList = todoDocuments.documents.mapNotNull { document ->
                    val todoItem = document.toObject(TodoItem::class.java)
                    todoItem?.copy(UUID = document.id) // Set the document ID
                }
                _todoState.value = todoList
            }
        }
    }


    fun addTodoToFirestore(termUUID: String, courseUUID: String, todoTitle: String, isCompleted: Boolean) {
        viewModelScope.launch {
            val user = auth.currentUser
            if (user != null) {
                val todoUUID = UUID.randomUUID().toString()
                try {
                    val todoMap = hashMapOf(
                        "UUID" to todoUUID,
                        "title" to todoTitle,
                        "completed" to isCompleted
                    )

                    db.collection("Users")
                        .document(user.uid)
                        .collection("Terms")
                        .document(termUUID)
                        .collection("Courses")
                        .document(courseUUID)
                        .collection("Todo")
                        .document(todoUUID)
                        .set(todoMap)
                        .await()

                } catch (e: Exception){
                    e.printStackTrace()
                }

                // Optionally, fetch the updated todolist
                fetchTodoList(termUUID, courseUUID)
            }
        }
    }

    fun updateTodoCompletion(termUUID: String, courseUUID: String, todo: TodoItem, isCompleted: Boolean) {
        viewModelScope.launch {
            val user = auth.currentUser
            if (user != null && todo.UUID.isNotEmpty()) {
                val todoMap = hashMapOf(
                    "UUID" to todo.UUID,
                    "title" to todo.title,
                    "completed" to isCompleted
                )

                Log.d("TODO_ID", todo.UUID)

                // Update the todoItem's isCompleted field in Firestore
                db.collection("Users")
                    .document(user.uid)
                    .collection("Terms")
                    .document(termUUID)
                    .collection("Courses")
                    .document(courseUUID)
                    .collection("Todo")
                    .document(todo.UUID)
                    .set(todoMap)
                    .await()

                // Optionally, fetch the updated todolist
                fetchTodoList(termUUID, courseUUID)
            }
        }
    }

}