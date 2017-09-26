package io.inventi.shiro.api.realm.service;

import io.inventi.shiro.api.realm.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.client.RestClientException;
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

    public Optional<User> findUser(String userId) {
        Optional<User> user = getUser(userId);
        if (user.isPresent()) {
            return user;
        }
        return createUser(userId);
    }

    @Cacheable(value = "userCache", key = "#userId")
    public Optional<User> getUser(String userId) {
        try {
            return Optional.of(userApiTemplate.getForObject("/v1/users/{userId}",
                    User.class,
                    userId));
        } catch (RestClientException ex) {
            logger.debug("Unable to find User with id of {}", userId);
        }
        return Optional.empty();
    }

    @CacheEvict(value = "userCache", key = "#userId")
    public Optional<User> createUser(String userId) {
        try {
            return Optional.of(userApiTemplate.postForObject("/v1/users/",
                    createUserRequest(userId),
                    User.class));
        } catch (RestClientException ex) {
            logger.debug("Unable to create User with id of {}, ex {}", userId, ex);
            return getUser(userId);
        }

    }

    private Map<String, String> createUserRequest(String userId) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("userId", userId);
        return requestMap;
    }

}
