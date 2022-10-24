package com.lee.fragment.stopwatchpj

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lee.fragment.stopwatchpj.databinding.RecyclerTimeBinding


class saveTimeRecyclerViewAdapter(
    private val timeList: MutableList<myTime>,
    private val owner: Activity
):RecyclerView.Adapter<saveTimeRecyclerViewAdapter.mytimeViewHolder>(){

    inner class mytimeViewHolder(val binding: RecyclerTimeBinding):
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mytimeViewHolder {
        val binding = RecyclerTimeBinding.inflate(
            LayoutInflater.from(parent.context),parent, false
        )
        return mytimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: mytimeViewHolder, position: Int) {
        val timeData = timeList[position]
        with(holder.binding){
            hour.text = timeData.hour
            minute.text = timeData.minute
            second.text = timeData.second
            milsecond.text = timeData.milsecond
        }
    }

    override fun getItemCount() = timeList.size
}