package com.example.board.service;

import com.example.board.domain.SearchType;
import com.example.board.dto.ArticleDto;
import com.example.board.dto.ArticleWithCommentsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ArticleService {

  @Transactional(readOnly = true)
  public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword,
      Pageable pageable) {
    return Page.empty(
    );
  }

  @Transactional(readOnly = true)
  public ArticleWithCommentsDto getArticle(long l) {
    return null;
  }

  public void saveArticle(ArticleDto dto) {

  }

  public void updateArticle(ArticleDto dto) {

  }

  public void deleteArticle(long articleId) {

  }
}
