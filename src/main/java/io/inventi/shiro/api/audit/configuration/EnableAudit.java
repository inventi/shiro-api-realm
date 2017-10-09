package io.inventi.shiro.api.audit.configuration;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = { java.lang.annotation.ElementType.TYPE })
@Import({AuditConfiguration.class, AuditProducerConfiguration.class})
public @interface EnableAudit {
}
