package io.hhplus.tdd.interfaces.api.dto;

import java.time.LocalDate;

public record LectureItemRes(
        Long lectureId,
        Long lectureItemId,
        String lectureName,
        String teacherName,
        Integer remainCnt,
        LocalDate startDate,
        LocalDate openDate
) {

}
