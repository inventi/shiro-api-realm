package io.inventi.shiro.api.audit.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.inventi.shiro.api.audit.service.AuditEventProducer;
import io.inventi.shiro.api.audit.service.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

public class AuditProducerConfiguration {
    @Bean
    public AuditEventProducer auditEventProducer(
            KafkaTemplate<String, String> producer,
            @Value("${audit.event-topic}") String topic,
            ObjectMapper objectMapper) {

        return new AuditEventProducer(producer, topic, objectMapper);
    }

    @Bean
    public SecurityUtils securityUtils() {
        return new SecurityUtils();
    }
}
