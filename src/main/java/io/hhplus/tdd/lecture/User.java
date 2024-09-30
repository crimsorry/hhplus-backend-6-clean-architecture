package io.hhplus.tdd.lecture;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Comment("신청자")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("PK, autoIncrement")
    private Long userId;

    @NotNull
    @Comment("신청자 명")
    @Column private String userName;

}
