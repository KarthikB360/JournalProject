package com.journal.util;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

public class UserDeserializer implements Deserializer<com.journal.entity.UserEvent> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // No additional configuration needed
    }

    @Override
    public com.journal.entity.UserEvent deserialize(String topic, byte[] data) {
        if (data == null) {
            return null; // Return null if the data is null
        }
        try {
            // Attempt to deserialize as com.journal.entity.User
            return objectMapper.readValue(data, com.journal.entity.UserEvent.class);
        } catch (IOException e) {
            e.printStackTrace(); // Handle deserialization failure
        }
        return null; // Return null if deserialization fails
    }

    @Override
    public void close() {
        // No resources to close
    }
}
