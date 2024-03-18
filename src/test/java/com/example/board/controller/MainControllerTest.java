package com.example.board.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.example.board.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@Import(TestSecurityConfig.class)
@WebMvcTest(MainController.class)
class MainControllerTest {

  private final MockMvc mvc;

  public MainControllerTest(@Autowired MockMvc mvc) throws Exception {
    this.mvc = mvc;
  }

  @Test
  void givenNothing_whenRequestingRootPage_thenRedirectsToArticlePage() throws Exception {
    // Given

    // When & Then
    mvc.perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(view().name("forward:/articles"))
        .andExpect(forwardedUrl("/articles"));
  }
}