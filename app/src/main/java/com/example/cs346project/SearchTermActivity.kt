package com.example.cs346project

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cs346project.viewModels.CourseManagementViewModel
import com.example.cs346project.viewModels.TermViewModel
import com.example.cs346project.viewModels.NavigationViewModel

import android.util.Log


class SearchTermActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

         setContent {
            val selectedItem = NavigationViewModel.selectedItem.value
            BaseScreen(selectedItem, onItemSelected = { index ->
                NavigationViewModel.selectedItem.value = index
            }) {
                SearchTerm()
            }
        }
    }
}


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SearchTerm() {
    val termViewModel: TermViewModel = viewModel()
    val courseViewModel: CourseManagementViewModel = viewModel()
    val termsState = termViewModel.termsState.collectAsState()
    var selectedTerm by remember { mutableStateOf("") }
    var isFetchingUUID by remember { mutableStateOf(false) }
    var newTerm by remember { mutableStateOf("") }
    var refresh by remember { mutableStateOf(0) }
    refresh++

    LaunchedEffect(true) {
        termViewModel.fetchTerms()
    }

    val termsList = termsState.value.toMutableList()


    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun dropdownMenu(termsList: MutableList<String>, onTermSelected: (String) -> Unit): String {
        var expandedState by remember { mutableStateOf(false) }
        var selectedOption by remember { mutableStateOf("Select a Term") }

        ExposedDropdownMenuBox(
            expanded = expandedState,
            onExpandedChange = {
                refresh++
                expandedState = !expandedState
            }) {
            
            TextField(
                value = selectedOption,
                onValueChange = {refresh++},
                trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedState)},
                readOnly = true,
                textStyle = TextStyle.Default.copy(fontSize = 28.sp)
            )

            ExposedDropdownMenu(
                expanded = expandedState,
                onDismissRequest = {
                    refresh++
                    expandedState = false }) {

                termsList.forEach{
                    eachOption -> DropdownMenuItem(onClick = {
                    refresh++
                    selectedOption = eachOption
                    expandedState = false
                    onTermSelected(eachOption)
            }) {
                        Text(text = eachOption, fontSize = 28.sp)
                    }
                }

            }

        }

        return selectedOption

    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current
        IconButton(
            onClick = {
                context.startActivity(Intent(context, HomepageActivity::class.java))
            },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Close")
        }
            Text(
                "Search for a term",
                color = Color.Gray,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 120.dp, vertical = 20.dp),
                textAlign = TextAlign.Center
            )
            

            selectedTerm = dropdownMenu(termsList) { termName ->
                refresh++
                selectedTerm = termName
        }
            

            Button(
            onClick = {
                if (selectedTerm != "" && selectedTerm != "Select a Term") {
                    refresh++
                    val intent = Intent(context, TermActivity::class.java)
                    intent.putExtra("SELECTED_TERM", selectedTerm) // Passing the course name
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, "No term selected", Toast.LENGTH_LONG).show()
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Go to Term")
        }

            val coroutineScope = rememberCoroutineScope()

        // "Save as Default Term" Button
            Button(
            onClick = {
                if (selectedTerm != "" && selectedTerm != "Select a Term") {
                    isFetchingUUID = true
                    coroutineScope.launch {
                        val termUUID = courseViewModel.fetchTermUUIDByName(selectedTerm)
                        Log.d("SearchTermActivity", "Fetched UUID: $termUUID")

                        if (termUUID != null && termUUID.isNotEmpty()) {
                            termViewModel.updateUserCurrentTerm(termUUID)
                            refresh++
                            Toast.makeText(context, "Current term set to $selectedTerm", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, "Unable to set current term. Please try again.", Toast.LENGTH_LONG).show()
                        }
                        isFetchingUUID = false
                    }
                } else {
                    Toast.makeText(context, "Please select a term first", Toast.LENGTH_LONG).show()
                }
            },
            modifier = Modifier.padding(8.dp),
            enabled = !isFetchingUUID
        ) {
            Text("Save as current Term")
        }


            Text(
                "Add a new term",
                color = Color.Gray,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 120.dp, vertical = 20.dp),
                textAlign = TextAlign.Center
            )

            OutlinedTextField(
                value = newTerm,
                onValueChange = { newTerm = it },
                label = { Text("Term to add") },
                placeholder = { Text("e.g Fall 2023") },
                modifier = Modifier
                    .width(320.dp)
                    .padding(bottom = 16.dp)
            )

            Button(
                onClick = {
                    if (newTerm != "") {
                        refresh++
                        termViewModel.addTerm(newTerm)
                        newTerm = "" // Reset newTerm after adding
                    } else {
                        Toast.makeText(context, "No term to add", Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Add term")
            }
        }
    }

        
    
