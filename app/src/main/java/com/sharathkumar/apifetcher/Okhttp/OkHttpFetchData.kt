package com.sharathkumar.apifetcher.Okhttp

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.*
import java.io.IOException

class OkHttpViewModel : ViewModel() {

    private val _dataList = MutableStateFlow<List<String>>(emptyList())
    val dataList: StateFlow<List<String>> = _dataList
    private val client = OkHttpClient()

    fun fetchDataFromApi() {
        val BASE_URL = "https://jsonplaceholder.typicode.com/posts"
        val request = Request.Builder()
            .url(BASE_URL)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("OkHttpViewModel", "Failed to fetch data", e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { responseBody ->
                    try {
                        val data = responseBody.string()
                        if (response.isSuccessful) {
                            val gson = Gson()
                            val posts: List<Post> = gson.fromJson(data, object : TypeToken<List<Post>>() {}.type)

                            // Update _dataList with the list of post titles
                            _dataList.value = posts.map { it.title }

                            // Logging each post
                            posts.forEach { post ->
                                Log.i("OkHttpViewModel", "Post ID: ${post.id}, Title: ${post.title}")
                            }
                        } else {
                            Log.e("OkHttpViewModel", "Response unsuccessful or empty body")
                        }
                    } catch (e: IOException) {
                        Log.e("OkHttpViewModel", "IOException: ${e.message}", e)
                    }
                }
            }
        })
    }
}

