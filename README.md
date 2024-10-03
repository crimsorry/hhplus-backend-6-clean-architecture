## 수강 신청 서비스 Table 설계 

### ⚡ 개요

* 수강 신청 서비스를 구축하기 위한 요구사항 분석부터 Table 설계 방안에 까지 다룹니다.

### ⚡ 요구사항 분석

- [x] **기능 구현**
  - [x] GET        /apply            특강 신청 가능 목록
  - [x] POST     /apply            특강 신청
  - [x] GET       /{id}/history   사용자 별 - 특강 신청 완료 목록

> **POST**: 수강신청 내역이 insert 되지만, 수강한 잔여좌석 수가 update 되야 하기 때문에 처음에는 **PATCH** 로 설계했습니다. 그러나 PATCH 는 기존 리소스의 일부 속성만 수정한다는 사실을 알게 되었습니다. '특강신청' 비지니스 로직은 업데이트 동작도 포함하지만, 새로운 시소스를 생성하는 기능 역시 존재하기 때문에 POST 메소드로 최종 선택했습니다.

- [x] **단위 테스트**
  - [x] @Mock 특강 신청 가능 목록
  - [x] @Mock 특강 신청
  - [x] @Mock 사용자 별 - 특강 신청 완료 목록
- [x] **통합 테스트**
  - [x] @SpringTest  동시성 - 40명이 30 좌석 강좌 수강 신청
  - [x] @SpringTest  동시성 - 1명의 수강생이 5번 동일한 강의 수강

### ⚡ 사용 Tool

* **DB**: MySQL
* **ERD**: DBeaver

### ⚡ ERD

#### <변경 전> 

![image-20241003233055104](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20241003233055104.png)

* **Member**
  * 강의를 수강 할 수 있는 **신청자 정보** 테이블 입니다.
* **Lecture**
  * **수강 신청 정보**를 확인 할 수 있는 테이블 입니다.
* **Lecture History**
  * 신청자가 **수강신청한 내역**을 확인 할 수 있는 테이블 입니다.

> 초기 설계 당시 '날짜별 신청 가능 특강' 이라는 항목의 의미를 날짜별로 동일한 특강은 단 하나만 존재한다고 판단했습니다. 그러나 **강의명, 코치명이 변경**될 경우와 **해당 강의에 대해 여러 시강의 할당되었을 경우에는 어떻게 해야 할까?** 라는 말을 듣고 아래와 같이 수강신청 테이블의 세부 테이블이 필요하다는 것을 알게 되었습니다. 
>
> 요구사항을 파악할때 요구사항을 그대로 파악하는 것 보다 그 이상으로 필요한 기능이 무엇일지 생각하는 **사용자 중심 설계**가 필요하다는 것을 알게 되었습니다.

#### <변경 후>

![image-20241003225726039](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20241003225726039.png)

* **Member**
  * 강의를 수강 할 수 있는 **신청자 정보** 테이블 입니다.
* **Lecture**
  * **수강 신청 정보**를 확인 할 수 있는 테이블 입니다.
* **Lecture Item**
  * **수강 신청 세부 일정 정보**를 확인 할 수 있는 테이블 입니다.
* **Lecture History**
  * 신청자가 **수강신청한 내역**을 확인 할 수 있는 테이블 입니다.


> 수강 세부 일정 테이블을 추가해 강의마다 신청일을 여러개 할당 할 수 있도록 수정 했습니다. 수강과 수강 세부 일정을 분리하는 방향이 기능이 추가되는 경우 대응하기 좋을 것 같다는 생각이 들었습니다.

### ⚡DDL

```mysql
-- DDL 정의

CREATE TABLE member (
    member_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '신청자 ID',
    member_name VARCHAR(13) NOT NULL COMMENT '신청자 명'
) COMMENT = '신청자';

CREATE TABLE lecture (
	lecture_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '수강신청 ID',
	lecture_name VARCHAR(13) NOT NULL COMMENT '강의명',
	teacher_name VARCHAR(13) NOT NULL COMMENT '코치명',
	capacity INT(2) NOT NULL COMMENT '수강인원'
) COMMENT = '수강신청';

CREATE TABLE lecture_item (
    item_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '세부일정 ID',
    remain_cnt INT(2) NOT NULL COMMENT '남은 신청인원',
    start_date DATE NOT NULL COMMENT '수강 신청일',
    end_date DATE NOT NULL COMMENT '수강 마감일',
    open_date DATE NOT NULL COMMENT '개강일',
    lecture_id BIGINT NOT NULL COMMENT '수강신청 ID',
    CONSTRAINT fk_lecture FOREIGN KEY (lecture_id) REFERENCES lecture(lecture_id)
) COMMENT = '수강신청 세부 일정';

CREATE TABLE lecture_history (
    history_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '신청자 ID',
    is_apply BOOLEAN NOT NULL COMMENT '신청 성공/실패 여부',
    member_id BIGINT NOT NULL COMMENT '신청자 ID',
    item_id BIGINT NOT NULL COMMENT '세부일정 ID',
    CONSTRAINT fk_member FOREIGN KEY (member_id) REFERENCES member(member_id),
    CONSTRAINT fk_item FOREIGN KEY (item_id) REFERENCES lecture_item(item_id)
) COMMENT = '강의 수강 내역';
```

### ⚡ 결론

* 프로젝트 요구사항과 테이블을 설계하면서 **요구사항 이상의 설계**를 하는 방법에 대해 알게 되었습니다. 
