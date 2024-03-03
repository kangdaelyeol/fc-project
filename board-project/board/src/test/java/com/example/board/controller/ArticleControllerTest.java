package com.example.board.controller;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("View 컨트롤러 - 게시글")
@WebMvcTest(ArticleController.class)
public class ArticleControllerTest {
  private final MockMvc mvc;

  public ArticleControllerTest(@Autowired MockMvc mvc) {
    this.mvc = mvc;
  }

  @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상 호출")
  @Test
  public void givenNothing_whenRequestingArticlesView_thenReturnsArticlesView () throws Exception {
    // Given

    // When & Then
    mvc.perform(get("/articles"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.TEXT_HTML))
        .andExpect(model().attributeExists("articles"));
  }

  @DisplayName("[view][GET] 게시글 상세 페이지 - 정상 호출")
  @Test
  public void givenNothing_whenRequestingArticleView_thenReturnsArticleView () throws Exception {
    // Given

    // When & Then
    mvc.perform(get("/articles/1"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.TEXT_HTML))
        .andExpect(model().attributeExists("article"));
  }

  @DisplayName("[view][GET] 게시글 검색 전용 페이지 - 정상 호출")
  @Test
  public void givenNothing_whenRequestingArticleSearchView_thenReturnsArticleSearchView () throws Exception {
    // Given

    // When & Then
    mvc.perform(get("/articles/search"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.TEXT_HTML));
  }

  @DisplayName("[view][GET] 게시글 해시태그 검색 페이지 - 정상 호출")
  @Test
  public void givenNothing_whenRequestingArticleHashtagSearchView_thenReturnsArticleHashtagSearchView () throws Exception {
    // Given

    // When & Then
    mvc.perform(get("/articles/search-hasttag"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.TEXT_HTML));
  }
}
