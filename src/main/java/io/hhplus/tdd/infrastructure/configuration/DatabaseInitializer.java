package io.hhplus.tdd.infrastructure.configuration;

import io.hhplus.tdd.domain.Lecture;
import io.hhplus.tdd.domain.LectureItem;
import io.hhplus.tdd.domain.Member;
import io.hhplus.tdd.infrastructure.repository.LectureItemRepository;
import io.hhplus.tdd.infrastructure.repository.LectureRepository;
import io.hhplus.tdd.infrastructure.repository.MemberRepository;
import io.hhplus.tdd.interfaces.api.util.DateUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer {

    private final MemberRepository memberRepository;
    private final LectureRepository lectureRepository;
    private final LectureItemRepository lectureItemRepository;
    private final DateUtils dateUtils;
    
    @PostConstruct
    public void init() {
        int capacity1 = 30;
        int capacity2 = 20;
        Lecture lecture1 = new Lecture(1L, "JAVA 프로그래밍", "허재", capacity1);
        Lecture lecture2 = new Lecture(2L, "MSA 마스터", "이석범", capacity2);
        memberRepository.save(new Member(1L, "김소리"));
        memberRepository.save(new Member(2L, "이현재"));
        lectureRepository.save(lecture1);
        lectureRepository.save(lecture2);
        lectureItemRepository.save(new LectureItem(1L, capacity1, dateUtils.StringTolocalDate("2024-09-28"), dateUtils.StringTolocalDate("2024-09-29"), dateUtils.StringTolocalDate("2024-10-28"), lecture1));
        lectureItemRepository.save(new LectureItem(2L, capacity1, dateUtils.StringTolocalDate("2024-10-01"), dateUtils.StringTolocalDate("2024-10-02"), dateUtils.StringTolocalDate("2024-10-29"), lecture1));
        lectureItemRepository.save(new LectureItem(3L, capacity1, dateUtils.StringTolocalDate("2024-10-01"), dateUtils.StringTolocalDate("2024-10-04"), dateUtils.StringTolocalDate("2024-10-30"), lecture1));
        lectureItemRepository.save(new LectureItem(4L, capacity2, dateUtils.StringTolocalDate("2024-09-29"), dateUtils.StringTolocalDate("2024-10-03"), dateUtils.StringTolocalDate("2024-10-30"), lecture2));
        lectureItemRepository.save(new LectureItem(5L, capacity2, dateUtils.StringTolocalDate("2024-09-29"), dateUtils.StringTolocalDate("2024-10-03"), dateUtils.StringTolocalDate("2024-10-31"), lecture2));
    }

}
