package io.hhplus.tdd.domain;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.Comment;

@Entity
@Comment("강의 수강 내역")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("PK, autoIncrement")
    private Long courseId;

    @NotNull
    @Comment("신청자")
    // 참조 무결성 보장. 
    // case1. DeadLock 발생 경우?
    //  > 일관성 유지 + 트랜잭션 크기 최소화. 낙관적 락 이용? > 충돌 시 롤백.
    // case2. N + 1 발생 경우?
    // > feach join 이용 예정
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "userId", referencedColumnName = "userId", nullable=false)
    private User user;

    @NotNull
    @Comment("강의")
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "lectureId", referencedColumnName = "lectureId", nullable=false)
    private Lecture lecture;

    @Comment("신청 성공/실패 여부")
    private Boolean isApply;

}
