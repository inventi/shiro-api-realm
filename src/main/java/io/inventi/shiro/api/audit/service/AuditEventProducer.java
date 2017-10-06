package io.inventi.shiro.api.audit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.inventi.shiro.api.audit.domain.AuditEvent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuditEventProducer {
    private static final Logger logger = LoggerFactory.getLogger(AuditEventProducer.class);

    private KafkaProducer<String, String> producer;
    private final String auditTopic;
    private ObjectMapper mapper;

    public AuditEventProducer(KafkaProducer<String, String> producer, String auditTopic, ObjectMapper mapper) {
        this.producer = producer;
        this.auditTopic = auditTopic;
        this.mapper = mapper;
    }

    public void send(AuditEvent event) {
        try {
            producer.send(new ProducerRecord<>(auditTopic, mapper.writeValueAsString(event)));
        } catch (JsonProcessingException e) {
            logger.error("Could not serialize audit event", e);
        }
    }
}
