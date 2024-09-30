package io.hhplus.tdd.lecture;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.Comment;

@Entity
@Comment("수강신청")
public class Lecture {

    @Id
    public Long id;
}
