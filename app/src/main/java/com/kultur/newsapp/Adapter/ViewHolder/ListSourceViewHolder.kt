package com.kultur.newsapp.Adapter.ViewHolder

import android.view.View
import android.widget.Switch
import androidx.recyclerview.widget.RecyclerView
import com.kultur.newsapp.ItemClickListener
import com.kultur.newsapp.SaveData
import kotlinx.android.synthetic.main.source_news_layout.view.*

class ListSourceViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private lateinit var itemClickListener: ItemClickListener
    var source_title=itemView.source_news_name
    private var switch: Switch?=null
    private lateinit var saveData: SaveData

    init {
        itemView.setOnClickListener(this)
    }

    fun setItemClickListener (itemClickListener: ItemClickListener){
        this.itemClickListener=itemClickListener
    }

    override fun onClick(v: View?) {
        itemClickListener.onClick(v!!, adapterPosition)
    }
}