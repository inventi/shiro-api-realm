package io.inventi.shiro.api.audit.service;

import io.inventi.shiro.api.realm.domain.AuthInfo;

public class SecurityUtils {

    public AuthInfo getAuthInfo() {
        return (AuthInfo) org.apache.shiro.SecurityUtils.getSubject().getPrincipal();
    }
}
