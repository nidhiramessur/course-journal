package com.example.cs346project

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class GetCourses {

    private val db = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser

    fun fetchTerms(completion: (List<String>) -> Unit) {
        user?.let { user ->
            db.collection("Users").document(user.uid)
                .collection("Terms")
                .get()
                .addOnSuccessListener { documents ->
                    val termNames = documents.mapNotNull { it.getString("name") }
                    completion(termNames)
                }
                .addOnFailureListener { e ->
                    // Handle any errors here
                }
        }
    }

    fun fetchCoursesForTerm(termUUID: String, completion: (List<String>) -> Unit) {
        user?.let { user ->
            db.collection("Users").document(user.uid)
                .collection("Terms").document(termUUID)
                .collection("Courses")
                .get()
                .addOnSuccessListener { documents ->
                    val courseNames = documents.mapNotNull { it.getString("name") }
                    completion(courseNames)
                }
                .addOnFailureListener { e ->
                    // Handle any errors here
                }
        }
    }

    fun fetchNotesForCourse(termUUID: String, courseUUID: String, completion: (List<String>) -> Unit) {
        user?.let { user ->
            db.collection("Users").document(user.uid)
                .collection("Terms").document(termUUID)
                .collection("Courses").document(courseUUID)
                .get()
                .addOnSuccessListener { document ->
                    val notes = document.get("notes") as? List<String> ?: listOf()
                    completion(notes)
                }
                .addOnFailureListener { e ->
                    // Handle any errors here
                }
        }
    }

    fun fetchTodosForCourse(termUUID: String, courseUUID: String, completion: (List<String>) -> Unit) {
        user?.let { user ->
            db.collection("Users").document(user.uid)
                .collection("Terms").document(termUUID)
                .collection("Courses").document(courseUUID)
                .get()
                .addOnSuccessListener { document ->
                    val todos = document.get("todo") as? List<String> ?: listOf()
                    completion(todos)
                }
                .addOnFailureListener { e ->
                    // Handle any errors here
                }
        }
    }

    fun fetchAllUniqueCourses(completion: (List<String>) -> Unit) {
        user?.let { user ->
            db.collectionGroup("Courses").whereEqualTo("__name__", user.uid)
                .get()
                .addOnSuccessListener { documents ->
                    val courseNames = documents.mapNotNull { it.getString("name") }.distinct()
                    completion(courseNames)
                }
                .addOnFailureListener { e ->
                    // Handle any errors here
                }
        }
    }

    
}
