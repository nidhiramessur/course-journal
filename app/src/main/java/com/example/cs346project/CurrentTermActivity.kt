package com.example.cs346project

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
                CurrentTermPage(modifier = Modifier)
            }
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

    // Need to change the UUID above

    val context = LocalContext.current
    IconButton(
        onClick = {
            context.startActivity(Intent(context, HomepageActivity::class.java))
        },
        modifier = Modifier.height(60.dp)
    ) {
        androidx.compose.material.Icon(
            imageVector = Icons.Default.KeyboardArrowLeft,
            contentDescription = "Close"
        )
    }

    // Need to change the hardcoded "Fall 2023"
    Text("Fall 2023", color = Color.Gray, fontSize = 22.sp,fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 120.dp, vertical = 20.dp),
        textAlign = TextAlign.Center)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(horizontal = 40.dp, vertical = 70.dp)
    ){

        LazyColumn(Modifier.weight(1f)){
            items(items = coursesState.value){ item->
                ColumnItem(modifier = modifier, name = item, context)
            }
        }

        Button(
            onClick = {
                context.startActivity(Intent(context, CourseInfoDisplayActivity::class.java))
            },
            modifier = Modifier
                .padding(top = 30.dp)) {
            Text("Add Course")
        }

    }
}

@Composable
fun ColumnItem(modifier: Modifier, name: String, context: Context){
    Card(
        modifier
            .padding(6.dp)
            .wrapContentHeight()
            .height(100.dp)
            .aspectRatio(3f),
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray
        ),
        elevation = CardDefaults.cardElevation(10.dp)
    ){
        Button(
            onClick = {
                context.startActivity(Intent(context, CourseManagementActivity::class.java))
            },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
        ){
            Text(text = name, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
    }
}

