# 나만의 일정 관리 앱 서버 만들기

## ⚙ STACK
![](https://img.shields.io/badge/SpringBoot-6db33f?style=flat-square&logo=springboot&logoColor=white)
![](https://img.shields.io/badge/Gradle-02303a?style=flat-square&logo=gradle&logoColor=white)
![](https://img.shields.io/badge/IntelliJ-000000?style=flat-square&logo=intellijidea&logoColor=white)
![](https://img.shields.io/badge/Postman-ff6c37?style=flat-square&logo=postman&logoColor=white)
![](https://img.shields.io/badge/Git-f05032?style=flat-square&logo=git&logoColor=white)

<div style="display: flex; align-items: flex-start;">
<img src="https://techstack-generator.vercel.app/java-icon.svg" alt="icon" width="65" height="65" />
<img src="https://techstack-generator.vercel.app/mysql-icon.svg" alt="icon" width="65" height="65" />
<img src="https://techstack-generator.vercel.app/github-icon.svg" alt="icon" width="65" height="65" />
<img src="https://techstack-generator.vercel.app/restapi-icon.svg" alt="icon" width="65" height="65" />
</div>

## API
### [🔗 API Document](https://documenter.getpostman.com/view/37564576/2sA3s4mqhN)

| 기능        | Method | URL                    | request | response       |
|-----------|--------|------------------------|---------|----------------|
| 일정 등록     | POST   | /api/scheduler         | body    | 등록 정보      |
| 일정 전체 조회  | GET    | /api/scheduler         | param   | 다건 응답 정보 |
| 일정 수정     | PUT    | /api/scheduler/{id}    | body    | 수정 정보      |
| 일정 삭제     | DELETE | /api/scheduler/{id}    | param   | 삭제 정보      |
| 선택한 일정 조회 | GET    | /api/scheduler/{id}    | param   | 단건 응답 정보 |
| 일정 목록 조회  | GET    | /api/scheduler/search  | query   | 다건 응답 정보 |


## ERD
### [🔗 ERDCloud](https://www.erdcloud.com/d/bKfi5Aojohi64giyD) <br/>
![ERD](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbQAN84%2FbtsI3NiTenM%2FwbxUKQrW4dq3dExzVD6w1k%2Fimg.png)

## SQL
### [🔗 schedule.sql](https://github.com/everydayspring/spring-scheduler/blob/main/schedule.sql) <br/>
![SQL](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2F0VvWI%2FbtsI4y6ft8u%2Fh8kl4lnkkYKkoQwG3GYdyK%2Fimg.png)

