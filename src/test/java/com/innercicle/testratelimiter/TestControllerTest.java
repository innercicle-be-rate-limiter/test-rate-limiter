package com.innercicle.testratelimiter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class TestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("")
    void test () {
        // given
        String test = "1234";
        // when

        int count = 15;
        CountDownLatch latch = new CountDownLatch(count);
        try {
            ExecutorService service = Executors.newFixedThreadPool(count);
            for (int i = 0; i < count; i++) {
                service.submit(() -> {
                    try {
                        TestController.RequestParameter parameter = new TestController.RequestParameter();
                        parameter.setTest("1234");
                        mockMvc.perform(get("/test")
                                            .param("test", test)
                        ).andDo(print());
                    } catch (Exception e) {
                        // e.printStackTrace();
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // then

    }

}