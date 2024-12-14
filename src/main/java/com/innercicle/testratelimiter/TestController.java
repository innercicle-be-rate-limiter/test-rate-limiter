package com.innercicle.testratelimiter;

import com.innercicle.annotations.RateLimiting;
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

    @GetMapping("/test")
    @RateLimiting(name="test", cacheKey = "#parameter.getTest()")
    public void test(RequestParameter parameter) {
        log.error("test {}", parameter.getTest());
    }

    @ToString
    @Setter @Getter
    public static class RequestParameter {
        private String test;
    }

}
