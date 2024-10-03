package io.hhplus.tdd.application;

import io.hhplus.tdd.application.service.LectureService;
import io.hhplus.tdd.domain.Lecture;
import io.hhplus.tdd.domain.LectureItem;
import io.hhplus.tdd.domain.Member;
import io.hhplus.tdd.infrastracture.DatabaseCleaner;
import io.hhplus.tdd.infrastructure.repository.LectureHistoryRepository;
import io.hhplus.tdd.infrastructure.repository.LectureItemRepository;
import io.hhplus.tdd.infrastructure.repository.LectureRepository;
import io.hhplus.tdd.infrastructure.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
* 트랙잭션 적용 시 save 안되는 현상 발견
* https://velog.io/@junho5336/%ED%85%8C%EC%8A%A4%ED%8A%B8%EB%B3%84%EB%A1%9C-DB-%EC%B4%88%EA%B8%B0%ED%99%94%ED%95%98%EA%B8%B0
*
 */
@SpringBootTest
public class LectureServiceIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private LectureService lectureService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private LectureItemRepository lectureItemRepository;

    @Autowired
    private LectureHistoryRepository lectureHistoryRepository;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    public void setUp() {
        databaseCleaner.clear();  // 각 테스트 전 DB 초기화
    }

    @Test
    public void 동시성_특강신청_40명이_30명_강의_신청() throws InterruptedException {
        // given
        int itemId = 11;
        int capacity = 30;
        int totalTasks = 40;
        AtomicInteger failCnt = new AtomicInteger();
        for(long i=1; i<=totalTasks; i++){
            memberRepository.save(new Member(i + 10, "김소리" + i));
        }
        Lecture lecture = new Lecture(11L, "JAVA 프로그래밍", "이석범", capacity);
        lectureRepository.save(lecture);
        lectureItemRepository.save(new LectureItem(11L, capacity, LocalDate.now(), LocalDate.now().plusDays(3), LocalDate.now().plusDays(10), lecture));

        CountDownLatch latch = new CountDownLatch(totalTasks);
        ExecutorService executorService = Executors.newFixedThreadPool(totalTasks);

        // when
        for(long i=1; i<=totalTasks; i++){
            final long memberId = i + 10;
            executorService.execute(() -> {
                try {
                    lectureService.lectureApply(memberId, itemId);
                } catch(Exception e){
                    System.out.println("e.getMessage(): " + e.getMessage());
                    if(e.getMessage().equals("이미 완료된 수강신청입니다.")) failCnt.getAndIncrement();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        assertEquals(totalTasks - capacity, failCnt.get());

        executorService.shutdown();
    }

    @Test
    public void 동일한_특강_같은유저_신청_불가() throws InterruptedException {
        // given
        long memberId = 71L;
        long itemId = 71L;
        long lectureId = 71L;
        int capacity = 30;
        int totalTasks = 5;
        AtomicInteger failCnt = new AtomicInteger();
        Member member = new Member(memberId, "김소리");
        memberRepository.save(member);
        Lecture lecture = new Lecture(lectureId, "JAVA 프로그래밍", "이석범", capacity);
        lectureRepository.save(lecture);
        LectureItem lectureItem = new LectureItem(itemId, capacity, LocalDate.now(), LocalDate.now().plusDays(3), LocalDate.now().plusDays(10), lecture);
        lectureItemRepository.save(lectureItem);

        CountDownLatch latch = new CountDownLatch(totalTasks);
        ExecutorService executorService = Executors.newFixedThreadPool(totalTasks);

        // when
        for(long i=1; i<=totalTasks; i++){
            executorService.execute(() -> {
                try {
                    lectureService.lectureApply(memberId, itemId);
                } catch(Exception e){
                    if(e.getMessage().equals("이미 신청 내역이 존재합니다.")) failCnt.getAndIncrement();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        assertEquals(totalTasks - 1, failCnt.get());

        executorService.shutdown();

    }

}
