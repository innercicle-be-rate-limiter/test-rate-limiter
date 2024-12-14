package com.innercicle.testratelimiter.car.service;

import com.innercicle.domain.BucketProperties;
import com.innercicle.testratelimiter.car.controller.request.ParkingApplyRequest;
import com.innercicle.testratelimiter.car.entity.CarEntity;
import com.innercicle.testratelimiter.car.repository.ParkingRepository;
import com.innercicle.testratelimiter.container.RedisTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
    "rate-limiter.enabled=true",
    "rate-limiter.lock-type=redis_redisson",
    "rate-limiter.rate-type=fixed_window_counter",
    "rate-limiter.cache-type=REDIS",
    "token-bucket.fixed-window-counter.request-limit=10",
    "token-bucket.fixed-window-counter.window-size=10",
})
class ParkingServiceFixedWindowCounterTest extends RedisTestContainer {

    @Autowired
    private ParkingService parkingService;
    @Autowired
    private ParkingRepository parkingRepository;
    @Autowired
    private BucketProperties bucketProperties;

    @BeforeEach
    public void beforeEach() {
        parkingRepository.deleteAll();
    }

    @Test
    @DisplayName("주차권 저장 테스트")
    void lateLimitingTest() throws Exception {
        // given
        String carNo = "07로3725";
        ParkingApplyRequest parkingApplyRequest = new ParkingApplyRequest("seunggulee", carNo, "20240722", "10", "00");
        int threadCount = 100;
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        try (ExecutorService executor = Executors.newFixedThreadPool(threadCount)) {
            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    try {
                        parkingService.parking(parkingApplyRequest);
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();
        }
        // then
        List<CarEntity> allByCarNoIs = parkingRepository.findAllByCarNoIs(carNo);

        assertThat(allByCarNoIs).hasSize(bucketProperties.getCapacity());

    }

}