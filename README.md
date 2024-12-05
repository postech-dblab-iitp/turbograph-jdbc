## TurboGraph++를 위한 JDBC Driver
- TurboGraph++ DBMS 위해 설계 되었습니다.
- CUBRID JDBC 소스(JDBC 2.0 SPEC.)를 기반으로 개발 되었으며,
- 제공되는 Driver는 JDK 1.8을 준수 합니다.
- TurboGraph++ JDBC는 [Api-Broker](https://github.com/postech-dblab-iitp/api-broker) 를 통해 TurboGraph++와 연결됩니다.

## 소스로 부터 빌드하기
### 빌드 요구 사항

- ANT
- JDK 1.8

### 빌드 실행 방법
- Windows
  ```
  build.bat 
  ```
    
- Linux

  ```
  ./build.sh 
  ```

## 예제 소스
아래 Link에 예제를 통해 TurboGraph++위한 JDBC 사용방법을 확인 할 수 있습니다.

[TurboGraph++ JDBC Example](https://github.com/hwany7seo/turbograph-jdbc/blob/develop/src/jdbc/turbograph/jdbc/test/TurboGraphTest.java)

## Maven 정보
저장소 : https://ftp.cubrid.org/iitp/maven

### Maven 빌드에 포함

```
<dependency>
    <groupId>turbograph</groupId>
    <artifactId>turbograph-jdbc</artifactId>
    <version>release</version>
</dependency>
```

## 라이센스
- BSD License

