package io.inventi.shiro.api.realm.service;

import io.inventi.shiro.api.realm.domain.AuthInfo;
import io.inventi.shiro.api.realm.domain.UserToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class ApiRealm extends AuthorizingRealm {

    private final SimpleAccount noAuthAccount;
    private UserService service;

    public ApiRealm(UserService service) {
        this.service = service;
        this.noAuthAccount = createNoAuthAccount();
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        AuthInfo authInfo = (AuthInfo) principals.getPrimaryPrincipal();
        if (authInfo.isPreAuth) {
            return getUser(authInfo);
        }
        return noAuthAccount;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        AuthInfo authInfo = ((UserToken) token).getPrincipal();
        if (authInfo.isPreAuth) {
            return getUser(authInfo);
        }
        return noAuthAccount;
    }

    private SimpleAccount getUser(AuthInfo authInfo) {
        SimpleAccount account = new SimpleAccount(authInfo, null, getName());
        account.addStringPermissions(service.getUser(authInfo.userId).permissions);
        return account;
    }

    private SimpleAccount createNoAuthAccount() {
        SimpleAccount account = new SimpleAccount(new AuthInfo("", false), null, getName());
        account.addObjectPermission(new WildcardPermission("*"));
        return account;
    }

}
