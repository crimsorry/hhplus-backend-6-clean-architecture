package io.hhplus.tdd.interfaces.api;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lecture")
@RequiredArgsConstructor
public class LectureController {

    private static final Logger log = LoggerFactory.getLogger(LectureController.class);


}
