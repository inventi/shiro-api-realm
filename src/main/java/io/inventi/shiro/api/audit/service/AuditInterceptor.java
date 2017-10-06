package io.inventi.shiro.api.audit.service;

import io.inventi.shiro.api.audit.configuration.AuditAction;
import io.inventi.shiro.api.audit.domain.AuditEvent;
import io.inventi.shiro.api.realm.domain.AuthInfo;
import io.inventi.shiro.api.realm.service.PreAuthFilter;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AuditInterceptor extends HandlerInterceptorAdapter {

    private AuditEventProducer producer;
    private SecurityUtils securityUtils;

    public AuditInterceptor(AuditEventProducer producer, SecurityUtils securityUtils) {
        this.producer = producer;
        this.securityUtils = securityUtils;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Method method = getHandlerMethod(handler);
        if (requestMadeByUser(request) && auditRequest(method)) {
            producer.send(new AuditEvent(createUser(request), createAction(request, response)));
        }
    }

    private AuditEvent.Action createAction(HttpServletRequest request, HttpServletResponse response) {
        return new AuditEvent.Action(request.getRequestURI(), request.getMethod(), response.getStatus());
    }

    private AuditEvent.User createUser(HttpServletRequest request) {
        AuthInfo authInfo = securityUtils.getAuthInfo();
        return new AuditEvent.User(
                authInfo.userId,
                request.getHeader("User-Agent"),
                request.getHeader("X-Forwarded-For"));
    }

    private boolean requestMadeByUser(HttpServletRequest request) {
        return request.getHeader(PreAuthFilter.USERNAME_HEADER) != null;
    }

    private boolean auditRequest(Method method) {
        return method.isAnnotationPresent(RequiresPermissions.class) || method.isAnnotationPresent(AuditAction.class);
    }

    private Method getHandlerMethod(Object handler) {
        HandlerMethod methodHandler = (HandlerMethod)handler;
        return methodHandler.getMethod();
    }
}

