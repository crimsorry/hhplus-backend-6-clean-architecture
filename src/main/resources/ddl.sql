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