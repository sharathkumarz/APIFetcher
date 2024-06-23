package com.sharathkumar.apifetcher.Retrofit

import android.util.Log
import androidx.lifecycle.ViewModel


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class emailViewModel : ViewModel() {
    private val _emailList = MutableStateFlow<List<String>>(emptyList())
    val emailList : StateFlow<List<String>> = _emailList
    fun getAllComments() {

        val emailList = mutableListOf<String>()
        val BASE_URL = "https://jsonplaceholder.typicode.com/";
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyApi::class.java)

        api.getComments().enqueue(object : Callback<List<Comments>> {
            override fun onResponse(
                call: Call<List<Comments>>,
                response: Response<List<Comments>>,
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        for (comment in it) {
                            emailList.add(comment.email)
                            Log.i("MyAct", "Respone:${comment.email}")
                        }
                    }

                }
                _emailList.value=emailList
            }

            override fun onFailure(call: Call<List<Comments>>, t: Throwable) {
                Log.i("MyAct", "Respone:failed")
            }

        })

    }
}