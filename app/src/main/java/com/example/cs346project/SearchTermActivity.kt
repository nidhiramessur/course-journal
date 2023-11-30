package com.example.cs346project

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cs346project.viewModels.TermViewModel

class SearchTermActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SearchTerm()
        }
    }

    @SuppressLint("StateFlowValueCalledInComposition")
    @Composable
    fun SearchTerm() {

        val viewModel: TermViewModel = viewModel()
        val termsState = viewModel.termsState.collectAsState()
        var selectedTerm by remember { mutableStateOf("") }

        // uncomment once firestore is up
        LaunchedEffect(true) {
            viewModel.fetchTerms()
        }

        val termsList = termsState.value.toMutableList()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Term search fields and button
            val context = LocalContext.current
            IconButton(
                onClick = {
                    context.startActivity(Intent(context, HomepageActivity::class.java))
                },
                modifier = Modifier.align(Alignment.Start)
            ) {
                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Close")
            }

            selectedTerm = dropdownMenu(termsList)

            Button(
                onClick = {
                    if (selectedTerm != "" && selectedTerm != "Select a Term") {
                        context.startActivity(Intent(context, CurrentTermActivity::class.java))
                    } else {
                        Toast.makeText(context,"No term selected",Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Go to Term")
            }

        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun dropdownMenu(termsList: MutableList<String>): String {
        var expandedState by remember { mutableStateOf(false) }
        var selectedOption by remember { mutableStateOf("Select a Term") }

        ExposedDropdownMenuBox(
            expanded = expandedState,
            onExpandedChange = {expandedState = !expandedState}) {
            
            TextField(
                value = selectedOption,
                onValueChange = {},
                trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedState)},
                readOnly = true,
                textStyle = TextStyle.Default.copy(fontSize = 28.sp)
            )

            ExposedDropdownMenu(
                expanded = expandedState,
                onDismissRequest = { expandedState = false }) {

                // replace next line once firestore is up
                termsList.forEach{
                    eachOption -> DropdownMenuItem(onClick = { 
                    selectedOption = eachOption
                    expandedState = false
                    }) {
                        Text(text = eachOption, fontSize = 28.sp)
                    }
                }

            }

        }

        return selectedOption

    }

}