package com.example.cs346project

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cs346project.viewModels.TermViewModel
import com.example.cs346project.viewModels.NavigationViewModel

class CurrentTermActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val selectedItem = NavigationViewModel.selectedItem.value
            BaseScreen(selectedItem, onItemSelected = { index ->
                NavigationViewModel.selectedItem.value = index
            }) {
                CurrentTermPage()
            }
        }
    }
}

@Composable
fun CurrentTermPage() {
    val termViewModel: TermViewModel = viewModel()
    val coursesState = termViewModel.coursesState.collectAsState()
    val context = LocalContext.current
    var currentTermTitle by remember { mutableStateOf("Loading...") }
    var refresh by remember { mutableStateOf(0) }
    refresh++

    LaunchedEffect(refresh) {
        termViewModel.fetchCurrentTermUUID { termUUID ->
            if (termUUID != "") {
                termViewModel.fetchCoursesForTerm(termUUID)
                termViewModel.fetchNamefromCurrentTermUUID(termUUID) { currentTermName ->
                    currentTermTitle = currentTermName
                }
            } else {
                currentTermTitle = "No current term"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = {
                context.startActivity(Intent(context, HomepageActivity::class.java))
            },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Back")
        }

        Text(
            text = currentTermTitle,
            color = Color.Gray,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 120.dp, vertical = 20.dp),
            textAlign = TextAlign.Center
        )

        LazyColumn(Modifier.weight(1f)) {
            items(items = coursesState.value) { course ->
                ColumnItem(courseName = course, context = context, termName = currentTermTitle)
            }
        }

        if (currentTermTitle != "No current term") {
            Button(
                onClick = {
                    refresh++
                    context.startActivity(Intent(context, CourseInfoDisplayActivity::class.java))
                },
                modifier = Modifier
                    .padding(top = 30.dp)
            ) {
                Text("Add Course")
            }
        }
    }
}

@Composable
fun ColumnItem(courseName: String, context: Context, termName: String) {
    Card(
        modifier = Modifier
            .padding(6.dp)
            .wrapContentHeight()
            .height(100.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray
        ),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Button(
            onClick = {
                val intent = Intent(context, CourseManagementActivity::class.java)
                intent.putExtra("TERM_NAME", termName) // Passing the term name
                intent.putExtra("COURSE_NAME", courseName) // Passing the course name
                intent.putExtra("SOURCE_ACTIVITY", "CurrentTermActivity")
                context.startActivity(intent)
            },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
        ) {
            Text(text = courseName, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
    }
}
