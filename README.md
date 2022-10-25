# StopWatchPJ
## StopWatch 기능 구현 App
------------
### 주요기능
+ 밀리초 단위 시간 측정
+ 구간 기록
+ 구간마다 시차 기록
------------
### 설계
1. 핵심 변수  
  -repeatedTime: 시간 측정용  
  -lagtime : 구간 측정용  
  -시간 값 저장용 data class
  
       data class myTime(var hour:String, var minute: String, var second: String, var milsecond: String)    
    
  

2. 시간 출력  
  -밀리초를 받아 시계 구성에 맞춰 string으로 변환해주는 함수  
  
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
    

3. 동작 방식

------------
### 실행사진

![KakaoTalk_20221026_021038648_03](https://user-images.githubusercontent.com/37658906/197842153-f4cd8567-2684-448d-b151-67a6472acbd4.jpg)
![KakaoTalk_20221026_021038648_02](https://user-images.githubusercontent.com/37658906/197842140-4ad1672b-80ec-4c66-aae3-f93af5f3261c.jpg)
![KakaoTalk_20221026_021038648_01](https://user-images.githubusercontent.com/37658906/197842131-678300ee-7e93-4a38-a5c8-c76d2a9c5567.jpg)


