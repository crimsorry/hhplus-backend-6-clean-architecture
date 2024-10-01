package io.hhplus.tdd.application.service;

import io.hhplus.tdd.domain.LectureHistory;
import io.hhplus.tdd.domain.Lecture;
import io.hhplus.tdd.domain.LectureItem;
import io.hhplus.tdd.domain.Member;
import io.hhplus.tdd.infrastructure.repository.LectureHistoryRepository;
import io.hhplus.tdd.infrastructure.repository.LectureItemRepository;
import io.hhplus.tdd.infrastructure.repository.LectureRepository;
import io.hhplus.tdd.infrastructure.repository.MemberRepository;
import io.hhplus.tdd.interfaces.api.common.CustomException;
import io.hhplus.tdd.interfaces.api.dto.LectureItemResponseDto;
import io.hhplus.tdd.interfaces.api.dto.UserLectureHistoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final MemberRepository memberRepository;
    private final LectureRepository lectureRepository;
    private final LectureItemRepository lectureItemRepository;
    private final LectureHistoryRepository lectureHistoryRepository;

    /* 신청 가능 특강 목록 */
    @Transactional(readOnly = true) // 조회용 메소드
    public Map<LocalDate, List<LectureItemResponseDto>> applyLectures(){
        List<LectureItem> lectureItems = lectureItemRepository.findByRemainCntGreaterThanWithLecture();
        // 날짜별로 그룹화
        Map<LocalDate, List<LectureItemResponseDto>> groupedLectures = new HashMap<>();
        getLectureItemMap(lectureItems, groupedLectures);
        return groupedLectures;
    }

    /* 특강 신청 */
    @Transactional
    public LectureHistory lectureApply(long memberId, long itemId){
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(()
                -> new CustomException("존재하지 않는 유저입니다."));
        LectureItem lectureItem = lectureItemRepository.findByItemId(itemId).orElseThrow(()
                -> new CustomException("존재하지 않는 특강입니다."));
        Optional<LectureHistory> existingLecture = lectureHistoryRepository.findByMemberAndLectureItemAndIsApply(member, lectureItem, true);
        if (existingLecture.isPresent() && !existingLecture.isEmpty()) {
            throw new CustomException("이미 신청 내역이 존재합니다.");
        }

        // TODO: 동시성 제어 추가 필요.
        if(lectureItem.getRemainCnt() <= 0){
            throw new CustomException("이미 완료된 수강신청입니다.");
        }
        lectureItemRepository.updateItemId(itemId); // 강의 좌석 -1
        LectureHistory lectureHistory = LectureHistory.builder()
                .member(member)
                .lectureItem(lectureItem)
                .isApply(true)
                .build();
        lectureHistoryRepository.save(lectureHistory); // 수강내역 추가
        return lectureHistory;
    }

    /* 사용자별 - 특강 신청 완료 목록 */
    @Transactional(readOnly = true)
    public List<UserLectureHistoryResponseDto> applyCourses(long memberId){
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(()
                -> new CustomException("존재하지 않는 유저입니다."));
        List<UserLectureHistoryResponseDto> lectureItemResponseDtos = new ArrayList<>();
        getUserLectureHistoryList(member, lectureItemResponseDtos);
        return lectureItemResponseDtos;
    }

    public List<UserLectureHistoryResponseDto> getUserLectureHistoryList(Member member, List<UserLectureHistoryResponseDto> lectureItemResponseDtos){
        List<LectureHistory> lectureHistories = lectureHistoryRepository.findByMemberAndIsApply(member, true);
        for (LectureHistory lectureHistory : lectureHistories) {
            UserLectureHistoryResponseDto dto = new UserLectureHistoryResponseDto(
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

    public Map<LocalDate, List<LectureItemResponseDto>> getLectureItemMap(List<LectureItem> lectureItems, Map<LocalDate, List<LectureItemResponseDto>> groupedLectures){
        for (LectureItem lectureItem : lectureItems) {
            LocalDate startDate = lectureItem.getStartDate();
            LocalDate endDate = lectureItem.getEndDate();

            // 시작일에서 마감일까지 모든 날짜 생성
            while (!startDate.isAfter(endDate)) {
                LectureItemResponseDto dto = new LectureItemResponseDto(
                        lectureItem.getLecture().getLectureId(),
                        lectureItem.getItemId(),
                        lectureItem.getLecture().getLectureName(),
                        lectureItem.getLecture().getTeacherName(),
                        lectureItem.getRemainCnt(),
                        lectureItem.getOpenDate()
                );

                // Map에 날짜별로 그룹화
                groupedLectures.computeIfAbsent(startDate, k -> new ArrayList<>()).add(dto);

                // 다음 날짜로 이동
                startDate = startDate.plusDays(1);
            }
        }
        return groupedLectures;
    }


}
