import com.google.protobuf.gradle.id

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.3"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("kapt") version "1.9.25"

    // protobuf 플러그인을 통해 .proto 파일을 컴파일
    id("com.google.protobuf") version "0.9.4"
}
group = "com.hoang"
version = "0.0.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

object Version {
    const val PROTOBUF = "4.27.2"
    const val GRPC = "1.65.1"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // jpa, h2 database
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.3.3")
    runtimeOnly("com.h2database:h2:2.3.232")

    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    kapt("org.mapstruct:mapstruct-processor:1.5.5.Final")

    // grpc 프로토콜 버터를 사용하기 위한 핵심 라이브러리 (Protobuf 메시지의 직렬화 및 역직렬화를 지원합니다.)
    implementation("com.google.protobuf:protobuf-java-util:${Version.PROTOBUF}")
    implementation("com.google.protobuf:protobuf-java:${Version.PROTOBUF}")

    // grpc 서버, 클라이언트 설정
    implementation("net.devh:grpc-spring-boot-starter:3.1.0.RELEASE") // Spring Boot와 gRPC의 통합을 간편하게 도와주는 스타터
    implementation("io.grpc:grpc-netty-shaded:${Version.GRPC}") // Netty Shaded 사용(gRPC 서버와 클라이언트의 Netty 전송 계층을 제공)
    implementation("io.grpc:grpc-protobuf:${Version.GRPC}")     // Protobuf 메시지와 gRPC의 통합을 지원
    implementation("io.grpc:grpc-stub:${Version.GRPC}")        // gRPC 클라이언트 스텁을 생성
    kapt("org.apache.tomcat:annotations-api:6.0.53")    // 이걸 추가해야 gRPC 컴파일시 javax 어노테이션 오류가 발생하지 않는다.

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${Version.PROTOBUF}"
    }

    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${Version.GRPC}"
        }
    }

    generateProtoTasks {
        all().forEach {
            it.plugins {
                create("grpc") {}
            }
        }
    }
}

tasks.named("clean") {
    doLast {
        delete(File("${layout.buildDirectory}/generated/source/proto"))
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

//tasks.withType<Test> {
//    useJUnitPlatform()
//}
