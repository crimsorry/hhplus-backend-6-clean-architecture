package io.hhplus.tdd.infrastructure.repository;

import io.hhplus.tdd.domain.Lecture;
import io.hhplus.tdd.domain.LectureItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureItemRepository extends JpaRepository<LectureItem, Long>{

    Optional<LectureItem> findByItemId(long itemId);
    
    /* 500에러 > fetch join N + 1 해결 */
    @Query("SELECT li FROM LectureItem li JOIN FETCH li.lecture WHERE li.remainCnt > 0")
    List<LectureItem> findByRemainCntGreaterThanWithLecture();

    @Modifying
    @Query("UPDATE LectureItem SET remainCnt = remainCnt - 1 WHERE itemId = :itemId")
    void updateItemId(long itemId);


}
