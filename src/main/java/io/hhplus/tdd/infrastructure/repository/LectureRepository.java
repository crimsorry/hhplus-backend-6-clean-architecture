package io.hhplus.tdd.infrastructure.repository;

import io.hhplus.tdd.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long>{

    Optional<Lecture> findByLectureId(long lectureId);
//    List<Lecture> findByRemainCnt(int remainCnt);
//    List<Lecture> findByRemainCntGreaterThan(int remainCnt);


}
