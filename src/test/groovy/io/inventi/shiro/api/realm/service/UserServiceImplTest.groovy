package io.inventi.shiro.api.realm.service

import io.inventi.shiro.api.realm.domain.User
import org.junit.Before
import org.junit.Test
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

import static org.hamcrest.Matchers.hasEntry
import static org.hamcrest.core.AllOf.allOf
import static org.mockito.Matchers.any
import static org.mockito.Matchers.anyMap
import static org.mockito.Matchers.anyString
import static org.mockito.Matchers.argThat
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.times
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

class UserServiceImplTest {

    private RestTemplate restTemplate
    private UserServiceImpl userService

    @Before
    void setUp() {
        restTemplate = mock(RestTemplate)
        RestTemplateBuilder builder = mock(RestTemplateBuilder)
        when(builder.rootUri("MOCK_ROOT_URI")).thenReturn(builder)
        when(builder.build()).thenReturn(restTemplate)
        userService = new UserServiceImpl("MOCK_ROOT_URI", builder)
    }

    @Test
    void "finds user"() {
        User user = new User()
        when(restTemplate.getForObject(anyString(), any(Class), anyString()))
            .thenReturn(user)
        def result = userService.findUser("1")
        assert user == result.get()
    }

    @Test
    void "creates user"() {
        User user = new User()
        when(restTemplate.postForObject(anyString(), anyMap(), any(Class)))
                .thenReturn(user)
        def result = userService.createUser("1")
        assert user == result.get()
    }

    @Test
    void "creates user when get returns 404"() {
        when(restTemplate.getForObject(anyString(), any(Class), anyString()))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND))
        when(restTemplate.postForObject(anyString(), anyMap(), any(Class)))
                .thenReturn(new User())
        userService.getUser("1")
        verify(restTemplate).postForObject(
                anyString(),
                argThat(allOf(
                        hasEntry("userId", "1"),
                )),
                any(Class))
    }

    @Test
    void "calls findUser when creating returns 409"() {
        when(restTemplate.getForObject(anyString(), any(Class), anyString()))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND))
                .thenReturn(new User())
        when(restTemplate.postForObject(anyString(), anyMap(), any(Class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.CONFLICT))
        userService.getUser("1")
        verify(restTemplate, times(2)).getForObject(
                anyString(),
                any(Class),
                anyString())
    }


}
