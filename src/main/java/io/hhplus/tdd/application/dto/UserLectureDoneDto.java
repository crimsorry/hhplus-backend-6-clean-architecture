package io.hhplus.tdd.application.dto;

import java.time.LocalDate;

public record UserLectureDoneDto(
        Long courseId,
        Long memberId,
        String memberName,
        Long lectureId,
        String lectureName,
        String teacherName,
        Long itemId,
        LocalDate startDate,
        LocalDate endDate,
        LocalDate openDate
) {
}
