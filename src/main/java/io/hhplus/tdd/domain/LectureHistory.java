package io.hhplus.tdd.domain;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.Comment;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Comment("강의 수강 내역")
public class LectureHistory {

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", referencedColumnName = "memberId", nullable=false)
    private Member member;

    @NotNull
    @Comment("수강신청 세부 일정")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemId", referencedColumnName = "itemId", nullable=false)
    private LectureItem lectureItem;

    @Comment("신청 성공/실패 여부")
    private Boolean isApply;

}
