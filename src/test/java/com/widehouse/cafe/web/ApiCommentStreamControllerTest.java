package com.widehouse.cafe.web;

import com.widehouse.cafe.domain.article.CommentReactiveRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiCommentStreamControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CommentReactiveRepository commentRepository;

    @Test
    public void getAllCommentsTest() {
        webTestClient.get().uri("/api/articles/1/stream/comments")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8);
    }

}
