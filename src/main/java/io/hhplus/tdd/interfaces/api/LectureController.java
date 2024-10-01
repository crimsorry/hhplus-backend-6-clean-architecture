package io.hhplus.tdd.interfaces.api;

import io.hhplus.tdd.application.service.LectureService;
import io.hhplus.tdd.domain.LectureHistory;
import io.hhplus.tdd.interfaces.api.dto.LectureItemResponseDto;
import io.hhplus.tdd.interfaces.api.dto.UserLectureHistoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lecture")
@RequiredArgsConstructor
public class LectureController {

    private static final Logger log = LoggerFactory.getLogger(LectureController.class);
    private final LectureService lectureService;

    // 특강 신청 가능 목록 조회 api - 날짜별
    @GetMapping("apply")
    public ResponseEntity<?> getApplyLectures(){
        Map<LocalDate, List<LectureItemResponseDto>> restResponse = lectureService.applyLectures();
        return new ResponseEntity<>(restResponse, HttpStatus.OK);
    }

    // 특강 신청 api
    @PatchMapping("apply")
    public ResponseEntity<?> applyLecture(
            @RequestParam(required = true, defaultValue = "1") long memberId,
            @RequestParam(required = true, defaultValue = "1") long itemId
    ){
        LectureHistory restResponse = lectureService.lectureApply(memberId, itemId);
        return new ResponseEntity<>(restResponse, HttpStatus.OK);
    }

    // 사용자별 - 특강 신청 완료 목록 api
    @GetMapping("{id}/history")
    public ResponseEntity<?> getUserLectureHistory(
            @PathVariable long id
    ){
        List<UserLectureHistoryResponseDto> restResponse = lectureService.applyCourses(id);
        return new ResponseEntity<>(restResponse, HttpStatus.OK);
    }




}
