package com.example.myapplication2.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication2.R
import com.example.myapplication2.RecycleViewSlide

class HorizontalRecycleView(private val dataList: List<RecycleViewSlide>) :
    RecyclerView.Adapter<HorizontalRecycleView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        Log.d("HorizontalRecycleView", "dataList: $dataList")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.imgBook.setImageResource(item.imageRes)
        holder.tvTitle.text = item.title
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgBook: ImageView = itemView.findViewById(R.id.img_book)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
    }
}
