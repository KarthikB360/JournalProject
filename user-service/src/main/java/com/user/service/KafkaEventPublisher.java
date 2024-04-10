package com.user.service;

import com.user.entity.User;
import com.user.entity.UserEvent;
import com.user.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class KafkaEventPublisher {

    @Autowired
    private KafkaTemplate<String, Object> template;

    public void sendEventsToTopic(UserEvent userEvent) throws CustomException {
        try {
            CompletableFuture<SendResult<String, Object>> future = template.send("user-events", userEvent);
            future.whenComplete((result, exception) -> {
                if (exception == null) {
                    log.info("User event sent successfully : {}", userEvent);
                } else {
                    log.error("User event sent failed {} for user : {}", exception.getMessage(), userEvent);
                }
            });

        } catch (Exception ex) {
            throw new CustomException("Event publish failed");
        }
    }
}
