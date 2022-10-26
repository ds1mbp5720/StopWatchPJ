package com.lee.fragment.stopwatchpj

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lee.fragment.stopwatchpj.databinding.SavetimeLayoutBinding

class saveTimeFragment:Fragment(){
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
    // 전달받은 data clss 형식 해당 list에 값받기
    @Suppress("DEPRECATION")
    private fun lagtimeSave() = mutableListOf<myTime>().apply {
        var recevietime = arguments?.getParcelableArrayList<myTime>(EXTRA_LAG)
        if(recevietime != null){
            for(i in 0 until recevietime!!.size){
                add(recevietime[i])
            }
        }
    }
    @Suppress("DEPRECATION")
    private fun timeSave() = mutableListOf<myTime>().apply {
        var recevietime = arguments?.getParcelableArrayList<myTime>(EXTRA_SAVETIME)
        if(recevietime != null){
            for(i in 0 until recevietime!!.size){
                add(recevietime[i])
            }
        }
    }
}
