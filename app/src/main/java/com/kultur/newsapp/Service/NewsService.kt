package com.kultur.newsapp.Service

import com.kultur.newsapp.Model.News
import com.kultur.newsapp.Model.WebSite
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface NewsService {
    //https://newsapi.org/v2/top-headlines/sources?apiKey=daf0803d4b6949d9abc7dcb6a1766970


    @get:GET("v2/top-headlines/sources?apiKey=daf0803d4b6949d9abc7dcb6a1766970")
    val sources : Call<WebSite>

    @GET
    fun getNewsFromSource(@Url url :String): Call<News>
}