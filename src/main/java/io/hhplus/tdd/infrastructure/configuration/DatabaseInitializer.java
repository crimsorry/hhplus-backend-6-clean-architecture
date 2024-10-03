package io.hhplus.tdd.infrastructure.configuration;

import io.hhplus.tdd.domain.Lecture;
import io.hhplus.tdd.domain.LectureItem;
import io.hhplus.tdd.domain.Member;
import io.hhplus.tdd.infrastructure.repository.LectureItemRepository;
import io.hhplus.tdd.infrastructure.repository.LectureRepository;
import io.hhplus.tdd.infrastructure.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer {

    private final MemberRepository memberRepository;
    private final LectureRepository lectureRepository;
    private final LectureItemRepository lectureItemRepository;
    
    @PostConstruct
    public void init() {
        int capacity1 = 30;
        int capacity2 = 20;
        if(memberRepository.findByMemberId(1L).isPresent()) return;
        Lecture lecture1 = new Lecture(1L, "JAVA 프로그래밍", "이석범", capacity1);
        Lecture lecture2 = new Lecture(2L, "MSA 마스터", "이석범", capacity2);
        memberRepository.save(new Member(1L, "김소리"));
        memberRepository.save(new Member(2L, "이현재"));
        lectureRepository.save(lecture1);
        lectureRepository.save(lecture2);
        lectureItemRepository.save(new LectureItem(1L, capacity1, LocalDate.now().minusDays(2), LocalDate.now().plusDays(3), LocalDate.now().plusDays(20), lecture1));
        lectureItemRepository.save(new LectureItem(2L, capacity1, LocalDate.now().minusDays(1), LocalDate.now().plusDays(4), LocalDate.now().plusDays(18), lecture1));
        lectureItemRepository.save(new LectureItem(3L, capacity1, LocalDate.now(), LocalDate.now().plusDays(3), LocalDate.now().plusDays(14), lecture1));
        lectureItemRepository.save(new LectureItem(4L, capacity2, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), LocalDate.now().plusDays(13), lecture2));
        lectureItemRepository.save(new LectureItem(5L, capacity2, LocalDate.now(), LocalDate.now().plusDays(2), LocalDate.now().plusDays(14), lecture2));
    }

}
