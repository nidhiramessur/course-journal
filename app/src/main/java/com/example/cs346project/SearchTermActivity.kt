package com.example.cs346project

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

class SearchTermActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SearchTerm()
        }
    }

    @Composable
    fun SearchTerm() {

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

            DropdownMenu()

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Go to Term")
            }

        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun DropdownMenu() {
        val parentOptions = listOf<String>("Animals", "Bird", "Fish")
        var expandedState by remember { mutableStateOf(false) }
        var selectedOption by remember { mutableStateOf(parentOptions[0]) }
        var mContext = LocalContext.current

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

                parentOptions.forEach{
                    eachOption -> DropdownMenuItem(onClick = { 
                    selectedOption = eachOption
                    expandedState = false
                    Toast.makeText(mContext,""+selectedOption,Toast.LENGTH_LONG).show()
                    }) {
                        Text(text = eachOption, fontSize = 28.sp)
                } 
                }

            }

        }

    }

}