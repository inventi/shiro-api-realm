package io.inventi.shiro.api.audit.service;

import io.inventi.shiro.api.audit.domain.AuditEvent;
import io.inventi.shiro.api.realm.domain.AuthInfo;
import io.inventi.shiro.api.realm.service.PreAuthFilter;
import org.springframework.web.method.HandlerMethod;
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

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Method method = getHandlerMethod(handler);
        if (requestMadeByUser(request)) {
            producer.send(new AuditEvent(createUser(request), createAction(method, request, response)));
        }
    }

    private AuditEvent.Action createAction(Method method, HttpServletRequest request, HttpServletResponse response) {
        AuditEvent.Action action = new AuditEvent.Action();
        action.name = capitalize(method.getName());
        action.server= String.format("%s:%s", request.getServerName(), request.getServerPort());
        action.uri = request.getRequestURI();
        action.query = request.getQueryString();
        action.method = request.getMethod();
        action.status = response.getStatus();

        return action;
    }

    private String capitalize(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
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

    private Method getHandlerMethod(Object handler) {
        HandlerMethod methodHandler = (HandlerMethod)handler;
        return methodHandler.getMethod();
    }
}

