package io.inventi.shiro.api.audit.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface AuditAction {
}
