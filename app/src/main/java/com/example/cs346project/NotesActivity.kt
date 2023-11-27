package com.example.cs346project

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Button
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cs346project.viewModels.NotesViewModel
import com.example.cs346project.viewModels.TermViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
// WIP

class NotesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            Notes(modifier = Modifier)
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Notes(modifier: Modifier) {
    val viewModel: NotesViewModel = viewModel()
    val notesState = viewModel.notesState.collectAsState()
    LaunchedEffect(true) {
        viewModel.fetchCourseNotes(termUUID = "5ee51a2c-022a-46f5-851e-558ad9a14a05", CourseUUID =
        "f1763f11-625f-434d-9af7-85cdedd10117")
    }
    Column(
        modifier
            .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
    ){
        TopAppBar(title = { Text("Notes") })


        LazyColumn(){
            items(items = notesState.value){ note->
                ColumnI(modifier = modifier, name = note.data)
            }
        }
    }
}

@Composable
fun ColumnI(modifier:Modifier, name: String){
    Card(
        modifier
            .padding(6.dp)
            .wrapContentHeight()
            .height(100.dp)
            .aspectRatio(3f), colors = CardDefaults.cardColors(
            containerColor = Color.LightGray
        ),
        elevation = CardDefaults.cardElevation(10.dp)
    ){
        Box(
            modifier
                .padding(10.dp)
                .fillMaxSize(), contentAlignment = Alignment.Center
        ){
            Text(text = name, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AddNotes(
    text: String,
    onValueChange: (String) -> Unit,
    onAddNote: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = onValueChange,
            placeholder = { Text("Add new note...") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = androidx.compose.ui.text.input.ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onAddNote()
                }
            ),
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp)
        )
    }
}

