package io.inventi.shiro.api.audit.configuration;

import io.inventi.shiro.api.audit.service.AuditEventProducer;
import io.inventi.shiro.api.audit.service.AuditInterceptor;
import io.inventi.shiro.api.audit.service.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

public class AuditConfiguration extends WebMvcConfigurerAdapter{
    @Autowired
    private AuditEventProducer producer;

    @Autowired
    private SecurityUtils securityUtils;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuditInterceptor(producer, securityUtils));
    }
}
