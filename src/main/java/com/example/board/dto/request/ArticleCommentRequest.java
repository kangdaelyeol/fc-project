package com.example.board.dto.request;

import com.example.board.dto.ArticleCommentDto;
import com.example.board.dto.UserAccountDto;

/**
 * DTO for {@link com.example.board.domain.ArticleComment}
 */
public record ArticleCommentRequest(Long articleId, String content) {

  public static ArticleCommentRequest of(Long articleId, String content) {
    return new ArticleCommentRequest(articleId, content);
  }

  public ArticleCommentDto toDto(UserAccountDto userAccountDto) {
    return ArticleCommentDto.of(
        articleId,
        userAccountDto,
        content
    );
  }

}