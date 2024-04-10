package com.user.service;

import com.user.entity.EventType;
import com.user.entity.User;
import com.user.entity.UserEvent;
import com.user.exception.CustomException;
import com.user.exception.CustomNotFoundException;
import com.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
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
        String userName = new StringBuilder().append(user.getFirstName()).append(user.getLastName()).toString();
        UserEvent userEvent = UserEvent.builder().event(eventType.name()).userName(userName).email(user.getEmail()).build();
        kafkaEventPublisher.sendEventsToTopic(userEvent);
    }
}
