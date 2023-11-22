package com.example.model
import kotlinx.serialization.Serializable

@Serializable
data class Course(
    var id: String, // Assuming this is the UUID
    var name: String,
    var notes: List<String>, // No need to use ArrayList, List is fine
    var todo: List<String>
)


