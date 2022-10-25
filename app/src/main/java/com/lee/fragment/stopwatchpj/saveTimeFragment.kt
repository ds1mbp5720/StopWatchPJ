package com.lee.fragment.stopwatchpj

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.rangeTo
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lee.fragment.stopwatchpj.databinding.RecyclerTimeBinding
import com.lee.fragment.stopwatchpj.databinding.SavetimeLayoutBinding

class saveTimeFragment:Fragment() {
    companion object{
        fun newInstance() = saveTimeFragment()
    }
    private lateinit var binding: SavetimeLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SavetimeLayoutBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 출력되는 list 형식
        val listManager = LinearLayoutManager(activity as Activity,
        LinearLayoutManager.VERTICAL, true)
        with(binding.saveTimeList){
            layoutManager = listManager
            adapter = saveTimeRecyclerViewAdapter(timeSave(),lagtimeSave(),activity as Activity)
            println(timeSave()) // list 확인
        }
    }
    // 전달받은 list를 data clss 형식 list로 반환
    private fun lagtimeSave() = mutableListOf<myTime>().apply {
        var recevietime = arguments?.getIntArray(EXTRA_LAG)
        if(recevietime != null){
            for(i in 0 until recevietime!!.size){
                add(timesetting(recevietime[i]))
            }
        }
    }
    private fun timeSave() = mutableListOf<myTime>().apply {
        var recevietime = arguments?.getIntArray(EXTRA_SAVETIME)
        if(recevietime != null){
            for(i in 0 until recevietime!!.size){
                add(timesetting(recevietime[i]))
            }
        }
    }

    /**
     * main에서 받은 값을 시간 양식에 맞춰 변경 함수
     */
    private fun timesetting(checkTime: Int):myTime{
        var settingmyTime = myTime("","","","")
        var hour = 0
        var minute = 0
        var seconds = checkTime / 100
        var milliseconds = checkTime % 100
        if(minute >= 60){  // 분을 시로
            hour = minute / 60
            minute %= minute
        }
        if( seconds >= 60){ // 초를 분으로
            minute = seconds / 60
            seconds %= 60
        }
        var stringHour = if (hour >=1) "${hour}:" //시부분
        else ""
        var stringMinute = if(minute >=10) "${minute}:" //분부분
        else "0${minute}:"
        var stringSecond = if(seconds >=10) "${seconds}."  //초부분
        else "0${seconds}."
        var stringMilsecond = if(milliseconds >=10) "${milliseconds}" //밀리 초부분
        else "0${milliseconds}"

        settingmyTime.hour = stringHour
        settingmyTime.minute = stringMinute
        settingmyTime.second = stringSecond
        settingmyTime.milsecond = stringMilsecond
        return settingmyTime
    }
}