package com.example.cs346project

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log

class UserFetchTermCoursesActivity : AppCompatActivity() {

    private lateinit var termsSpinner: Spinner
    private lateinit var coursesTextView: TextView

    // This will hold the list of term names and their UUIDs
    private val termsMap = hashMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firestore)

        termsSpinner = findViewById(R.id.termsSpinner) 
        coursesTextView = findViewById(R.id.coursesTextView) 

        loadTerms()
    }

    private fun loadTerms() {
        val user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()

        user?.let { firebaseUser ->
            db.collection("Users").document(firebaseUser.uid)
                .collection("Terms")
                .get()
                .addOnSuccessListener { documents ->
                    val termNames = documents.mapNotNull { it.getString("name") }
                    val spinnerArrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, termNames)
                    termsSpinner.adapter = spinnerArrayAdapter
                    
                    documents.forEach { document ->
                        termsMap[document.getString("name") ?: ""] = document.id
                    }
                    
                    termsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                            val termName = parent.getItemAtPosition(position).toString()
                            termsMap[termName]?.let { uuid ->
                                loadCoursesForTerm(uuid)
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            coursesTextView.text = "Please select a term."
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error loading terms", e)
                }
        }
    }

    private fun loadCoursesForTerm(termUUID: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()

        user?.let { firebaseUser ->
            db.collection("Users").document(firebaseUser.uid)
                .collection("Terms").document(termUUID)
                .collection("Courses")
                .get()
                .addOnSuccessListener { documents ->
                    val courseInfo = documents.mapNotNull { document ->
                        val courseName = document.getString("name") ?: "Unknown"
                        val notesCount = (document.get("notes") as? List<*>)?.size ?: 0
                        val todoCount = (document.get("todo") as? List<*>)?.size ?: 0
                        "$courseName - Notes: $notesCount, Todos: $todoCount"
                    }.joinToString("\n")
                    coursesTextView.text = courseInfo
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error loading courses for term $termUUID", e)
                }
        }
    }
}
