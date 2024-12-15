package com.innercicle.testratelimiter;

import com.innercicle.annotations.RateLimiting;
import com.innercicle.testratelimiter.car.service.TestService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping("/test")
    public void test(RequestParameter parameter) {
        testService.test(parameter);
    }

    @ToString
    @Setter @Getter
    public static class RequestParameter {
        private String test;
    }

}
