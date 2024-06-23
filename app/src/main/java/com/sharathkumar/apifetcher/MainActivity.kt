package com.sharathkumar.apifetcher
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.sharathkumar.apifetcher.Okhttp.OkHttpViewModel
import com.sharathkumar.apifetcher.Retrofit.emailViewModel
import com.sharathkumar.apifetcher.Volley.VolleyViewModel
import com.sharathkumar.apifetcher.ui.theme.APIFetcherTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            APIFetcherTheme {
                ApiScreen(namesViewModel = VolleyViewModel(), myokmodel = OkHttpViewModel(), EmailViewModel = emailViewModel(), context = this)
            }
        }
    }
}


@Composable
fun ApiScreen(namesViewModel:VolleyViewModel,myokmodel: OkHttpViewModel,EmailViewModel: emailViewModel,context: Context) {
    val (selectApi, selectedApi) = remember { mutableStateOf<String?>(null) }

    Column( modifier = Modifier.fillMaxSize().padding(bottom = 16.dp,top = 25.dp)) {

        Box(modifier = Modifier.weight(1f)) {
            when (selectApi) {
                "VOLLEY" -> NamesListScreen(namesViewModel = namesViewModel, context = context)
                "RETROFIT" -> GetList(EmailViewModel = EmailViewModel)
                "OK-HTTP" -> MyList(myokmodel = myokmodel)
                else -> {}
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
             verticalAlignment = Alignment.Bottom

        ){ Button(onClick = { selectedApi("VOLLEY") }) {
                Text(" VOLLEY ")
            }

            Button(onClick = { selectedApi("RETROFIT") }) {
                Text("RETROFIT")
            }

            Button(onClick = { selectedApi("OK-HTTP") }) {
                Text(" Ok-HTTP ")
            }

        }
    }
}

@Composable
fun NamesListScreen(namesViewModel: VolleyViewModel = viewModel(), context: Context) {
    val namesList by namesViewModel.namesList.collectAsState(initial = emptyList())
    LazyColumn(modifier = Modifier.fillMaxSize()
        .background(color = Color.Cyan),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(namesList.size){
            item ->
               // Text(text=namesList[item])
            AsyncImage(model = namesList[item], contentDescription ="null")
        }
    }
    SideEffect {
        namesViewModel.VolleyFetchData(context = context )
    }
}


@Composable
fun GetList(EmailViewModel: emailViewModel = viewModel()){
    val emailList by EmailViewModel.emailList.collectAsState(initial = emptyList())
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(emailList.size){
                index ->
            Text(text="email id: ${emailList[index]}", fontSize = 20.sp )
        }
    }
    SideEffect {
        EmailViewModel.getAllComments()
    }
}


@Composable
fun MyList(myokmodel: OkHttpViewModel = viewModel()){
    val dataList by myokmodel.dataList.collectAsState(initial = emptyList())
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(dataList.size){
                index ->
            Text(text="post title: ${dataList[index]}", fontSize = 20.sp )
        }
    }
    SideEffect {
        myokmodel.fetchDataFromApi()
    }
}

