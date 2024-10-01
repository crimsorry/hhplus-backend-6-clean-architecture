package io.hhplus.tdd.interfaces.api.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateUtils {

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public LocalDate StringTolocalDate(String dateTimeString){
        return LocalDate.parse(dateTimeString, dateTimeFormatter);
    }

}
