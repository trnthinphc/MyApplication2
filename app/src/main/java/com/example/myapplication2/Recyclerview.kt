package com.example.myapplication2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication2.adapter.HorizontalRecycleView

class Recyclerview : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HorizontalRecycleView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)

        recyclerView = findViewById(R.id.recyclerView)
        val dataList: List<RecycleViewSlide> =
            listOf(
                RecycleViewSlide(R.drawable.pt1, "Tree"),
                RecycleViewSlide(R.drawable.pt2, "Climbers"),
                RecycleViewSlide(R.drawable.pt3, "Shrubs"),
                RecycleViewSlide(R.drawable.pt4, "Creepers"),
                RecycleViewSlide(R.drawable.pt5, "Herbs")
            )
        adapter = HorizontalRecycleView(dataList)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
    }
}