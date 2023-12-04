package com.example.cs346project

import com.example.cs346project.viewModels.NavigationViewModel
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

open class BaseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Use the property delegate to observe changes in selectedItem
            val selectedItem = NavigationViewModel.selectedItem.value
            BaseScreen(selectedItem, onItemSelected = { index ->
                NavigationViewModel.selectedItem.value = index
            })
        }
    }
}



@Composable
fun BottomNavigationBar(selectedItem: Int, onItemSelected: (Int) -> Unit) {
    val context = LocalContext.current

    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        elevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            selected = selectedItem == 0,
            onClick = { 
                onItemSelected(0)
                // Navigate to Profile Screen
                context.startActivity(Intent(context, HomepageActivity::class.java))
            }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Edit, contentDescription = "Add Term") },
            selected = selectedItem == 1,
            onClick = { 
                onItemSelected(1)
                // Navigate to Add/Change Screen
                context.startActivity(Intent(context, SearchTermActivity::class.java))
            }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            selected = selectedItem == 2,
            onClick = { 
                onItemSelected(2)
                // Navigate to Profile Screen
                context.startActivity(Intent(context, ProfileActivity::class.java))
            }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.CalendarToday, contentDescription = "Schedule") },
            selected = selectedItem == 3,
            onClick = { 
                onItemSelected(3)
                // Navigate to Schedule Screen
                context.startActivity(Intent(context, CurrentTermActivity::class.java))
            }
        )
    }
}



@Composable
fun BaseScreen(selectedItem: Int, onItemSelected: (Int) -> Unit, content: @Composable () -> Unit = {}) {
    Scaffold(
        bottomBar = { BottomNavigationBar(selectedItem, onItemSelected) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            content()
        }
    }
}