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

        get("/users/{userId}/username") {
            val userId = call.parameters["userId"] ?: return@get call.respondText("User ID is required", status = HttpStatusCode.BadRequest)

            try {
                val firestore = FirestoreClient.getFirestore()
                val userDocument = firestore.collection("Users").document(userId).get().get()
                val username = userDocument.getString("username") ?: "Username not found"

                call.respondText("Username: $username", status = HttpStatusCode.OK)
            } catch (e: Exception) {
                application.log.error("Error fetching username for user $userId", e)
                call.respondText("Error fetching username: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // Route to fetch the email of a user
        get("/users/{userId}/email") {
            val userId = call.parameters["userId"] ?: return@get call.respondText("User ID is required", status = HttpStatusCode.BadRequest)

            try {
                val userRecord = FirebaseAuth.getInstance().getUser(userId)
                val email = userRecord.email ?: "Email not found"

                call.respondText("Email: $email", status = HttpStatusCode.OK)
            } catch (e: Exception) {
                application.log.error("Error fetching email for user $userId", e)
                call.respondText("Error fetching email: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
            }
        }

        post("/signup") {
            val (email, password) = call.receive<Pair<String, String>>()
            try {
                val userRecord = FirebaseAuth.getInstance().createUser(CreateRequest()
                    .setEmail(email)
                    .setPassword(password)
                )
                // Additional Firestore data storage can be added here if required
                call.respondText("Signup successful: ${userRecord.uid}", status = HttpStatusCode.OK)
            } catch (e: Exception) {
                application.log.error("Signup failed", e)
                call.respondText("Signup failed: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // Route for user login
        post("/login") {
            val (email, password) = call.receive<Pair<String, String>>()
            try {
                // Firebase Authentication logic for login
                val userRecord = FirebaseAuth.getInstance().getUserByEmail(email)
                // Here you can validate the password (this part is not straightforward with Firebase, usually handled on the client side)
                // For now, assuming password check is done, return success response
                call.respondText("Login successful: ${userRecord.uid}", status = HttpStatusCode.OK)
            } catch (e: Exception) {
                application.log.error("Login failed", e)
                call.respondText("Login failed: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }
    }
}
