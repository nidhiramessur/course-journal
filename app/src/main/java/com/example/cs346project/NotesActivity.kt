package com.example.cs346project

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cs346project.models.Note
import com.example.cs346project.viewModels.NotesViewModel
import com.example.cs346project.viewModels.NavigationViewModel
import com.example.cs346project.viewModels.TermViewModel
import kotlinx.coroutines.launch


class NotesActivity : BaseActivity() {
    private val viewModel: NotesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val termName = intent.getStringExtra("TERM_NAME") // Retrieve the term name
        val courseName = intent.getStringExtra("COURSE_NAME") // Retrieve the course name

        setContent {
            val selectedItem = NavigationViewModel.selectedItem.value
            BaseScreen(selectedItem, onItemSelected = { index ->
                NavigationViewModel.selectedItem.value = index
            }) {
                Notes(modifier = Modifier, viewModel, termName ?: "", courseName ?: "")
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Notes(modifier: Modifier, viewModel: NotesViewModel, termName:String, courseName: String) {
    val notesState = viewModel.notesState.collectAsState()
    val termViewModel: TermViewModel = viewModel()

    var showDialog by remember {
        mutableStateOf((false))
    }
    var noteData by remember {
        mutableStateOf("")
    }
    var refresh by remember {
        mutableStateOf(0)
    }
    var noteSelected by remember {
        mutableStateOf<Note?>(null)
    }
    val coroutineScope = rememberCoroutineScope()

    refresh++


    LaunchedEffect(refresh) {
        termViewModel.fetchTermUUIDFromName(termName) { termUUID ->
            termViewModel.fetchCoursesUUIDFromName(termUUID, courseName) { courseUUID ->
                viewModel.fetchCourseNotes(termUUID, courseUUID)
            }
        }
    }
    Column(
        modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
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
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Close"
            )
        }

        TopAppBar(title = { Text("Notes") },
            actions = {
                TextButton(onClick = { showDialog = true}) {
                    Text("Add note")
                }
            }
        )
        LazyColumn(){
            items(items = notesState.value){ note->
                ColumnI(modifier = modifier, name = note.data, onClick ={ noteSelected = note})
            }
        }
    }
    noteSelected?.let{ note ->
        NoteDetails(
            note = note,
            onDismiss = { noteSelected = null},
            onUpdate = {newnote ->
                coroutineScope.launch {
                    termViewModel.fetchTermUUIDFromName(termName) { termUUID ->
                        termViewModel.fetchCoursesUUIDFromName(termUUID, courseName) { courseUUID ->
                            viewModel.updateNote(termUUID, courseUUID,newnote)
                            noteSelected = null
                            refresh++
                        }
                    }
                }
            },
            onDelete = {delNote ->
                coroutineScope.launch {
                    termViewModel.fetchTermUUIDFromName(termName) { termUUID ->
                        termViewModel.fetchCoursesUUIDFromName(termUUID, courseName) { courseUUID ->
                            viewModel.deleteNote(termUUID, courseUUID,delNote)
                            noteSelected = null
                            refresh++
                        }
                    }
                }
            }
        )
    }
    if (showDialog){
        AddNewNote(
            onDismiss = {showDialog = false
                noteData = ""},
            onSave = {note ->
                coroutineScope.launch {
                    termViewModel.fetchTermUUIDFromName(termName) { termUUID ->
                        termViewModel.fetchCoursesUUIDFromName(termUUID, courseName) { courseUUID ->
                            viewModel.addNote(termUUID, courseUUID,note)
                            showDialog = false
                            noteData = ""
                            refresh++
                        }
                    }
                }
            }
        )
    }
}


@Composable
fun AddNewNote(onDismiss: () -> Unit, onSave: (String) -> Unit){
    var noteData by remember {
        mutableStateOf("")
    }
    Dialog(onDismissRequest = { onDismiss()}) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.37f)
            .background(color = Color.Gray)) {

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Add New Note",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)

                )
                TextField(
                    value = noteData,
                    onValueChange = { noteData = it },
                    label = { Text("Enter note here...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = { onDismiss() }) {
                        Text("Cancel")
                    }
                    TextButton(onClick = {
                        onSave(noteData)
                    }) {
                        Text("Save")
                    }
                }

            }
        }
    }
}



@Composable
fun ColumnI(modifier:Modifier, name: String, onClick: () ->Unit){
    Card(
        modifier
            .padding(6.dp)
            .wrapContentHeight()
            .height(100.dp)
            .clickable { onClick() }
            .aspectRatio(3f), colors = CardDefaults.cardColors(
            containerColor = Color.LightGray
        ),
        elevation = CardDefaults.cardElevation(10.dp)
    ){
        Box(
            modifier
                .padding(4.dp)
                .fillMaxSize(), contentAlignment = Alignment.Center
        ){
            Text(text = name, fontSize = 22.sp, fontWeight = FontWeight.Bold, maxLines = 3,
                overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
fun NoteDetails(note: Note, onDismiss: () -> Unit, onUpdate: (Note) -> Unit,
                onDelete: (Note) -> Unit) {
    var newNoteData by remember {
        mutableStateOf(note.data)
    }
    Dialog(onDismissRequest = { onDismiss() }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.37f)
                .background(color = Color.Gray)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "View Full Note",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)

                )
                TextField(modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                    value = newNoteData,
                    onValueChange = {newNoteData = it },
                    label = { Text("Edit note here", fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    ) }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    TextButton(onClick = {
                        val newNote = note.copy(data = newNoteData)
                        onUpdate(newNote)
                    }) {
                        Text("Update", fontWeight = FontWeight.Bold)
                    }

                    TextButton(onClick = { onDismiss() }) {
                        Text("Cancel")
                    }

                    TextButton(onClick = {
                        onDelete(note)
                    }) {
                        Text("Delete", color = Color.Red, fontWeight = FontWeight.Bold)
                    }
                }

            }
        }
    }
}