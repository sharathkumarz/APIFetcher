package com.sharathkumar.apifetcher.Volley

import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONArray
import org.json.JSONObject
import android.content.Context

class VolleyViewModel : ViewModel(){
    val _namesList= MutableStateFlow<List<String>>(emptyList())
    val namesList: StateFlow<List<String>> = _namesList

    fun VolleyFetchData(context: Context){
     val BASE_URL ="https://pixabay.com/api/?key=********&q=yellow+flowers&image_type=photo&pretty=true"
     val requestQueue = Volley.newRequestQueue(context)
        val namesList = mutableListOf<String>()
        val jsonArrayRequest = JsonObjectRequest(
            Request.Method.GET,BASE_URL,null,
            { response->
                try{
                    val hits: JSONArray = response.getJSONArray("hits")
                    for (i in 0..hits.length()-1) {
                        val item: JSONObject = hits.getJSONObject(i)
                        val name: String = item.getString("user")
                        val picture: String = item.getString("webformatURL")
                        namesList.add(picture)
                    }
                    _namesList.value = namesList
                }
                catch (e:Exception){
                    e.printStackTrace()
                }
            },
            { error ->
                error.message ?: "Unknown error occurred"
            }
        )
        requestQueue.add(jsonArrayRequest)
    }
}
