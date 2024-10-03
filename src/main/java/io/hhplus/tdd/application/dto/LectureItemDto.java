package io.hhplus.tdd.application.dto;

import java.time.LocalDate;

public record LectureItemDto(
        Long lectureId,
        Long lectureItemId,
        String lectureName,
        String teacherName,
        Integer remainCnt,
        LocalDate startDate,
        LocalDate openDate
) {
}
