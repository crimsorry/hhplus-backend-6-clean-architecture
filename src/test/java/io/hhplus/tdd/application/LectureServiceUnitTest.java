package io.hhplus.tdd.application;

import io.hhplus.tdd.application.dto.LectureItemDto;
import io.hhplus.tdd.application.dto.UserLectureDoneDto;
import io.hhplus.tdd.application.dto.UserLectureHistoryDto;
import io.hhplus.tdd.application.service.LectureService;
import io.hhplus.tdd.domain.LectureHistory;
import io.hhplus.tdd.domain.Lecture;
import io.hhplus.tdd.domain.LectureItem;
import io.hhplus.tdd.domain.Member;
import io.hhplus.tdd.infrastructure.repository.LectureHistoryRepository;
import io.hhplus.tdd.infrastructure.repository.LectureItemRepository;
import io.hhplus.tdd.infrastructure.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LectureServiceUnitTest {

    @InjectMocks
    private LectureService lectureService;

    @Mock
    private LectureItemRepository lectureItemRepository;

    @Mock
    private LectureHistoryRepository lectureHistoryRepository;

    @Mock
    private MemberRepository memberRepository;

    @Test
    public void 신청_가능_특강_목록(){
        // given
        List<LectureItem> lectureItemList = new ArrayList<>();
        lectureItemList.add(new LectureItem(1L, 10, LocalDate.now(), LocalDate.now().plusDays(2), LocalDate.now().plusDays(10), new Lecture()));
        lectureItemList.add(new LectureItem(2L, 5, LocalDate.now(), LocalDate.now().plusDays(3), LocalDate.now().plusDays(11), new Lecture()));

        // when
        when(lectureItemRepository.findByRemainCntGreaterThanWithLecture()).thenReturn(lectureItemList);

        // then
        List<LectureItemDto> result = lectureService.applyLectures();
        assertEquals(7, result.size()); // 2024.10.01 ~ 2024.10.04 > 4일 간 map list
    }

    @Test
    public void 사용자_특강_신청(){
        // given
        Member member = new Member(1L, "김소리");
        LectureItem lectureItem = new LectureItem(1L, 10, LocalDate.now(), LocalDate.now().plusDays(2), LocalDate.now().plusDays(1), new Lecture());
        LectureHistory lectureHistory = LectureHistory.builder().member(member).lectureItem(lectureItem).isApply(true).build();

        // when
        when(memberRepository.findByMemberId(member.getMemberId())).thenReturn(Optional.of(member));
        when(lectureItemRepository.findByItemId(lectureItem.getItemId())).thenReturn(Optional.of(lectureItem));
        when(lectureHistoryRepository.findByMemberAndLectureItem_LectureAndIsApply(member, lectureItem.getLecture(), true)).thenReturn(Optional.empty());

        // then
        UserLectureDoneDto result = lectureService.lectureApply(member.getMemberId(), lectureItem.getItemId());
        assertEquals(lectureHistory.getMember().getMemberId(), result.memberId());
        assertEquals(lectureHistory.getLectureItem().getItemId(), result.itemId());
    }

    @Test
    public void 사용자_특강_신청_완료_목록(){
        // given
        Member member = new Member(1L, "김소리");
        LectureItem lectureItem = new LectureItem(1L, 10, LocalDate.now(), LocalDate.now().plusDays(3), LocalDate.now().plusDays(1), new Lecture());
        List<LectureHistory> lectureHistoryList = new ArrayList<>();
        lectureHistoryList.add(new LectureHistory(1L, member, lectureItem, true));

        // when
        when(memberRepository.findByMemberId(member.getMemberId())).thenReturn(Optional.of(member));
        when(lectureHistoryRepository.findByMemberAndIsApply(member, true)).thenReturn(lectureHistoryList);

        // then
        List<UserLectureHistoryDto> result = lectureService.applyHistorys(member.getMemberId());
        assertEquals(1, result.size());
        assertEquals(lectureItem.getRemainCnt(), result.get(0).remainCnt());
    }








}