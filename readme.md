
# Classroom 프로젝트

이 프로젝트는 Java 및 Spring Boot 기반의 Classroom 관리 시스템입니다.

## 프로젝트 구조

### 1. Java 소스 코드 (`src/main/java/com/example/classroom/`)

- **classroomApplication.java**: 메인 애플리케이션 진입점
- **config/**: AjaxController.java, WebResourceConfig.java 등 환경 및 웹 설정
- **domain/**
  - **classroom/**
	- controller/: ClassroomController.java, IndexController.java
	- entity/: Classroom.java, ClassroomMember.java, DailyCheck.java
	- mapper/: ClassroomMapper.java, ClassroomMemberMapper.java
	- request/: CreateClassroomRequest.java
	- response/: ClassroomWithTeacher.java
  - **member/**
	- controller/: AccountController.java, LoginController.java, SignUpController.java
	- entity/: Member.java, Verification.java
	- mapper/: MemberMapper.java, VerificationMapper.java
	- request/: AccountCreationRequest.java, LoginRequest.java, ProfileSetupRequest.java, VerifyRequest.java
  - **notice/**
	- entity/: Notice.java, NoticeAttachment.java, NoticeReply.java
	- mapper/: NoticeMapper.java
	- request/: CreateNoticeRequest.java, CreateReplyRequest.java
	- response/: NoticeWithAttachment.java

### 2. 리소스 파일 (`src/main/resources/`)

- **application.properties**: Spring Boot 설정 파일
- **mappers/**: MyBatis 매퍼 XML (classroom-mapper.xml, classroom-member-mapper.xml, member-mapper.xml, notice-mapper.xml, verication-mapper.xml)
- **static/**
  - images/: empty_states_home.png, logo-wide.png 등 정적 이미지 파일
- **templates/**
  - index-logon.html, index.html, login.html
  - account/: verify.html
  - classroom/: attendance.html, form.html, main.html
  - signup/: account.html, profile.html

### 3. 기타 주요 파일

- **build.gradle.kts**: Gradle 빌드 설정
- **settings.gradle.kts**: Gradle 프로젝트 설정

## 주요 기능
- 클래스룸 생성 및 관리
- 멤버 관리 및 인증
- 출석 관리
- 공지사항 및 댓글 관리
- 회원가입, 로그인, 계정 인증

## 실행 방법
1. JDK 17 이상이 설치되어 있는지 확인하세요.
2. 프로젝트 루트 디렉토리에서 아래 명령어로 실행합니다:

```bash
./gradlew bootRun
```

3. 웹 브라우저에서 `http://localhost:8080` 접속

## 개발 환경
- Java 17 이상
- Spring Boot
- Gradle


## 기여 방법
1. 이슈를 등록하거나, 포크 후 PR을 생성해주세요.
2. 커밋 메시지는 명확하게 작성해주세요.

## 라이선스
MIT License

