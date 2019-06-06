package com.example.michel.rest_api;

import com.example.michel.rest_api.models.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testUpdateUser() throws Exception {
        User user = new User(58, new Timestamp(new Date().getTime()), "new1", "kekeke", "new6@kek8",
                "", 1, 2, 2001);

        Map<String, String> rmap = new HashMap<>();
        rmap.put("username", "new6@kek8");
        rmap.put("password", "x12345");

        HttpEntity<Map> authEntity = new HttpEntity<Map>(rmap);

        ResponseEntity authResponse = restTemplate.exchange("http://localhost:" + port + "/login",
                HttpMethod.POST, authEntity, String.class);

        List<String> token = authResponse.getHeaders().get("Authorization");

        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.add("Authorization", token.get(0));

        HttpEntity<User> requestUserEntity = new HttpEntity<User>(user, reqHeaders);


        ResponseEntity<Map> userResponse = restTemplate.exchange("http://localhost:" + port + "/user/updateUser",
                HttpMethod.POST, requestUserEntity, Map.class);

        assertThat((String) userResponse.getBody().get("userEmail")).contains(user.getEmail());
    }
}
