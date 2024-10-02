package io.hhplus.tdd.interfaces.api;

import io.hhplus.tdd.application.dto.LectureItemDto;
import io.hhplus.tdd.application.dto.UserLectureDoneDto;
import io.hhplus.tdd.application.dto.UserLectureHistoryDto;
import io.hhplus.tdd.application.service.LectureService;
import io.hhplus.tdd.domain.Lecture;
import io.hhplus.tdd.domain.LectureHistory;
import io.hhplus.tdd.interfaces.api.dto.LectureItemRes;
import io.hhplus.tdd.interfaces.api.dto.UserLectureDoneRes;
import io.hhplus.tdd.interfaces.api.dto.UserLectureHistoryRes;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lecture")
@RequiredArgsConstructor
public class LectureController {

    private static final Logger log = LoggerFactory.getLogger(LectureController.class);
    private final LectureService lectureService;

    // 특강 신청 가능 목록 조회 api - 날짜별
    @GetMapping("apply")
    public ResponseEntity<?> getApplyLectures(){
        List<LectureItemDto> lectureItemDtos = lectureService.applyLectures();
        List<LectureItemRes> restResponse = lectureItemDtos.stream()
                .map(dto -> new LectureItemRes(
                        dto.lectureId(),
                        dto.lectureItemId(),
                        dto.lectureName(),
                        dto.teacherName(),
                        dto.remainCnt(),
                        dto.startDate(),
                        dto.openDate()
                ))
                .toList();
        return new ResponseEntity<>(restResponse, HttpStatus.OK);
    }

    // 특강 신청 api
    @PatchMapping("apply")
    public ResponseEntity<?> applyLecture(
            @RequestParam(required = true, defaultValue = "1") long memberId,
            @RequestParam(required = true, defaultValue = "1") long itemId
    ){
        UserLectureDoneDto dto = lectureService.lectureApply(memberId, itemId);
        UserLectureDoneRes restResponse = new UserLectureDoneRes(
                            dto.courseId(),
                            dto.memberId(),
                            dto.memberName(),
                            dto.lectureId(),
                            dto.lectureName(),
                            dto.teacherName(),
                            dto.itemId(),
                            dto.startDate(),
                            dto.endDate(),
                            dto.openDate()
                    );
        return new ResponseEntity<>(restResponse, HttpStatus.OK);
    }

    // 사용자별 - 특강 신청 완료 목록 api
    @GetMapping("{id}/history")
    public ResponseEntity<?> getUserLectureHistory(
            @PathVariable long id
    ){
        List<UserLectureHistoryDto> lectureHistoryDtos = lectureService.applyCourses(id);
        List<UserLectureHistoryRes> restResponse = lectureHistoryDtos.stream()
                .map(dto -> new UserLectureHistoryRes(
                        dto.lectureId(),
                        dto.lectureItemId(),
                        dto.lectureName(),
                        dto.teacherName(),
                        dto.remainCnt(),
                        dto.startDate(),
                        dto.endDate(),
                        dto.openDate()
                ))
                .toList();
        return new ResponseEntity<>(restResponse, HttpStatus.OK);
    }




}
