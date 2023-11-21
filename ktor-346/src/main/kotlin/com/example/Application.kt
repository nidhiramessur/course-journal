package com.example

import com.example.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import java.io.FileInputStream


fun main() {
    // Initialize Firebase here
   val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(FileInputStream("/app/firebase-key.json")))
        .build()
    FirebaseApp.initializeApp(options)

    // Rest of your server setup
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}



fun Application.module() {
    configureSockets()
    configureSerialization()
    configureSecurity()
    configureRouting()
}
