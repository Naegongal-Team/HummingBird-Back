# 🐦 Humming-Bird Backend

<br/>
<img src="https://github.com/Naegongal-Team/HummingBird-Back/assets/52346113/6c7b0a62-7492-427b-b8d2-d1750325ff42" width="300px"/>
<br/>

> 허밍버드 프로젝트는 공연 알림 서비스를 제공하는 프로젝트입니다.  
> 해당 레포지토리는 허밍버드 백엔드 전용 레포지토리입니다.

<br/>

### 목차

1. [개요](#개요)
2. [서비스 기능](#서비스-기능)
3. [프로젝트 설계](#프로젝트-설계)
4. [개발 환경 및 기술 스택](#개발-환경-및-기술-스택)
5. [팀원 소개](#팀원-소개)

<br/>
<br/>

## [개요](#목차)

> **개발 기간** : `2023-09-27 ~ 2024-02-29`

|Repository|
|:--------:|
|     [📌 Frontend](https://github.com/Naegongal-Team/HummingBird-Front)        |  

<br/>

#### 허밍버드 서비스

<img width="200px" src="./img/Logotype_main.png">

- 외국 가수의 한국 내한 일정과 티켓팅 정보를 효율적으로 제공하여 사용자들이 놓치지 않고 원하는 공연에 참석할 수 있도록 돕는 내한 공연 알리미 어플입니다.
- 이 서비스를 통해 사용자들은 더욱 간편하게 외국 가수의 내한 공연에 참여할 수 있습니다.
    - 내한 공연 정보가 다양한 플랫폼에 분산되어 있어 발생하는 정보 수집의 어려움을 최소화할 수 있습니다.
    - 알림을 통해 티켓팅 정보를 제공하여 사용자들이 원하는 공연을 놓치지 않고 참석할 수 있도록 도와줍니다.

<br/>
<br/>

## [서비스 기능](#목차)

- 내한 공연 일정 제공
- 티켓팅 정보 제공
- 알림 기능
- 채팅 기능
- 사용자 편의 기능
    - 선호하는 공연과 가수 설정

<br/>
<br/>

## [프로젝트 설계](#목차)

### 시스템 아키텍쳐
<img width="700px" src="./img/SystemArchitecture.png">

<br/>

### ERD
<img width="700px" src="./img/ERD.png">


<br/>

### API 명세서


<br/>
<br/>

## [개발 환경 및 기술 스택](#목차)

|  개발 환경  | 기술 스택                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
|:-------:|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **백엔드** | ![Java](https://img.shields.io/badge/Java_11-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white) ![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white) ![Spring Boot](https://img.shields.io/badge/Spring_Boot_2.7.16-6DB33F?style=for-the-badge&logo=spring&logoColor=white) ![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white) ![OAuth2](https://img.shields.io/badge/OAuth2-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white) <br/> ![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-gray?style=for-the-badge&logo=Spring_Data_JPA&logoColor=white) ![QueryDSL](https://img.shields.io/badge/QueryDSL-0078D4?style=for-the-badge&logo=Querydsl&logoColor=white) ![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white) |
|  **테스트**  | ![](https://img.shields.io/badge/Junit-25A162?style=for-the-badge&logo=JUnit5&logoColor=white) ![](https://img.shields.io/badge/Mockito-6DB33F?style=for-the-badge)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          |
|   **DB**    | ![mariadb](https://img.shields.io/badge/mariadb-4479A1?style=for-the-badge&logo=mariadb&logoColor=white) ![redis](https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white) ![firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
|   **인프라**   | ![amazonec2](https://img.shields.io/badge/amazon_ec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white) ![Amazon S3](https://img.shields.io/badge/Amazon_S3-569A31?&style=for-the-badge&logo=AmazonS3&logoColor=white) ![docker](https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white) ![Jenkins](https://img.shields.io/badge/Jenkins-D24939?style=for-the-badge&logo=Jenkins&logoColor=white)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                |
|   **협업툴**   | ![Git](https://img.shields.io/badge/Git-F05032?&style=for-the-badge&logo=Git&logoColor=white) ![Notion](https://img.shields.io/badge/Notion-000000.svg?style=for-the-badge&logo=notion&logoColor=white) ![discord](https://img.shields.io/badge/discord-5360E4?style=for-the-badge&logo=discord&logoColor=white) ![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white) ![Spring rest docs](https://img.shields.io/badge/Spring_rest_docs-6DB33F?style=for-the-badge&logo=googledocs&logoColor=white)                                                                                                                                                                                                                                                                                                                                                                                 |
| **외부 API**  | ![spotify](https://img.shields.io/badge/spotify_api-000000?style=for-the-badge&logo=spotify&logoColor=white)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |

<br/>
<br/>

## [팀원 소개](#목차)

|                     **[손지민](https://github.com/jmxx219)**                      |                   **[전현근](https://github.com/thisfetch1591)**                   |                   **[최이주](https://github.com/cherryiJuice)**                    |
|:------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------:|
| <img width="120px" src="https://avatars.githubusercontent.com/u/52346113?v=4"> | <img width="120px" src="https://avatars.githubusercontent.com/u/144662707?v=4"> | <img width="120px" src="https://avatars.githubusercontent.com/u/143402486?v=4"> |
|                                 공연 및 채팅 기능 개발                                  |                                  가수 및 알림 기능 개발                                  |                                유저 기능 개발 및 인프라 구축                                |

<br>
