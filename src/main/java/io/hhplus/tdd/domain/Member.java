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
@Entity
@Comment("신청자")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("PK, autoIncrement")
    private Long memberId;

    @NotNull
    @Comment("신청자 명")
    @Column private String memberName;

}
