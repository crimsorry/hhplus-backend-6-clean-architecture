package io.hhplus.tdd.infrastructure.repository;

import io.hhplus.tdd.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long>{

}
