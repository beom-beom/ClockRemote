# 📱 ClockRemote (IR-Sync Android App)

> **Android 스마트폰의 IR 센서를 이용하여 사내 전자시계를 동기화하고 제어하는 리모컨 애플리케이션입니다.**

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white)

---

## 🚀 주요 기능 (Key Features)

* **IR 광동기화 (IR Synchronization):** 스마트폰의 IR 블래스터를 활용한 시간 동기화 신호 송신.
* **사용자 친화적 UI:** Noto Sans CJK KR 기반의 가독성 높은 디자인과 다크/라이트 테마 지원.
* **하드웨어 체크:** 앱 실행 시 IR 센서 유무를 자동으로 감지하여 예외 상황 방지.
* **CI/CD 자동화:** GitHub Actions를 통한 자동 빌드 및 APK 결과물 생성.

## 🛠 기술 스택 (Tech Stack)

* **Language:** Kotlin / Java
* **IDE:** Android Studio / Project IDX
* **Build System:** Gradle (Kotlin DSL) / AndroidX
* **Automation:** GitHub Actions (CI/CD)
* **Target SDK:** API 33 (Android 13.0)

## 📂 프로젝트 구조 (Project Structure)

```text
ClockRemote/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/clockremote/  # 메인 로직
│   │   ├── res/                           # 레이아웃 및 리소스
│   │   └── AndroidManifest.xml            # 권한 및 앱 설정
│   └── build.gradle.kts                   # 모듈 빌드 설정
├── .github/workflows/                     # CI/CD 자동화 스크립트
├── gradle.properties                      # AndroidX 및 환경 설정
└── README.md
