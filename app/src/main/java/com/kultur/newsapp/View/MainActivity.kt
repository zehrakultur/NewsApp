package com.kultur.newsapp.View

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Switch
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.kultur.newsapp.Adapter.ViewHolder.ListSourceAdapter
import com.kultur.newsapp.Constant
import com.kultur.newsapp.Model.WebSite
import com.kultur.newsapp.R
import com.kultur.newsapp.SaveData
import com.kultur.newsapp.Service.NewsService
import com.shashank.sony.fancytoastlib.FancyToast
import dmax.dialog.SpotsDialog
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var layoutManager: LinearLayoutManager
    lateinit var mService: NewsService
    lateinit var adapter: ListSourceAdapter
    lateinit var dialog: AlertDialog
    private var switch: Switch?=null
    private lateinit var saveData: SaveData

    override fun onCreate(savedInstanceState: Bundle?) {
        saveData= SaveData(this)
        if(saveData.loadDarkModeState() == true){
            setTheme(R.style.darkTheme)
        }
        else{
            setTheme(R.style.AppTheme)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        switch=findViewById<View>(R.id.dayNight) as Switch?
        if(saveData.loadDarkModeState() == true){
            switch!!.isChecked=true
        }
        switch!!.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                saveData.setDarkModeState(true)
                restartApplication()
            } else{
                saveData.setDarkModeState(false)
                restartApplication()
            }
        }

        Paper.init(this)

        mService= Constant.newsService

        swipe_to_refresh.setOnRefreshListener {
            loadWebSiteSource(true)
        }


        recycler_view_source_news.setHasFixedSize(true)
        layoutManager= LinearLayoutManager(this)
        recycler_view_source_news.layoutManager =layoutManager

        dialog= SpotsDialog(this)
        loadWebSiteSource(false)
    }
    private fun restartApplication() {
        val i= Intent(applicationContext, MainActivity::class.java)
        startActivity(i)
        finish()
    }
    private fun loadWebSiteSource(isRefresh: Boolean){

        if(!isRefresh){
            val cache=Paper.book().read<String>("cache")
            if(cache != null && !cache.isBlank() && cache != "null"){
                var webSite= Gson().fromJson<WebSite>(cache, WebSite::class.java)
                adapter= ListSourceAdapter(baseContext, webSite)
                adapter.notifyDataSetChanged()
                recycler_view_source_news.adapter=adapter

            }
            else{
                dialog.show()

                mService.sources.enqueue(object : Callback<WebSite> {
                    override fun onResponse(call: Call<WebSite>, response: Response<WebSite>) {
                        adapter= ListSourceAdapter(baseContext, response.body()!!)
                        adapter.notifyDataSetChanged()
                        recycler_view_source_news.adapter=adapter

                        Paper.book().write("cache", Gson().toJson(response.body()))

                        dialog.dismiss()
                    }

                    override fun onFailure(call: Call<WebSite>, t: Throwable) {
                        FancyToast.makeText(this@MainActivity,"Failed",
                            FancyToast.LENGTH_LONG, FancyToast.ERROR,false).show()
                    }

                })
            }
        }

        else{
            swipe_to_refresh.isRefreshing=true

            mService.sources.enqueue(object : Callback<WebSite> {
                override fun onResponse(call: Call<WebSite>, response: Response<WebSite>) {
                    adapter=ListSourceAdapter(baseContext,response.body()!!)
                    adapter.notifyDataSetChanged()
                    recycler_view_source_news.adapter=adapter

                    Paper.book().write("cache", Gson().toJson(response.body()))

                    swipe_to_refresh.isRefreshing=false
                }

                override fun onFailure(call: Call<WebSite>, t: Throwable) {
                    FancyToast.makeText(this@MainActivity,"Failed",
                        FancyToast.LENGTH_LONG, FancyToast.ERROR,false).show()
                }

            })
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
        System.exit(0)
    }
}