package io.hhplus.tdd.domain;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.Comment;

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
