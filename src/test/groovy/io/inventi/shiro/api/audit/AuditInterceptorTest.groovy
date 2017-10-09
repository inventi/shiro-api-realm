package io.inventi.shiro.api.audit

import io.inventi.shiro.api.audit.configuration.AuditAction
import io.inventi.shiro.api.audit.domain.AuditEvent
import io.inventi.shiro.api.audit.service.AuditEventProducer
import io.inventi.shiro.api.audit.service.AuditInterceptor
import io.inventi.shiro.api.audit.service.SecurityUtils
import io.inventi.shiro.api.realm.domain.AuthInfo
import org.junit.Test
import org.springframework.web.method.HandlerMethod

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import static org.mockito.Matchers.any
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.never
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

class AuditInterceptorTest {
    AuditEventProducer producer = mock(AuditEventProducer)
    SecurityUtils securityUtils = mock(SecurityUtils)
    AuditInterceptor interceptor = new AuditInterceptor(producer, securityUtils)
    HttpServletRequest request = mock(HttpServletRequest)
    HandlerMethod handlerMethod = mock(HandlerMethod)

    @Test
    void "does not audit when request does not have credentials header"() {
        when(request.getHeader("x-credential-username")).thenReturn(null)
        when(handlerMethod.getMethod()).thenReturn(this.getClass().getMethod("auditMethod"))

        interceptor.postHandle(request, mock(HttpServletResponse), handlerMethod, null)

        verify(producer, never()).send(any(AuditEvent))
    }

    @Test
    void "does not audit when method does not have correct annotation"() {
        when(request.getHeader("x-credential-username")).thenReturn("John")
        when(handlerMethod.getMethod()).thenReturn(this.getClass().getMethod("regularMethod"))

        interceptor.postHandle(request, mock(HttpServletResponse), handlerMethod, null)

        verify(producer, never()).send(any(AuditEvent))
    }

    @Test
    void "audits method when request has userName and method annotated with correct annotation"() {
        when(request.getHeader("x-credential-username")).thenReturn("John")
        when(securityUtils.getAuthInfo()).thenReturn(new AuthInfo("John", true))
        when(handlerMethod.getMethod()).thenReturn(this.getClass().getMethod("auditMethod"))

        interceptor.postHandle(request, mock(HttpServletResponse), handlerMethod, null)

        verify(producer).send(any(AuditEvent))
    }

    @AuditAction
    def auditMethod() {

    }

    def regularMethod() {

    }
}
