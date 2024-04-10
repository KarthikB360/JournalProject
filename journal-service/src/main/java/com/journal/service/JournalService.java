package com.journal.service;

import com.journal.entity.Journal;
import com.journal.exception.CustomException;
import com.journal.exception.CustomNotFoundException;
import com.journal.repository.JournalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
public class JournalService {

    @Autowired
    private JournalRepository journalRepository;

    public List<Journal> getAllJournals() throws CustomException {
        List<Journal> journals = journalRepository.findAll();
        if (CollectionUtils.isEmpty(journals)) {
            log.error("Journals not found");
            throw new CustomNotFoundException("Journals not found");
        }
        log.info("Journals found");
        return journals;
    }

    public Journal saveJournal(Journal journal) {
        Journal savedJournal = journalRepository.save(journal);
        log.info("Journal saved {}", journal);
        return savedJournal;
    }
}
