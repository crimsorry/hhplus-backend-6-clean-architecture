package io.hhplus.tdd.interfaces.api.dto;

import java.time.LocalDate;

public record UserLectureHistoryRes(
        Long lectureId,
        Long lectureItemId,
        String lectureName,
        String teacherName,
        Integer remainCnt,
        LocalDate startDate,
        LocalDate endDate,
        LocalDate openDate
) {
}
