package com.example.cs346project

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel

class ToDoListActivity : AppCompatActivity() {
    private val viewModel: TodoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoListScreen(viewModel)
        }
    }
}

class TodoViewModel : ViewModel() {
    var todos by mutableStateOf(listOf<String>())
        private set

    fun addTodo(todo: String) {
        todos = todos + todo
    }
}

@Composable
fun TodoListScreen(viewModel: TodoViewModel) {
    var newTodoText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Title
        Text(
            text = "To-Do List",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Input field to add a new to-do
        BasicTextField(
            value = newTodoText,
            onValueChange = { newTodoText = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(Color.LightGray),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Add Button
        Button(
            onClick = {
                viewModel.addTodo(newTodoText)
                newTodoText = ""
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Add")
        }

        // To-do list
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            this.items(viewModel.todos) { todo ->
                TodoItem(todo = todo)
            }
        }
    }
}

@Composable
fun TodoItem(todo: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = todo, fontSize = 16.sp, modifier = Modifier.weight(1f))
        // Need to add a button here for marking tasks as completed or deleting them.
    }
}
