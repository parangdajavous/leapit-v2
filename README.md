# Spring Boot 기반 구인구직 플랫폼 → RESTful 자원 서버로 전환 

![로고](./image/leapit.png)
> 기존에 웹 화면 중심으로 구현된 ‘Spring Boot 기반 구인구직 플랫폼’을 프론트엔드-백엔드 완전 분리 구조의 RESTful API 서버로 재구성한 프로젝트입니다.
> Spring REST Docs를 통해 API 문서를 자동 생성하여, API 명세 관리와 유지보수 효율성을 높였으며, JWT 기반 인증, 예외 처리 구조 고도화, 테스트 기반 개발까지 적용하여 실제 서비스 수준의 백엔드 아키텍처 설계 및 구현 경험을 목표로 삼았습니다.

## 프로젝트 시연영상
<video src="https://github.com/user-attachments/assets/703e846f-0662-445c-a3e1-4deadbab5e60" controls width="600"></video>


## 목차
1. [🗓️ 개발 기간 및 참여 인원](#개발기간및참여인원)
2. [🔚 회고](#회고)
3. [📄 API 문서](#API문서)
4. [💡 주요 기능](#주요기능)
5. [✍️ 개인 기여도 및 역할](#개인기여도)
6. [👥 팀원](#팀원)
7. [🛠️ 기술 스택](#기술스택)
8. [🧩 문제 해결 경험](#문제해결경험)
9. [📋 ERD](#erd)


<a id="개발기간및참여인원"></a>
## 🗓️ 개발 기간 및 참여 인원
- 기간: 2025.05.12 ~ 2025.05.22
- 인원: 4인 팀 프로젝트


<a id="회고"></a>
## 🔚 회고
**1️⃣ 협업 규칙을 지키는 것의 어려움**

두 번째 프로젝트는 첫 번째보다 익숙해졌기에 상대적으로 수월할 것이라 예상했지만, 막상 진행해보니 **데이터만 주고받는 구조임에도 불구하고 예상 외의 어려움이 많았습니다.**
특히 **JSON 기반으로 요청과 응답을 정확하게 주고받는 과정에서 세밀한 조율의 중요성**을 느꼈고, 팀 내에서 정의한 컨벤션을 **끝까지 일관되게 지키는 것이 얼마나 어려운 일인지** 다시금 절감하게 되었습니다.
이번 경험을 통해 협업은 단순한 역할 분담이 아니라 **약속을 지키고, 서로를 배려하며 맞춰가는 과정**이라는 사실을 깨달았습니다. 마지막 프로젝트에서는 더 나은 협업자이자 개발자로 성장할 수 있도록 **끝까지 책임감 있게 임하겠습니다.**

<a id="API문서"></a>
## 📄 API 문서
<img src="image/restApi.png" width="400"/>
<img src="image/Authorization.png" width="400"/>
<img src="image/Request&Response.png" width="400"/>


<a id="주요기능"></a>
## 💡 주요 기능
### 👤 개인

- 회원가입 / 로그인 / 로그아웃 / 회원정보 수정 (REST API 설계 및 문서화)
- 채용공고 탐색 (직무, 기술스택, 경력, 지역, 최신/인기 정렬 API)
- 채용공고 상세 조회 및 이력서 지원 API
- 이력서 등록 / 수정 / 삭제 / 사진 업로드 (Multipart API)
- 지원 내역 / 스크랩 공고 마이페이지 API
- 합불 여부 결과 확인 API

### 🏢 기업

- 회원가입 / 로그인 / 로그아웃 / 기업 정보 수정 API
- 채용공고 등록 / 수정 / 조회 API (여러 이미지 업로드 포함)
- 지원자 이력서 열람 / 스크랩 / 합격 여부 처리 API


<a id="개인기여도"></a>
## ✍️ 개인 기여도 및 역할
| 구분 | 기능명 | 설명 |
| --- | --- | --- |
| (기업)  | 기업 정보 보기 / 등록 / 수정 | 기업이 작성한 기업 정보에 대해 상세보기(READ), 등록(CREATE), 수정(UPDATE) 기능 구현. 응답 DTO 설계 및 매핑 |
| (개인)  | 기업 정보 상세보기 | 기업이 작성한 기업 정보를 개인이 상세히 조회할 수 있도록 조회(READ) API 구현 |
| (개인)  | 커뮤니티 게시글 - 보기 / 등록 / 수정 / 삭제 | 개인 사용자 전용 커뮤니티 게시글의 목록 및 상세보기(READ), 등록(CREATE), 수정(UPDATE), 삭제(DELETE) 기능 구현 |
| (개인)  | 커뮤니티 댓글 - 보기 / 등록 / 삭제 | 커뮤니티 게시글에 달린 댓글 조회(READ), 작성(CREATE), 삭제(DELETE) 기능 구현 |
| (개인)  | 커뮤니티 좋아요 - 보기 / 등록 / 삭제 | 커뮤니티 게시글에 대한 좋아요 목록 조회(READ), 좋아요 등록(CREATE), 좋아요 삭제(DELETE) 기능 구현 |



<a id="팀원"></a>
## 👥 팀원
<table>
  <thead>
    <tr>
      <th>김주희</th>
      <th>백하림</th>
      <th>김정원</th>
      <th>김미숙</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><img src="https://avatars.githubusercontent.com/u/127887466?v=4" alt="김주희" width="100" /></td>
      <td><img src="https://avatars.githubusercontent.com/u/143974982?v=4" alt="백하림" width="100" /></td>
      <td><img src="https://avatars.githubusercontent.com/u/203729981?v=4" alt="김정원" width="100" /></td>
      <td><img src="https://avatars.githubusercontent.com/u/203644415?v=4" alt="김미숙" width="100" /></td>
    </tr>
    <tr>
      <td>팀장</td>
      <td>팀원</td>
      <td>팀원</td>
      <td>팀원</td>
    </tr>
    <tr>
      <td><a href="https://github.com/jh0804">GitHub</a></td>
      <td><a href="https://github.com/harimmon">GitHub</a></td>
      <td><a href="https://github.com/hahamik">GitHub</a></td>
      <td><a href="https://github.com/parangdajavous">GitHub</a></td>
    </tr>
  </tbody>
</table>




<a id="기술스택"></a>
## 🛠️기술스택
| 구분 | 기술 스택 |
|------|-----------|
| 💻 Backend | <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/Springboot-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white"> <img src="https://img.shields.io/badge/JSON-000000?style=for-the-badge&logoColor=white" alt="JSON" /> |
| 🛢 DataBase | <img src="https://img.shields.io/badge/H2_Console-4479A1?style=for-the-badge&logo=h2&logoColor=white"/> |
| 🔐 보안 / 인증 | <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white"> |
| 📦 ORM / JPA | <img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white"> |
| 🌐 API / 문서화 | <img src="https://img.shields.io/badge/Spring%20REST%20Docs-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> <img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white"> |
| 🧪 테스트 | <img src="https://img.shields.io/badge/JUnit-25A162?style=for-the-badge&logo=JUnit5&logoColor=white"> <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> |
| ☁ 협업 도구 | <img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white"> <img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white"> |



<a id="문제해결경험"></a>
## 🧩 문제 해결 경험
### 💬 문제 : Base64 인코딩된 이미지 문자열 처리 문제

- **문제 상황**
    - 처음 프론트엔드에서 이미지 데이터를 서버로 전달할 때 어떻게 전달하는지 잘 몰라서 **MultipartFile 방식**으로 처리했고, 그로 인해 DTO 필드에 값이 전달되지 않는 문제 발생
    - 이후 프론트에서 **이미지를 Base64 인코딩된 문자열 형태로 전달한다는 걸 인지하고**, DTO에 Base64 문자열 필드를 추가하는 방식으로 코드를 수정했지만, **디코딩하지 않고 그대로 문자열을 DB에 저장해버리는 문제**가 다시 발생
- **원인 분석**
    - 백엔드에서는 이미지를 **MultipartFile 방식**로만 처리하도록 되어 있었기 때문에, 프론트에서 전달된 **Base64 문자열이 `image` 필드에 매핑되지 않아 null로 처리**됨
    - 이후 Base64 문자열로 전달된다는 사실을 인지하고 DTO 구조를 수정했지만, 서버 측에서 **Base64 문자열을 디코딩하지 않고 그대로 DB에 저장**하면서 이미지가 실제로 저장되지 않고, **데이터 형식이 깨지는 문제**가 발생함
- **해결 방법**:
    - DTO에 **Base64 문자열 필드(`imageFileContent`)를 따로 선언**
    - 서비스 계층에서 해당 문자열을 받아 직접 디코딩 처리
    - 디코딩된 바이너리 데이터를 파일로 변환하여 서버 `upload/` 디렉토리에 저장
    - DB에는 실제 이미지 파일 경로 (`image`)만 저장
<img width="400" height="400" alt="imaxcvxcvcvxcvxcvxcvzxsssge" src="https://github.com/user-attachments/assets/8087fd63-28c7-4c6c-bc8a-6b801b3915a8" />
<img width="400" height="400" alt="imadvsvcvxcvxcvcvcvge" src="https://github.com/user-attachments/assets/9b5f7968-9998-44c6-9592-7ac78976c5f6" />



<a id="erd"></a>
## 📋 ERD
<img width="1798" height="1166" alt="erd (1)" src="https://github.com/user-attachments/assets/cb500155-aa51-4563-b37f-a14234a7f83c" />
