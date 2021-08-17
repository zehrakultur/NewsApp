package com.kultur.newsapp.Adapter.ViewHolder

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.kultur.newsapp.ISO8601Parser
import com.kultur.newsapp.ItemClickListener
import com.kultur.newsapp.Model.Article
import com.kultur.newsapp.R
import com.kultur.newsapp.View.NewsDetail
import com.shashank.sony.fancytoastlib.FancyToast
import com.squareup.picasso.Picasso
import java.text.ParseException
import java.util.*

class ListNewsAdapter(val articleList:MutableList<Article>, private val context: Context):
    RecyclerView.Adapter<ListNewsViewHolder> () {

    val PREFS_FILENAME = "com.kultur.newsapplication.Adapter.ViewHolder"
    val keySources = "source"
    val imageShared="image"

    private var progressBar: ProgressBar? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListNewsViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        val itemView=inflater.inflate(R.layout.news_layout, parent,false)

        progressBar = itemView.findViewById<ProgressBar>(R.id.progress_load_photo) as ProgressBar
        progressBar!!.visibility= View.VISIBLE
        return ListNewsViewHolder(itemView)

    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ListNewsViewHolder, position: Int) {

        Picasso.get().load(articleList[position].urlToImage)
            .into(holder.article_image)
        progressBar!!.visibility= View.GONE
        if(articleList[position].title!!.length > 65 ){
            holder.article_title.text=articleList[position].title!!.substring(0,65)+"..."
        }else{
            holder.article_title.text=articleList[position].title!!
        }

        if(articleList[position].publishedAt !=null){
            var date: Date?=null
            try {
                date= ISO8601Parser.parse(articleList[position].publishedAt!!)
            }catch (ex: ParseException){
                ex.printStackTrace()
            }
            holder.article_time.setReferenceTime(date!!.time)
        }

        holder.setItemClickListener(object : ItemClickListener {
            override fun onClick(view: View, position: Int) {

                val detail= Intent(context, NewsDetail::class.java)
                detail.putExtra("webURL",articleList[position].url)
                context.startActivity(detail)
            }

        })



        holder.imgBtn.setOnClickListener {

            val prefences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
            val editor = prefences.edit()
            val message:String= articleList[position].url.toString()
            editor.putString(keySources,message)
            editor.apply()
            FancyToast.makeText(context,"Success", FancyToast.LENGTH_LONG, FancyToast.SUCCESS,false).show()

            holder.imgBtn.setBackgroundResource(R.drawable.ic_favorite_dark)

//            val urlKayit = prefences.getString(message,articleList[position].url.toString())
//            Log.e("kayit",urlKayit.toString())
        }

        holder.shareBtn.setOnClickListener {
            val message:String= articleList[position].url.toString()

            val share= Intent()
            share.action= Intent.ACTION_SEND
            share.putExtra(Intent.EXTRA_TEXT, message)
            share.type="text/plain"

            context.startActivity(Intent.createChooser(share,"Share"))
        }

    }


    override fun getItemCount(): Int {
        return articleList.size
    }
}