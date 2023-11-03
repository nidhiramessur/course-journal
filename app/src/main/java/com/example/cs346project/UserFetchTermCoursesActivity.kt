package com.example.cs346project

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import java.util.UUID
import android.util.Log

class UserFetchTermCoursesActivity : AppCompatActivity() {

    private lateinit var termInput: EditText
    private lateinit var courseInput: EditText
    private lateinit var noteInput: EditText
    private lateinit var todoInput: EditText

    // Use this to track the current term's UUID for the user
    private var currentTermUUID: String = ""

    // Use this to track the last course's UUID for the user
    private var currentCourseUUID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firestore)

        termInput = findViewById(R.id.termInput)
        courseInput = findViewById(R.id.courseInput)
        noteInput = findViewById(R.id.noteInput)
        todoInput = findViewById(R.id.todoInput)

        findViewById<Button>(R.id.addTermButton).setOnClickListener { addTerm() }
        findViewById<Button>(R.id.addCourseButton).setOnClickListener { addCourse() }
        findViewById<Button>(R.id.addNoteButton).setOnClickListener { addNote() }
        findViewById<Button>(R.id.addTodoButton).setOnClickListener { addTodo() }
    }

    private fun addTerm() {
        val termName = termInput.text.toString()
        val termUUID = UUID.randomUUID().toString()
        currentTermUUID = termUUID

        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val termMap = hashMapOf(
                "UUID" to termUUID,
                "name" to termName,
                "courses" to listOf<HashMap<String, Any>>()  // This will hold courses
            )

            db.collection("Users").document(it.uid)
                .collection("Terms").document(termUUID)
                .set(termMap)
                .addOnSuccessListener {
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error adding term document", e)
                }
        }
    }

    private fun addCourse() {
        val courseName = courseInput.text.toString()
        val courseUUID = UUID.randomUUID().toString()
        currentCourseUUID = courseUUID

        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val courseMap = hashMapOf(
                "UUID" to courseUUID,
                "name" to courseName,
                "notes" to listOf<String>(), 
                "todo" to listOf<String>()  
            )

            db.collection("Users").document(it.uid)
                .collection("Terms").document(currentTermUUID)
                .collection("Courses").document(courseUUID)
                .set(courseMap)
                .addOnSuccessListener {
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error adding course document", e)
                }
        }
    }

    private fun addNote() {
        val note = noteInput.text.toString()

        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            db.collection("Users").document(it.uid)
                .collection("Terms").document(currentTermUUID)
                .collection("Courses").document(currentCourseUUID)
                .update("notes", FieldValue.arrayUnion(note))
                .addOnSuccessListener {
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error adding note", e)
                }
        }
    }

    private fun addTodo() {
        val todo = todoInput.text.toString()

        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            db.collection("Users").document(it.uid)
                .collection("Terms").document(currentTermUUID)
                .collection("Courses").document(currentCourseUUID)
                .update("todo", FieldValue.arrayUnion(todo))
                .addOnSuccessListener {
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error adding todo", e)
                }
        }
    }
}

