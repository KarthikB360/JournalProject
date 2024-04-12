package com.user.service;

import com.user.entity.EventType;
import com.user.entity.User;
import com.user.entity.UserEvent;
import com.user.exception.CustomBadRequestException;
import com.user.exception.CustomException;
import com.user.exception.CustomNotFoundException;
import com.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaEventPublisher kafkaEventPublisher;

    public User createUser(User user) throws CustomException {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(user.getRole().toUpperCase());
        User savedUser = userRepository.save(user);

        publishEvent(savedUser, EventType.CREATED);
        log.info("Created user {}", user);
        return savedUser;
    }

    public User getUserById(Long userId) throws CustomException {
        Optional<User> optionalUser = userRepository.findById(userId);
        log.info("Retrieved user with id {}", userId);
        return optionalUser.orElseThrow(() -> new CustomNotFoundException("User not found"));
    }

    public List<User> getAllUsers() throws CustomException {
        List<User> users = userRepository.findAll();
        if (CollectionUtils.isEmpty(users)) {
            log.error("Users list is empty");
            throw new CustomNotFoundException("Users not found");
        }
        log.info("Retrieved users");
        return users;
    }

    public User updateUser(User user) throws CustomException {
        User existingUser = getUserById(user.getId());
        if (!StringUtils.hasText(user.getEmail())) {
            throw new CustomBadRequestException("Invalid email");
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new CustomBadRequestException("Invalid password");
        }
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        User updatedUser = userRepository.save(existingUser);

        publishEvent(updatedUser, EventType.UPDATED);
        log.info("Updated user {}", updatedUser);
        return updatedUser;
    }

    public void deleteUser(Long userId) throws CustomException {
        User existingUser = getUserById(userId);
        userRepository.deleteById(userId);
        log.info("Deleted user {}", existingUser);
        publishEvent(existingUser, EventType.DELETED);
    }

    private void publishEvent(User user, EventType eventType) throws CustomException {
        log.info("Publishing event type {} for {}", eventType, user);
        UserEvent userEvent = UserEvent.builder().event(eventType.name()).userName(user.getUserName()).email(user.getEmail()).build();
        kafkaEventPublisher.sendEventsToTopic(userEvent);
    }
}
