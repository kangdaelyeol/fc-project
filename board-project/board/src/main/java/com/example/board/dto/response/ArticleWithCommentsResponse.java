package com.example.board.dto.response;

import com.example.board.dto.ArticleWithCommentsDto;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DTO for {@link com.example.board.dto.ArticleWithCommentsDto}
 */
public record ArticleWithCommentsResponse(
    Long id,
    String title,
    String content,
    String hashtag,
    LocalDateTime createdAt,
    String email,
    String nickname,
    Set<ArticleCommentResponse> articleCommentsResponse,
    String userId
) {

  public static ArticleWithCommentsResponse of(Long id,
      String title,
      String content,
      String hashtag,
      LocalDateTime createdAt,
      String email,
      String nickname,
      Set<ArticleCommentResponse> articleCommentResponses,
      String userId) {
    return new ArticleWithCommentsResponse(id,
        title,
        content,
        hashtag,
        createdAt,
        email,
        nickname,
        articleCommentResponses,
        userId);
  }

  public static ArticleWithCommentsResponse from(ArticleWithCommentsDto dto) {
    String nickname = dto.userAccountDto().nickname();
    if (nickname == null || nickname.isBlank()) {
      nickname = dto.userAccountDto().userId();
    }

    return new ArticleWithCommentsResponse(dto.id(),
        dto.title(),
        dto.content(),
        dto.hashtag(),
        dto.createdAt(),
        dto.userAccountDto().email(),
        nickname,
        dto.articleCommentDtos().stream().map(ArticleCommentResponse::from)
            .collect(Collectors.toCollection(
                LinkedHashSet::new)),
        dto.userAccountDto().userId());
  }
}