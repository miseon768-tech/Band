plugins {
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.7"
	java
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "BandProject"

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(21))
	}
}

repositories {
	mavenCentral()
}

dependencies {
    // 핵심 웹 스타터
    implementation("org.springframework.boot:spring-boot-starter-web")

    // MyBatis 추가 (Spring Boot 3용 최신 버전)
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.3")

    // 타임리프 템플릿 엔진
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    // 롬복
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // 개발 편의 도구 및 DB
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.mysql:mysql-connector-j")

    // 테스트 도구
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // 메일 발송을 위한 스타터 라이브러리 추가
    implementation("org.springframework.boot:spring-boot-starter-mail")

    // Spring Security 추가
    implementation("org.springframework.boot:spring-boot-starter-security")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
