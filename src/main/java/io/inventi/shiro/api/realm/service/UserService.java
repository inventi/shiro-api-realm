package io.inventi.shiro.api.realm.service;

import io.inventi.shiro.api.realm.domain.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findUser(String userId);

}
