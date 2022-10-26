package com.lee.fragment.stopwatchpj

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
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
    private var lagtime = 0 // 눌렸을때 시간
    private val timeList : MutableList<myTime> = mutableListOf()
    private val lagList : MutableList<myTime> = mutableListOf()
    private lateinit var livemytime: myTime
    private lateinit var restarttime: myTime

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
                if(isRunning) save() else reset()  // 시작 혹은 정지
            }
        }
    }
    // back key로 완전 종료시키기
    override fun onBackPressed() {
        finish()
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
        if(minute >= 60){  // 분 -> 시
            hour = minute / 60
            minute %= minute
        }
        if( seconds >= 60){ // 초 -> 분
            minute = seconds / 60
            seconds %= 60
        }
        //textview에 들어갈 시간 출력부분 만들기, 한자리의 경우 앞에0 추가
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
        return settingmyTime  // data class 반환
    }

    /**
     * 시간 측정 시작 함수
     */
    private fun start(){
        binding.fabReset.text = "구간기록" // 기록으로 버튼 변경
        binding.fabStart.text = "정지"// 시작 아이콘
        // 시간 증가
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
    /**
     * 구간기록 함수
     */
    private fun save(){
        supportFragmentManager.popBackStack("saveTimeList",
            FragmentManager.POP_BACK_STACK_INCLUSIVE)
        lagList.add(timesetting(repeatedTime - lagtime))
        timeList.add(timesetting(repeatedTime))
        lagtime = repeatedTime  // 저장 이후 시작하는 시간
        var bundle = Bundle() // saveFragment 전달
        var saveFragment = saveTimeFragment() // 외부로 나가면 화면 갱신이 안됨

        bundle.putParcelableArrayList(EXTRA_SAVETIME, timeList as ArrayList<out Parcelable?>?)
        bundle.putParcelableArrayList(EXTRA_LAG, lagList as ArrayList<out Parcelable?>?)

        saveFragment.arguments = bundle
        supportFragmentManager!!.beginTransaction().apply {
            add(R.id.saveTimeContainer,saveFragment)
            setReorderingAllowed(true)
            addToBackStack("saveTimeList")  // 초기화때 삭제를 위한 이름 명시
            commit()
        }
    }
    /**
     * 정지 함수
     */
    private fun pause(){
        binding.fabReset.text = "초기화" // 구간기록에서 초기화로 버튼 변경
        binding.fabStart.text = "시작" // 정지시 시작 버튼 변경
        timerTask?.cancel() // start 에서 가동된 부분 정지
    }
    @SuppressLint("SetTextI18n")
    private fun reset(){
        binding.fabReset.text = "초기화" // 초기화 버튼
        timerTask?.cancel() // 정지 후
        repeatedTime = 0 // 시간 0으로 초기화
        lagtime = 0 // 시간차 초기화
        lagList.clear()  // 시간차 저장 list 초기화
        timeList.clear()  // 구간기록 저장 list 초기화
        isRunning = false // 정지상태
        with(binding){
            // 시간 기록들 0으로 재출력
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
            // saveTimeFragment 삭제
            supportFragmentManager.popBackStack("saveTimeList",
                FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

}