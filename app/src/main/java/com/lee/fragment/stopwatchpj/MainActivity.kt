package com.lee.fragment.stopwatchpj

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.lee.fragment.stopwatchpj.databinding.ActivityMainBinding
import java.util.*

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
    private var countRecord = 0 //구간 횟수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding){
            // 시작버튼 (시작 -> 중지 -> 계속)
            fabStart.setOnClickListener{
                isRunning = !isRunning  // 시작과 정지 구분 조건
                if(isRunning) start() else pause()  // 시작 혹은 정지
            }
            // 초기화 (구간기록 -> 초기화)
            fabReset.setOnClickListener{
                reset()
            }
            // 저장
            btnLab.setOnClickListener{
                if(repeatedTime != 0) currentTapTime()
                binding.recordTitle.text = "구간        구간기록              전체시간"
            }
        }
    }
    private fun start(){
        binding.fabStart.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24) // 시작 아이콘
        timerTask = kotlin.concurrent.timer(period = 10){
            repeatedTime++

            var hour = 0
            var minute = 0
            var seconds = repeatedTime /100
            if(minute >= 60){  // 분을 시로
                hour = minute / 60
                minute %= minute
            }
            if( seconds >= 60){ // 초를 분으로
                minute = seconds / 60
                seconds %= 60
            }
            var milliseconds = repeatedTime % 100

            //Main Thread 에서 Runnable 실행
            runOnUiThread{
                with(binding){ // 출력부분 최신화
                    if (hour >=1) hourText.text = "${hour}" //시부분
                    if(minute >=10) minuteText.text = "$minute:" //분부분
                    else minuteText.text = "0${minute}:"
                    if(seconds >=10) secondsText.text = "${seconds}."  //초부분
                    else secondsText.text = "0${seconds}."
                    if(milliseconds >=10) millisecondsText.text = "$milliseconds" //밀리 초부분
                    else millisecondsText.text = "0${milliseconds}"
                }
            }
        }
    }
    private fun pause(){
        binding.fabStart.setImageResource(R.drawable.ic_baseline_play_arrow_24) // 정지시 보여줄 아이콘
        timerTask?.cancel() // start 에서 가동된 부분 정지
    }
    @SuppressLint("SetTextI18n")
    private fun reset(){
        timerTask?.cancel() // 정지 후
        repeatedTime = 0 // 시간 0으로 초기화
        isRunning = false // 정지상태
        with(binding){
            fabStart.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            secondsText.post{  // 시간들 0 , 기록들 제거
                minuteText.text ="00:"
                secondsText.text = "00."
                millisecondsText.text = "00"
                lapLayout.removeAllViews()
                lapLayout.invalidate()  //자원 해제
            }
        }
    }
    /**
     * 구간기록 버튼 // 구간(순번) 구간기록(직전 구간 시차) 전체시간(기록한 시간)
     */
    @SuppressLint("SetTextI18n")
    private fun currentTapTime(){
        var lapTime = repeatedTime  // 전체시간
        var betweenRecorTime = 0 // 구간기록
        val textView = TextView(this).apply {
            textSize = 20f
        }
        countRecord++
        textView.text = "$countRecord        " +
                "${lapTime / 100}.${lapTime % 100}              " +  // 기록될 시간 양식
                "${lapTime / 100}.${lapTime % 100}"

        binding.lapLayout.addView(textView,0)
    }
    private fun timesetting(){

    }
}