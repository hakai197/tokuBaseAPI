package com.tokubase.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson configuration.
 *
 * Key concerns for this project:
 * - LocalDate / LocalDateTime must be serialised as ISO-8601 strings, not arrays.
 * - Lazy-loaded JPA collections: we use DTOs everywhere so entities are never
 *   serialised directly – this config acts as a safety net.
 * - FAIL_ON_EMPTY_BEANS disabled so detached / empty proxy objects don't blow up.
 */
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // Register the JSR-310 module so LocalDate/LocalDateTime work
        mapper.registerModule(new JavaTimeModule());

        // Write dates as ISO strings instead of numeric timestamps
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Don't throw when serialising an empty/proxy bean
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        return mapper;
    }
}

