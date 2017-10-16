package io.inventi.shiro.api.audit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.inventi.shiro.api.audit.domain.AuditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

public class AuditEventProducer {
    private static final Logger logger = LoggerFactory.getLogger(AuditEventProducer.class);

    private KafkaTemplate<String, String> producer;
    private final String auditTopic;
    private ObjectMapper mapper;

    public AuditEventProducer(KafkaTemplate<String, String> producer, String auditTopic, ObjectMapper mapper) {
        this.producer = producer;
        this.auditTopic = auditTopic;
        this.mapper = mapper;
    }

    public void send(AuditEvent event) {
        try {
            producer.send(auditTopic, mapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            logger.error("Could not serialize audit event", e);
        }
    }
}
