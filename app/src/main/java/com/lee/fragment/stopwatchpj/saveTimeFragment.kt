package com.lee.fragment.stopwatchpj

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val listmaneger = LinearLayoutManager(activity as Activity,
        LinearLayoutManager.VERTICAL, false)
        with(binding.saveTimeList){
            layoutManager = listmaneger
            adapter = saveTimeRecyclerViewAdapter(timeSave(),activity as Activity)
        }


    }
    private fun timeSave() = mutableListOf<myTime>().apply {
        val hour = arguments?.getString(EXTRA_HOUR)
        val minute = arguments?.getString(EXTRA_MINUTE)
        val second = arguments?.getString(EXTRA_SECOND)
        val milsecond = arguments?.getString(EXTRA_MILSECOND)
        if(hour != null && minute != null && second != null && milsecond != null ){
            add(myTime(hour,minute,second,milsecond))
        }

        println("받았다")
        /*var intent = Intent()
        with(binding){
            with(intent){
                add(myTime(getIntExtra(EXTRA_HOUR,0),getIntExtra(EXTRA_MINUTE,0),
                            getIntExtra(EXTRA_SECOND,0), getIntExtra(EXTRA_MILSECOND,0)))
                println("받았다")
            }
        }*/
    }
}