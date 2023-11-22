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
    }


}
