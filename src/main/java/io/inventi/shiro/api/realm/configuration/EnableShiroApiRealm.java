package io.inventi.shiro.api.realm.configuration;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = { java.lang.annotation.ElementType.TYPE })
@Import({CachingConfig.class, ShiroConfiguration.class})
public @interface EnableShiroApiRealm {
}
