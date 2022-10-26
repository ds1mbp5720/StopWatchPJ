# StopWatchPJ
## StopWatch 기능 구현 App
> 안드로이드 기기 stopwatch와 동일한 기능 App 구현하였습니다. 밀리초 단위까지 시간 측정하며 구간 기록, 구간마다 시차또한 출력합니다.
------------
### 주요기능
+ 밀리초 단위 시간 측정
+ 구간 기록
+ 구간마다 시차 기록
------------
### 설계
#### 1. 핵심 변수, 함수  
  - repeatedTime: 시간 측정용  
  - lagtime : 구간 측정용  
  - 시간 값 저장용 data class
  
         data class myTime(var hour:String, var minute: String, var second: String, var milsecond: String)        
      
   - timesetting(checkTime: Int):myTime: 밀리초를 받아 시계 구성에 맞춰 string으로 변환해주는 함수    
    
   - start() : 시간 동작  
   
          kotlin.concurrent.timer(period = 10)  
   
   - save() : 시간 측정 및 구간 측정
   - puse() : 정지
   - reset() : 초기화
   
#### 3. 동작 방식
  - activity에 버튼 및 시간 측정 구현
  - recyclerView 활용 측정한 시간들 list 출력
  - 측정 저장값들 bundle을 통한 값 전달

------------
### 실행사진

![KakaoTalk_20221026_021038648_03](https://user-images.githubusercontent.com/37658906/197842153-f4cd8567-2684-448d-b151-67a6472acbd4.jpg)
![KakaoTalk_20221026_021038648_02](https://user-images.githubusercontent.com/37658906/197842140-4ad1672b-80ec-4c66-aae3-f93af5f3261c.jpg)
![KakaoTalk_20221026_021038648_01](https://user-images.githubusercontent.com/37658906/197842131-678300ee-7e93-4a38-a5c8-c76d2a9c5567.jpg)


