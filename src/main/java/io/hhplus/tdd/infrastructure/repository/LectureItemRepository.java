package io.hhplus.tdd.infrastructure.repository;

import io.hhplus.tdd.domain.LectureItem;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureItemRepository extends JpaRepository<LectureItem, Long>{

    // 비관적 락
    // PESSIMISTIC_WRITE: 다른 트랜잭션에서 읽기도 쓰기도 못함 (배타적 락)
    // PESSIMISTIC_READ: 반복 읽기만 하고 수정하지 않는 용도. 다른 트랜잭선에서 읽기 가능 (공유 장금)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<LectureItem> findByItemId(long itemId);
    
    /* N + 1 문제 > fetch join */
    @Query("SELECT li FROM LectureItem li JOIN FETCH li.lecture WHERE li.remainCnt > 0")
    List<LectureItem> findByRemainCntGreaterThanWithLecture();

    @Modifying
    @Query("UPDATE LectureItem SET remainCnt = remainCnt - 1 WHERE itemId = :itemId")
    void updateItemId(long itemId);


}
