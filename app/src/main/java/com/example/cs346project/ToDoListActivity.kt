package com.example.cs346project

import android.content.Intent
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
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cs346project.models.TodoItem
import com.example.cs346project.viewModels.CourseManagementViewModel
import com.example.cs346project.viewModels.TodoViewModel
import kotlinx.coroutines.launch

class ToDoListActivity : AppCompatActivity() {
    private val viewModel: TodoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val termName = intent.getStringExtra("TERM_NAME") // Retrieve the term name
        val courseName = intent.getStringExtra("COURSE_NAME") // Retrieve the course name
        setContent {
            TodoListScreen(viewModel, termName ?: "", courseName ?: "")
        }
    }
}

@Composable
fun TodoListScreen(viewModel: TodoViewModel, termName:String, courseName: String) {
    var newTodoText by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    val courseViewModel: CourseManagementViewModel = viewModel()

    // Fetch todoList when the screen is first loaded
    LaunchedEffect(Unit) {
        val termUUID = courseViewModel.fetchTermUUIDByName(termName)
        val courseUUID = termUUID?.let { courseViewModel.fetchCourseUUIDByName(it, courseName) }
        if (termUUID != null && courseUUID != null) {
            viewModel.fetchTodoList(termUUID, courseUUID)
        }
    }

    val todoList = viewModel.todoState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val context = LocalContext.current
        IconButton(
            onClick = {
                val intent = Intent(context, CourseManagementActivity::class.java)
                intent.putExtra("SELECTED_TERM", termName)
                intent.putExtra("TERM_NAME", termName) // Passing the term name
                intent.putExtra("COURSE_NAME", courseName) // Passing the course name
                context.startActivity(intent)
            },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Close")
        }
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
                if(newTodoText.isNotBlank()) {
                    coroutineScope.launch {
                        val termUUID = courseViewModel.fetchTermUUIDByName(termName)
                        val courseUUID = termUUID?.let { courseViewModel.fetchCourseUUIDByName(it, courseName) }
                        if (termUUID != null && courseUUID != null) {
                            viewModel.addTodoToFirestore(termUUID, courseUUID, newTodoText, false)
                        }
                        newTodoText = "" // Reset the input field
                    }
                }
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
            items(todoList.value) { todoItem ->
                    TodoItem(todo = todoItem, onCompletionToggle = { todo ->
                        coroutineScope.launch {
                            val termUUID = courseViewModel.fetchTermUUIDByName(termName)
                            val courseUUID = termUUID?.let { courseViewModel.fetchCourseUUIDByName(it, courseName) }
                            if (termUUID != null && courseUUID != null) {
                                viewModel.updateTodoCompletion(termUUID, courseUUID, todo, !todo.completed)
                            }
                        }
                    })
            }
        }
    }
}

@Composable
fun TodoItem(todo: TodoItem, onCompletionToggle: (TodoItem) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = todo.title, fontSize = 16.sp, modifier = Modifier.weight(1f))
        Button(
            onClick = { onCompletionToggle(todo) },
            // Modify the button appearance based on the completion status
            colors = if (todo.completed) ButtonDefaults.buttonColors(backgroundColor = Color.Green)
            else ButtonDefaults.buttonColors(backgroundColor = Color.Red)
        ) {
            Text(if (todo.completed) "Completed" else "Complete")
        }
    }
}
