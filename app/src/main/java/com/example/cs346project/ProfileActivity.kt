package com.example.cs346project

import android.os.Bundle
import androidx.activity.compose.setContent
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.material.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cs346project.viewModels.NavigationViewModel



class ProfileActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Use the property delegate to observe changes in selectedItem
            val selectedItem = NavigationViewModel.selectedItem.value
            BaseScreen(selectedItem, onItemSelected = { index ->
                NavigationViewModel.selectedItem.value = index
            }) {
                ProfileContent()
            }
        }
    }
}


@Composable
fun ProfileContent() {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Username: ${currentUser?.displayName ?: "Not set"}", style = MaterialTheme.typography.h6)
        Text("Email: ${currentUser?.email ?: "Not set"}", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(20.dp))
        LogoutButton()
    }
}

@Composable
fun LogoutButton() {
    val context = LocalContext.current
    Button(
        onClick = {
            FirebaseAuth.getInstance().signOut()
            context.startActivity(Intent(context, MainActivity::class.java))
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp)
    ) {
        Text("Logout")
    }
}
