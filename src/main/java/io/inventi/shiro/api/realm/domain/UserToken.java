package io.inventi.shiro.api.realm.domain;

import org.apache.shiro.authc.AuthenticationToken;

public class UserToken implements AuthenticationToken {

    private final AuthInfo authInfo;

    public UserToken(String username, boolean isPreAuth) {
        this.authInfo = new AuthInfo(username, isPreAuth);
    }

    @Override
    public AuthInfo getPrincipal() {
        return authInfo;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

}
