package io.hhplus.tdd.infrastructure.repository;

import io.hhplus.tdd.domain.LectureHistory;
import io.hhplus.tdd.domain.Lecture;
import io.hhplus.tdd.domain.LectureItem;
import io.hhplus.tdd.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureHistoryRepository extends JpaRepository<LectureHistory, Long>{

    Optional<LectureHistory> findByMemberAndLectureItem_LectureAndIsApply(Member member, Lecture lecture, boolean isApply);
    List<LectureHistory> findByMemberAndIsApply(Member member, Boolean isApply);

}
