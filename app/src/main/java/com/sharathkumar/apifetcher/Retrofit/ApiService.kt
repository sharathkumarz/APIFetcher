package com.sharathkumar.apifetcher.Retrofit

import retrofit2.http.GET
import retrofit2.Call

interface MyApi {
    @GET("comments")
   fun getComments():Call<List<Comments>>
}