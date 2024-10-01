package io.hhplus.tdd.application;

import io.hhplus.tdd.application.service.LectureService;
import io.hhplus.tdd.domain.Lecture;
import io.hhplus.tdd.domain.LectureItem;
import io.hhplus.tdd.domain.Member;
import io.hhplus.tdd.infrastructure.repository.LectureHistoryRepository;
import io.hhplus.tdd.infrastructure.repository.LectureItemRepository;
import io.hhplus.tdd.infrastructure.repository.LectureRepository;
import io.hhplus.tdd.infrastructure.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class LectureServiceIntegrationTest {

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

    @Test
    public void 동시성_특강신청_40명이_30명_강의_신청() throws InterruptedException {
        // given
        int itemId = 1;
        int capacity = 30;
        int totalTasks = 40;
        AtomicInteger failCnt = new AtomicInteger();
        for(long i=1; i<=totalTasks; i++){
            memberRepository.save(new Member(i, "김소리" + i));
        }
        memberRepository.flush();
        Lecture lecture = new Lecture(1L, "JAVA 프로그래밍", "허재", capacity);
        lectureRepository.save(lecture);
        lectureRepository.flush();
        lectureItemRepository.save(new LectureItem(1L, capacity, LocalDate.now(), LocalDate.now().plusDays(3), LocalDate.now().plusDays(10), lecture));
        lectureItemRepository.flush();

        CountDownLatch latch = new CountDownLatch(totalTasks);
        ExecutorService executorService = Executors.newFixedThreadPool(totalTasks);

        // when
        for(long i=1; i<=totalTasks; i++){
            final long memberId = i;
            executorService.execute(() -> {
                try {
                    lectureService.lectureApply(memberId, itemId);
                } catch(Exception e){
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
        long memberId = 1L;
        int itemId = 1;
        int capacity = 30;
        int totalTasks = 5;
        AtomicInteger failCnt = new AtomicInteger();
        memberRepository.save(new Member(memberId, "김소리"));
        memberRepository.flush();
        Lecture lecture = new Lecture(1L, "JAVA 프로그래밍", "허재", capacity);
        lectureRepository.save(lecture);
        lectureRepository.flush();
        lectureItemRepository.save(new LectureItem(1L, capacity, LocalDate.now(), LocalDate.now().plusDays(3), LocalDate.now().plusDays(10), lecture));
        lectureItemRepository.flush();

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
