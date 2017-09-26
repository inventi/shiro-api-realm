package io.inventi.shiro.api.realm.service;

import io.inventi.shiro.api.realm.domain.User;

public interface UserService {

    User getUser(String userId);

}
