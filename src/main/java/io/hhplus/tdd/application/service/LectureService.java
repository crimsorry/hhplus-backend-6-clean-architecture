package io.hhplus.tdd.application.service;

import io.hhplus.tdd.application.dto.LectureItemDto;
import io.hhplus.tdd.application.dto.UserLectureDoneDto;
import io.hhplus.tdd.application.dto.UserLectureHistoryDto;
import io.hhplus.tdd.domain.LectureHistory;
import io.hhplus.tdd.domain.LectureItem;
import io.hhplus.tdd.domain.Member;
import io.hhplus.tdd.infrastructure.repository.LectureHistoryRepository;
import io.hhplus.tdd.infrastructure.repository.LectureItemRepository;
import io.hhplus.tdd.infrastructure.repository.MemberRepository;
import io.hhplus.tdd.application.common.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final MemberRepository memberRepository;
    private final LectureItemRepository lectureItemRepository;
    private final LectureHistoryRepository lectureHistoryRepository;

    /* 신청 가능 특강 목록 */
    @Transactional(readOnly = true) // 조회용 메소드
    public List<LectureItemDto> applyLectures(){
        List<LectureItem> lectureItems = lectureItemRepository.findByRemainCntGreaterThanWithLecture();
        List<LectureItemDto> groupedLectures = new ArrayList<>();
        getLectureItemMap(lectureItems, groupedLectures);
        return groupedLectures;
    }

    /* 특강 신청 */
    @Transactional
    public UserLectureDoneDto lectureApply(long memberId, long itemId){
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(()
                -> new CustomException("존재하지 않는 유저입니다."));
        LectureItem lectureItem = lectureItemRepository.findByItemId(itemId).orElseThrow(()
                -> new CustomException("존재하지 않는 특강입니다."));
        Optional<LectureHistory> existingLecture = lectureHistoryRepository.findByMemberAndLectureItem_LectureAndIsApply(member, lectureItem.getLecture(), true);
        if (existingLecture.isPresent() &&!existingLecture.isEmpty() ) {
            throw new CustomException("이미 신청 내역이 존재합니다.");
        }
        if(lectureItem.getRemainCnt() <= 0){
            throw new CustomException("이미 완료된 수강신청입니다.");
        }
        lectureItem.setRemainCnt(lectureItem.getRemainCnt()-1);
        LectureHistory lectureHistory = LectureHistory.builder()
                .member(member)
                .lectureItem(lectureItem)
                .isApply(true)
                .build();
        lectureHistoryRepository.save(lectureHistory); // 수강내역 추가
        UserLectureDoneDto userLectureDoneDto = new UserLectureDoneDto(
                lectureHistory.getCourseId(),
                lectureHistory.getMember().getMemberId(),
                lectureHistory.getMember().getMemberName(),
                lectureHistory.getLectureItem().getLecture().getLectureId(),
                lectureHistory.getLectureItem().getLecture().getLectureName(),
                lectureHistory.getLectureItem().getLecture().getTeacherName(),
                lectureHistory.getLectureItem().getItemId(),
                lectureHistory.getLectureItem().getStartDate(),
                lectureHistory.getLectureItem().getEndDate(),
                lectureHistory.getLectureItem().getOpenDate()
        );
        return userLectureDoneDto;
    }

    /* 사용자별 - 특강 신청 완료 목록 */
    @Transactional(readOnly = true)
    public List<UserLectureHistoryDto> applyCourses(long memberId){
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(()
                -> new CustomException("존재하지 않는 유저입니다."));
        List<UserLectureHistoryDto> lectureItemResponseDtos = new ArrayList<>();
        getUserLectureHistoryList(member, lectureItemResponseDtos);
        return lectureItemResponseDtos;
    }

    public List<UserLectureHistoryDto> getUserLectureHistoryList(Member member, List<UserLectureHistoryDto> lectureItemResponseDtos){
        List<LectureHistory> lectureHistories = lectureHistoryRepository.findByMemberAndIsApply(member, true);
        for (LectureHistory lectureHistory : lectureHistories) {
            UserLectureHistoryDto dto = new UserLectureHistoryDto(
                    lectureHistory.getLectureItem().getLecture().getLectureId(),
                    lectureHistory.getLectureItem().getItemId(),
                    lectureHistory.getLectureItem().getLecture().getLectureName(),
                    lectureHistory.getLectureItem().getLecture().getTeacherName(),
                    lectureHistory.getLectureItem().getRemainCnt(),
                    lectureHistory.getLectureItem().getStartDate(),
                    lectureHistory.getLectureItem().getEndDate(),
                    lectureHistory.getLectureItem().getOpenDate()
            );
            lectureItemResponseDtos.add(dto);
        }
        return lectureItemResponseDtos;
    }

    public void getLectureItemMap(List<LectureItem> lectureItems, List<LectureItemDto> groupedLectures){
        for (LectureItem lectureItem : lectureItems) {
            LocalDate startDate = lectureItem.getStartDate();
            LocalDate endDate = lectureItem.getEndDate();

            // 시작일에서 마감일까지 모든 날짜 생성
            while (!startDate.isAfter(endDate)) {
                LectureItemDto dto = new LectureItemDto(
                        lectureItem.getLecture().getLectureId(),
                        lectureItem.getItemId(),
                        lectureItem.getLecture().getLectureName(),
                        lectureItem.getLecture().getTeacherName(),
                        lectureItem.getRemainCnt(),
                        startDate,
                        lectureItem.getOpenDate()
                );
                groupedLectures.add(dto);

                // 다음 날짜로 이동
                startDate = startDate.plusDays(1);
            }
        }
        groupedLectures.sort(Comparator.comparing(LectureItemDto::startDate)); // 정렬
    }


}
