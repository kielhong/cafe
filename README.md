# Simple Naver Cafe
- Naver Cafe의 핵심 기능 위주의 단순화 버전
- Spring 5.x 기반 기술 적용
- 동적 화면 변경은 AngularJS 이용

## Dependencies
- Java 8
- Spring 5.1.10
- Spring Boot 2.1.9
- Spring Security 5.1.16
- Spring Data JPA 2.1.11
- Spring Data MongoDB 2.1.11
- Hibernate 5.3.12
### View
- Thymleaf 3.0.8
- AngularJS 1.6.1
### Test
- JUnit 5.3.x
- Mockito 2.23.x

## Features
### Cafe Section
- Cafe 메인 화면
  - 주제별 카페 주제 변경시 동적 변경 (AngularJS)
- Cafe 생성
### My Cafe
- Cafe 개별 카페 정보
  - Boards 게시판 목록
- Cafe 가입
- 전체/게시판 별 Article 목록
- Article 조회
- Article 작성
- Article 별 Comment 목록
- Comment 작성

## 구성
### Main/Java
- Domain
  - Cafe, Article, Member, CafeMember 의 Aggregate 단위로 Domain 작성
  - Domain과 연관된 Repsitory 
- Service
  - 기능 담당하는 Service 객체
- Projection
  - Spring Data Jpa에서 Repository 결과값을 제어하기 위한 Projection interface 들 모음
- Api
  - AngularJS 에서 사용하기 위한 API들
  - RESTful API 
- Web
  - 카페 Section, 개별 카페 메인 Controller
### Main/Resources
- static/js/ng
  - AngularJS Controller 들 위치
  - sectionController.js - 카페 스토리 영역 변경, 주제별 카페 목록 변경
  - cafeCreateController.js - 카페 생성 처리
  - cafeController.js - 게시물 목록, 게시물 조회, 게시물 작성, 덧글 목록, 덧글 작성 처리
### Test
- Api
  - 정상 결과, 권한 오류 등에 대해 HTTP Status 테스트
  - jsonPath를 이용해서 Mock 객체에 따른 json 결과값 테스트
- Domain, Service
  - 모든 기능 및 결과값 없는 경우에 대한 테스트
  - Mockito를 이용한 Mock 처리
- Repository
  - Spring Data Jpa 의 기본 제공 method (findById())외의 method에 대해 테스트
