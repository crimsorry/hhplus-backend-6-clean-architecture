package io.hhplus.tdd.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Comment("수강신청 세부 일정")
public class LectureItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("PK, autoIncrement")
    private Long itemId;

    @NotNull
    @Comment("남은 신청인원")
    @Column private Integer remainCnt;

    @NotNull
    @Comment("수강 신청일")
    @Column private LocalDate startDate;

    @NotNull
    @Comment("수강 마감일")
    @Column private LocalDate endDate;

    @NotNull
    @Comment("개강일")
    @Column private LocalDate openDate;

    @NotNull
    /* 순환참조 에러 발생
    *    - DTO 사용 + @JsonIgnore 사용
    *    - @JsonIgnore: 해당 프로퍼티 null. 데이터에 포함 안됨. DTO 이용 필요.
    *    - 역직렬화 무시.. (개념정리 추가) */
    @JsonIgnore
    @Comment("강의")
    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩 방식
    @JoinColumn(name = "lectureId", referencedColumnName = "lectureId", nullable=false)
    private Lecture lecture;

}
