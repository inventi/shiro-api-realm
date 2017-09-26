package io.inventi.shiro.api.realm.service;

import io.inventi.shiro.api.realm.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private RestTemplate userApiTemplate;

    public UserServiceImpl(String rootEndpoint, RestTemplateBuilder restTemplateBuilder) {

        userApiTemplate = restTemplateBuilder
                .rootUri(rootEndpoint)
                .build();
    }

    public User getUser(String userId) {
        return findUser(userId).orElseGet(() -> createUser(userId).get());
    }

    @Cacheable(value = "userCache", key = "#userId")
    public Optional<User> findUser(String userId) throws HttpClientErrorException {
        try {
            return Optional.of(userApiTemplate.getForObject("/v1/users/{userId}",
                    User.class,
                    userId));
        } catch (HttpClientErrorException ex) {
            logger.debug("Unable to find User with id of {}", userId);
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Optional.empty();
            }
            throw ex;
        }
    }

    @CacheEvict(value = "userCache", key = "#userId")
    public Optional<User> createUser(String userId) throws HttpClientErrorException {
        try {
            return Optional.of(userApiTemplate.postForObject("/v1/users/",
                    createUserRequest(userId),
                    User.class));
        } catch (HttpClientErrorException ex) {
            logger.debug("Unable to create User with id of {}, ex {}", userId, ex);
            if (ex.getStatusCode() == HttpStatus.CONFLICT) {
                return findUser(userId);
            }
            throw ex;
        }

    }

    private Map<String, String> createUserRequest(String userId) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("userId", userId);
        return requestMap;
    }

}
