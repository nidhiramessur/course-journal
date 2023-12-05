package com.example.cs346project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cs346project.viewModels.CourseManagementViewModel
import com.example.cs346project.viewModels.NavigationViewModel


class CourseManagementActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val termName = intent.getStringExtra("TERM_NAME") // Retrieve the term name
        val courseName = intent.getStringExtra("COURSE_NAME") // Retrieve the course name
        Log.d("TERM_NAME", termName?:"")
        Log.d("COURSE_NAME", courseName?:"")

         setContent {
            val selectedItem = NavigationViewModel.selectedItem.value
            BaseScreen(selectedItem, onItemSelected = { index ->
                NavigationViewModel.selectedItem.value = index
            }) {
                CourseInfo(termName ?: "", courseName ?: "")
            }
        }
    }
}


@Composable
fun CourseInfo(termName:String, courseName: String) {

    val viewModel: CourseManagementViewModel = viewModel()
    val courseInfoState = viewModel.courseInfoState.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(key1 = termName, key2 = courseName) {
        viewModel.fetchCourseInfo(termName, courseName)
    }

    if (errorMessage.isNotEmpty()) {
        Text(text = errorMessage, color = Color.Red)
    } else {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 20.dp, vertical = 50.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ){
            val context = LocalContext.current
            IconButton(
                onClick = {
                    val intent = Intent(context, TermActivity::class.java)
                    val sourceActivity = intent.getStringExtra("SOURCE_ACTIVITY")
                    val backIntent = when (sourceActivity) {
                        "CurrentTermActivity" -> Intent(context, CurrentTermActivity::class.java)
                        "TermActivity" -> Intent(context, TermActivity::class.java)
                        else -> Intent(context, CurrentTermActivity::class.java)
                    }
                    backIntent.putExtra("SELECTED_TERM", termName)
                    backIntent.putExtra("TERM_NAME", termName) // Passing the term name
                    backIntent.putExtra("COURSE_NAME", courseName) // Passing the course name
                    context.startActivity(backIntent)
                },
                modifier = Modifier.align(Alignment.Start)
            ) {
                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Close")
            }

            val courseDBData = courseInfoState.value.firstOrNull()
            Log.d("COURSE STATE", courseInfoState.value.toString())

            if (courseDBData != null) {

                Text(courseDBData.name,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center)
                Text(courseDBData.title,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center)
                if (courseDBData.requirements.isNotEmpty()) {
                    Text(courseDBData.requirements,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center)
                }

                Text("Course Rating: ${courseDBData.courserating}/5",
                    fontSize = 17.sp,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center)

                if (courseDBData.lecturedatetime.isNotEmpty()) {
                    Text(text = "All lecture times:",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        textAlign = TextAlign.Center)
                    Text(courseDBData.lecturedatetime,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center)
                }

                if (courseDBData.lecturelocation.isNotEmpty()) {
                    Text("Lecture location: ${courseDBData.lecturelocation}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center)
                }

                if (courseDBData.professorname.isNotEmpty()) {
                    Text("Instructor: ${courseDBData.professorname}",
                        fontSize = 25.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        textAlign = TextAlign.Center)
                    Text("Instructor Rating: ${courseDBData.professorrating}/5",
                        fontSize = 17.sp,
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center)
                }
            }

            Button(
                onClick = {
                    val intent = Intent(context, ToDoListActivity::class.java)
                    intent.putExtra("SELECTED_TERM", termName)
                    intent.putExtra("TERM_NAME", termName) // Passing the term name
                    intent.putExtra("COURSE_NAME", courseName) // Passing the course name
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp)
            ) {
                Text(" To-do list")
            }
            Button(
                onClick = {
                    val intent = Intent(context, NotesActivity::class.java)
                    intent.putExtra("SELECTED_TERM", termName)
                    intent.putExtra("TERM_NAME", termName) // Passing the term name
                    intent.putExtra("COURSE_NAME", courseName) // Passing the course name
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Notes")
            }

        }
    }

}