package com.sourcing.sourcing;

import com.sourcing.sourcing.event.UserAggregate;
import com.sourcing.sourcing.event.consumer.EventConsumer;
import com.sourcing.sourcing.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConcurrencyTest implements CommandLineRunner {
    @Autowired
    private UserService userService;
    @Autowired
    private EventConsumer eventConsumer;

    @Override
    public void run(String... args) throws Exception {
        String userId = "user123";
        userService.createUser(userId, "initialUser");

        Runnable task = () -> {
            for (int i = 0; i < 30; i++) {
                userService.updateUser(userId, "user" + i);
            }
        };

        int threadCount = 4; // 동시성을 테스트할 스레드 수
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(task);
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        // 최종 사용자 상태 확인
        UserAggregate userAggregate = eventConsumer.getUserAggregate(userId);
        System.out.println("Final username: " + userAggregate.getUsername());
    }
}
