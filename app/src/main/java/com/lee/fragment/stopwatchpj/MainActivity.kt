package com.lee.fragment.stopwatchpj

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.widget.TextView
import com.lee.fragment.stopwatchpj.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList

/** -사양-
 *  버튼 묶음: (시작 -> 중지 -> 계속), (구간기록 -> 초기화)
 *  표시 시간 단위: HH:MM:SS.ms
 *  시간 밑에 미니 시간: 구간 기록 후 경과시간
 *  구간기록 양식: 구간(순번) 구간기록(직전 구간 시차) 전체시간(기록한 시간)
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var repeatedTime = 0 //
    private var isRunning = false  // 시작, 정지 구분 boolean 변수
    private var timerTask : Timer? = null  //
    private var lagtime = 0 // 눌렸을때 시간
    private val timeList : MutableList<Int> = mutableListOf()
    private val lagList : MutableList<Int> = mutableListOf()
    private lateinit var livemytime: myTime
    private lateinit var restarttime: myTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(savedInstanceState == null){
            val ft = supportFragmentManager.beginTransaction()
            with(ft){
                add(R.id.saveTimeContainer, saveTimeFragment.newInstance())
                commit()
            }
        }

        with(binding){
            // 시작버튼 (시작 -> 중지 -> 계속)
            fabStart.setOnClickListener{
                isRunning = !isRunning  // 시작과 정지 구분 조건
                if(isRunning) start() else pause()  // 시작 혹은 정지
            }
            // 초기화 (구간기록 -> 초기화)
            fabReset.setOnClickListener{
                if(isRunning) save() else reset()  // 시작 혹은 정지

            }
        }
    }

    /**
     * 밀리초 단위 값 시,분,초,밀리초 출력 양식 조정하여 myTime 형으로 반환 함수
     */
    private fun timesetting(checkTime: Int):myTime{
        var settingmyTime = myTime("","","","")
        var hour = 0
        var minute = 0
        var seconds = checkTime / 100
        var milliceconds = checkTime % 100
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
        var stringMilsecond = if(milliceconds >=10) "${milliceconds}" //밀리 초부분
            else "0${milliceconds}"

        settingmyTime.hour = stringHour
        settingmyTime.minute = stringMinute
        settingmyTime.second = stringSecond
        settingmyTime.milsecond = stringMilsecond
        return settingmyTime
    }

    private fun start(){
        binding.fabReset.text = "구간기록"
        binding.fabStart.text = "정지"// 시작 아이콘
        timerTask = kotlin.concurrent.timer(period = 10){
            repeatedTime++

            livemytime = timesetting(repeatedTime)

            //Main Thread 에서 Runnable 실행
            runOnUiThread{
                with(binding){ // 출력부분 최신화
                    hourText.text = "${livemytime.hour}" //시부분
                    minuteText.text = "${livemytime.minute}" //분부분
                    secondsText.text = "${livemytime.second}"  //초부분
                    millisecondsText.text = "${livemytime.milsecond}" //밀리 초부분

                    if(lagtime >0){ // 구간 기록 눌렀을시 새로 시작하는 시간
                        restarttime = timesetting(repeatedTime - lagtime)
                        rehourText.text = "${restarttime.hour}" //시부분
                        reminuteText.text = "${restarttime.minute}" //분부분
                        resecondsText.text = "${restarttime.second}"  //초부분
                        remillisecondsText.text = "${restarttime.milsecond}" //밀리 초부분
                    }
                }
            }
        }
    }

    private fun save(){
        lagList.add(repeatedTime - lagtime)
        timeList.add(repeatedTime)
        lagtime = repeatedTime  // 저장 이후 시작하는 시간
        var bundle = Bundle()
        var fragment = saveTimeFragment()
        bundle.putIntArray(EXTRA_SAVETIME,timeList.toIntArray())
        bundle.putIntArray(EXTRA_LAG,lagList.toIntArray())
        fragment.arguments = bundle
        supportFragmentManager!!.beginTransaction()
            .replace(R.id.saveTimeContainer,fragment)
            .commit()
    }
    private fun pause(){
        binding.fabReset.text = "초기화"
        binding.fabStart.text = "시작" // 정지시 보여줄 아이콘
        timerTask?.cancel() // start 에서 가동된 부분 정지
    }
    @SuppressLint("SetTextI18n")
    private fun reset(){
        binding.fabReset.text = "초기화"
        timerTask?.cancel() // 정지 후
        repeatedTime = 0 // 시간 0으로 초기화
        lagtime = 0
        lagList.clear()
        timeList.clear()
        isRunning = false // 정지상태
        with(binding){
            fabStart.text = "시작"
            secondsText.post{  // 시간들 0 , 기록들 제거
                hourText.text = ""
                minuteText.text ="00:"
                secondsText.text = "00."
                millisecondsText.text = "00"

                rehourText.text = ""
                reminuteText.text = ""
                resecondsText.text = ""
                remillisecondsText.text = ""
            }
        }
        //초기화시 fragment도 초기화
        supportFragmentManager!!.beginTransaction()
            .replace(R.id.saveTimeContainer,saveTimeFragment.newInstance())
            .commit()
    }
}