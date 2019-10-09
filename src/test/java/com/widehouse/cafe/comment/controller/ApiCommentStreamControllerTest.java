package com.widehouse.cafe.comment.controller;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.widehouse.cafe.comment.entity.CommentReactiveRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class ApiCommentStreamControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CommentReactiveRepository commentRepository;

    @Test
    void getAllCommentsTest() {
        webTestClient.get().uri("/api/articles/1/stream/comments")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8);
    }

}
