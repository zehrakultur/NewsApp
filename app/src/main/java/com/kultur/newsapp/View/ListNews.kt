package com.kultur.newsapp.View

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.kultur.newsapp.Adapter.ViewHolder.ListNewsAdapter
import com.kultur.newsapp.Constant
import com.kultur.newsapp.Model.News
import com.kultur.newsapp.R
import com.kultur.newsapp.SaveData
import com.kultur.newsapp.Service.NewsService
import com.shashank.sony.fancytoastlib.FancyToast
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_list_news.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.swipe_to_refresh
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListNews : AppCompatActivity() {
    var source=""
    var webHotUrl:String?=""
    lateinit var mService: NewsService
    lateinit var adapter: ListNewsAdapter
    lateinit var dialog: AlertDialog
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
        setContentView(R.layout.activity_list_news)
        mService= Constant.newsService

        dialog= SpotsDialog(this)
        swipe_to_refresh.setOnRefreshListener{
            loadNews(source,true)
        }
        diagonalLayout.setOnClickListener {

            val detail= Intent(baseContext, NewsDetail::class.java)
            detail.putExtra("webURL",webHotUrl)
            startActivity(detail)
        }

        list_news.setHasFixedSize(true)
        list_news.layoutManager= LinearLayoutManager(this)


        if(intent !=null){
            source= intent.getStringExtra("source")!!
            if(!source.isEmpty()){
                loadNews(source,false)
            }
        }
    }
    private fun loadNews(source: String, isRefreshed: Boolean) {

        if(isRefreshed){
            dialog.show()
            mService.getNewsFromSource(Constant.getNewsAPI(source)).enqueue(object :
                Callback<News> {
                override fun onResponse(call: Call<News>, response: Response<News>) {
                    dialog.dismiss()

                    Picasso.get().load(response.body()!!.articles!![0].urlToImage).into(top_image)
                    top_title.text= response.body()!!.articles!![0].title
                    top_author.text= response.body()!!.articles!![0].author

                    webHotUrl= response.body()!!.articles!![0].url

                    val removeFirstItem= response.body()!!.articles
                    removeFirstItem!!.removeAt(0)
                    adapter= ListNewsAdapter(removeFirstItem,baseContext)
                    adapter.notifyDataSetChanged()
                    list_news.adapter=adapter
                }

                override fun onFailure(call: Call<News>, t: Throwable) {
                    FancyToast.makeText(baseContext,"Failed",
                        FancyToast.LENGTH_LONG, FancyToast.ERROR,false).show()
                }

            })
        }

        else{
            swipe_to_refresh.isRefreshing=true
            mService.getNewsFromSource(Constant.getNewsAPI(source)).enqueue(object :
                Callback<News> {
                override fun onResponse(call: Call<News>, response: Response<News>) {
                    swipe_to_refresh.isRefreshing=false

                    Picasso.get().load(response.body()!!.articles!![0].urlToImage).into(top_image)
                    top_title.text= response.body()!!.articles!![0].title
                    top_author.text= response.body()!!.articles!![0].author

                    webHotUrl= response.body()!!.articles!![0].url

                    val removeFirstItem= response.body()!!.articles
                    removeFirstItem!!.removeAt(0)
                    adapter= ListNewsAdapter(removeFirstItem,baseContext)
                    adapter.notifyDataSetChanged()
                    list_news.adapter=adapter
                }
                override fun onFailure(call: Call<News>, t: Throwable) {
                    FancyToast.makeText(baseContext,"Failed",
                        FancyToast.LENGTH_LONG, FancyToast.ERROR,false).show()
                }
            })
        }
    }
}