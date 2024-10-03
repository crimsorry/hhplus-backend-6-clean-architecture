package io.hhplus.tdd.interfaces.api.dto;

import java.time.LocalDate;

public record UserLectureDoneRes(
        Long historyId,
        Long memberId,
        String memberName,
        Long lectureId,
        String lectureName,
        String teacherName,
        Long itemId,
        LocalDate startDate,
        LocalDate endDate,
        LocalDate openDate
)  {
}
