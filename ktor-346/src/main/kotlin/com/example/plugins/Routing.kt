package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.google.firebase.cloud.FirestoreClient
import io.ktor.http.HttpStatusCode

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    

        get("/users/{userId}/terms/{termId}/courses") {
            val userId = call.parameters["userId"] ?: return@get call.respondText("User ID is required", status = HttpStatusCode.BadRequest)
            val termId = call.parameters["termId"] ?: return@get call.respondText("Term ID is required", status = HttpStatusCode.BadRequest)

            try {
                val firestore = FirestoreClient.getFirestore()
                val documents = firestore.collection("Users").document(userId)
                    .collection("Terms").document(termId)
                    .collection("Courses").get().get()
                
                val courseNames = documents.documents.mapNotNull { document ->
                    document.getString("name") ?: "Unknown"
                }
                
                call.respond(courseNames)
            } catch (e: Exception) {
                application.log.error("Error loading courses for term $termId", e)
                call.respondText("Error fetching courses: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        get("/users/{userId}/terms") {
            val userId = call.parameters["userId"] ?: return@get call.respondText("User ID is required", status = HttpStatusCode.BadRequest)

            try {
                val firestore = FirestoreClient.getFirestore()
                val termsQuerySnapshot = firestore.collection("Users").document(userId)
                    .collection("Terms").get().get()

                val termNames = termsQuerySnapshot.documents.mapNotNull { document ->
                    document.getString("name") ?: "Unknown Term Name"
                }

                call.respond(termNames)
            } catch (e: Exception) {
                application.log.error("Error loading terms for user $userId", e)
                call.respondText("Error fetching terms: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        get("/users/{userId}/terms/uuid/{termName}") {
            val userId = call.parameters["userId"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val termName = call.parameters["termName"] ?: return@get call.respond(HttpStatusCode.BadRequest)

            try {
                val firestore = FirestoreClient.getFirestore()
                val termQuerySnapshot = firestore.collection("Users").document(userId)
                    .collection("Terms").whereEqualTo("name", termName).get().get()
                
                val termUUID = termQuerySnapshot.documents.firstOrNull()?.id
                if (termUUID != null) {
                    call.respondText(termUUID)
                } else {
                    call.respondText("Term not found", status = HttpStatusCode.NotFound)
                }
            } catch (e: Exception) {
                application.log.error("Error fetching term UUID for term name $termName", e)
                call.respondText("Error fetching term UUID: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // Route to fetch the Course UUID by its name
        get("/users/{userId}/terms/{termId}/courses/uuid/{courseName}") {
            val userId = call.parameters["userId"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val termId = call.parameters["termId"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val courseName = call.parameters["courseName"] ?: return@get call.respond(HttpStatusCode.BadRequest)

            try {
                val firestore = FirestoreClient.getFirestore()
                val courseQuerySnapshot = firestore.collection("Users").document(userId)
                    .collection("Terms").document(termId)
                    .collection("Courses").whereEqualTo("name", courseName).get().get()

                val courseUUID = courseQuerySnapshot.documents.firstOrNull()?.id
                if (courseUUID != null) {
                    call.respondText(courseUUID)
                } else {
                    call.respondText("Course not found", status = HttpStatusCode.NotFound)
                }
            } catch (e: Exception) {
                application.log.error("Error fetching course UUID for course name $courseName", e)
                call.respondText("Error fetching course UUID: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        get("/users/{userId}/terms/{termId}/courses/{courseId}/info") {
            val userId = call.parameters["userId"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val termId = call.parameters["termId"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val courseId = call.parameters["courseId"] ?: return@get call.respond(HttpStatusCode.BadRequest)

            try {
                val firestore = FirestoreClient.getFirestore()
                val documentSnapshot = firestore.collection("Users").document(userId)
                    .collection("Terms").document(termId)
                    .collection("Courses").document(courseId).get().get()

                if (documentSnapshot.exists()) {
                    // The server would return a string that includes all the fields of the CourseInfoDbData
                    // joined by a separator, such as a pipe character '|'
                    val courseData = documentSnapshot.data?.map { "${it.key}:${it.value}" }?.joinToString("|")
                    call.respondText(courseData ?: "Data not found")
                } else {
                    call.respondText("Course not found", status = HttpStatusCode.NotFound)
                }
            } catch (e: Exception) {
                application.log.error("Failed to fetch course info", e)
                call.respondText("Failed to fetch course info: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

    }


}
