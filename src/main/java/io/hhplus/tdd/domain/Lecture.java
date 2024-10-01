package io.hhplus.tdd.domain;

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
@Comment("수강신청")
public class Lecture {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("PK, autoIncrement")
    private Long lectureId;

    @NotNull
    @Comment("강의명")
    @Column private String lectureName;

    @NotNull
    @Comment("코치명")
    @Column private String teacherName;

    @NotNull
    @Comment("수강인원")
    @Column private Integer capacity;

}
