package io.inventi.shiro.api.realm.domain;

public class AuthInfo {

    public final String userId;

    public final boolean isPreAuth;

    public AuthInfo(String userId, boolean isPreAuth) {
        this.userId = userId;
        this.isPreAuth = isPreAuth;
    }
}
