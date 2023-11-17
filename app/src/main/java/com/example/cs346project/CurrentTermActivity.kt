package com.example.cs346project

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cs346project.viewModels.TermViewModel
import com.google.firebase.auth.FirebaseAuth
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


class CurrentTermActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            CurrentTermPage(modifier = Modifier)
        }

    }

}

@Composable
fun CurrentTermPage(modifier: Modifier) {
    val viewModel: TermViewModel = viewModel()
    val coursesState = viewModel.coursesState.collectAsState()
    LaunchedEffect(true) {
        viewModel.fetchCoursesForTerm("5ee51a2c-022a-46f5-851e-558ad9a14a05")
    }
    Text("Fall 2023", color = Color.Gray, fontSize = 22.sp,fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 120.dp, vertical = 20.dp),
        textAlign = TextAlign.Center)
    Column(modifier.fillMaxSize().padding(vertical = 40.dp, horizontal = 120.dp)
    ){
        LazyColumn(){
            items(items = coursesState.value){ item->
                ColumnItem(modifier = modifier, name = item)
            }
        }


    }
}

@Composable
fun ColumnItem(modifier:Modifier, name: String){
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

