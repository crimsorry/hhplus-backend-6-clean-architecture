package io.hhplus.tdd.domain;

import jakarta.persistence.*;
import lombok.Builder;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Builder
@Entity
@Comment("수강신청")
public class Lecture {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("PK, autoIncrement")
    private Long lectureId;

    @NotNull
    @Comment("수강명")
    @Column private String lectureName;

    @NotNull
    @Comment("코치명")
    @Column private String teacherName;

    @NotNull
    @Comment("수강 인원")
    @ColumnDefault("0")
    @Column private Integer lectureCnt;

    @NotNull
    @Comment("수강 신청일")
    @Column private LocalDateTime startDateTime;

    @NotNull
    @Comment("수강 마감일")
    @Column private LocalDateTime endDateTime;

}
