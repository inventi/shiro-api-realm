package io.inventi.shiro.api.audit.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.inventi.shiro.api.audit.service.AuditEventProducer;
import io.inventi.shiro.api.audit.service.SecurityUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.Properties;

public class AuditProducerConfiguration {
    @Bean
    public AuditEventProducer auditEventProducer(
            @Value("${audit.event-topic}") String topic,
            @Value("${audit.kafka-bootstrap-servers}") String server,
            ObjectMapper objectMapper) {

        Properties props = new Properties();
        props.put("bootstrap.servers", server);
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        return new AuditEventProducer(new KafkaProducer<>(props), topic, objectMapper);
    }

    @Bean
    public SecurityUtils securityUtils() {
        return new SecurityUtils();
    }
}
