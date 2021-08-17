package com.kultur.newsapp

import com.kultur.newsapp.Service.NewsService
import com.kultur.newsapp.Service.RetrofitClient

object Constant {
    const val BASE_URL ="https://newsapi.org/"
    const val API_KEY = "daf0803d4b6949d9abc7dcb6a1766970"


    val newsService:NewsService
        get()= RetrofitClient.getClient(BASE_URL).create(NewsService::class.java)

    //https://newsapi.org/v2/top-headlines?sources=bbc-news&apiKey=daf0803d4b6949d9abc7dcb6a1766970
    fun getNewsAPI(source:String):String{
        val apiUrl =StringBuilder("https://newsapi.org/v2/top-headlines?sources=")
            .append(source)
            .append("&apiKey=")
            .append(API_KEY)
            .toString()
        return apiUrl
    }
}