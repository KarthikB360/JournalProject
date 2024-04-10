package com.journal.service;

import com.journal.entity.Journal;
import com.journal.entity.UserEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class KafkaEventListener {

    @Autowired
    private JournalService journalService;

    @KafkaListener(topics = "user-events", groupId = "user-events-group")
    public void consumeEvents(UserEvent userEvent) {
        log.info("Consumer event {}", userEvent);

        String message = new StringBuilder().append("User ").append(userEvent.getUserName()).append(" ").append(userEvent.getEvent()).toString();
        Journal journal = Journal.builder().message(message).timestamp(LocalDateTime.now()).build();
        journalService.saveJournal(journal);
    }
}
