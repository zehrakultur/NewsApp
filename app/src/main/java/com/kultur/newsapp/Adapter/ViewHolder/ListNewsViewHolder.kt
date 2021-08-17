package com.kultur.newsapp.Adapter.ViewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.kultur.newsapp.ItemClickListener
import kotlinx.android.synthetic.main.news_layout.view.*

class ListNewsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private lateinit var itemClickListener: ItemClickListener

    var article_title=itemView.article_title
    var article_image=itemView.article_image
    var article_time=itemView.article_time
    var imgBtn=itemView.imgBtn
    var shareBtn=itemView.shareBtn

    init {
        itemView.setOnClickListener(this)
    }

    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener=itemClickListener
    }
    override fun onClick(v: View?) {
        itemClickListener.onClick(v!!, adapterPosition)
    }
}