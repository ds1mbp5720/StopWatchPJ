package com.lee.fragment.stopwatchpj

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lee.fragment.stopwatchpj.databinding.RecyclerTimeBinding


class saveTimeRecyclerViewAdapter(
    private val timeList: MutableList<myTime>,
    private val lagtimeList: MutableList<myTime>,
    private val owner: Activity
):RecyclerView.Adapter<saveTimeRecyclerViewAdapter.mytimeViewHolder>(){

    inner class mytimeViewHolder(val binding: RecyclerTimeBinding):
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mytimeViewHolder {
        val binding = RecyclerTimeBinding.inflate(
            LayoutInflater.from(parent.context),parent, false)
        return mytimeViewHolder(binding)
    }
    // 리스트에 저장될 양식
    override fun onBindViewHolder(holder: mytimeViewHolder, position: Int) {
        val timeData = timeList[position]
        val lagData = lagtimeList[position]
        with(holder.binding){
            listNum.text = "${position+1}"  // 저장 번호
            //구간 시간차 시간
            betweenhour.text = lagData.hour
            betweenminute.text = lagData.minute
            betweensecond.text = lagData.second
            betweenmilsecond.text = lagData.milsecond
            //저장 누른 시간
            hour.text = timeData.hour
            minute.text = timeData.minute
            second.text = timeData.second
            milsecond.text = timeData.milsecond
        }
    }

    override fun getItemCount() = timeList.size
}