package com.example.board.dto.response;

import com.example.board.dto.ArticleCommentDto;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * DTO for {@link com.example.board.dto.ArticleCommentDto}
 */
public record ArticleCommentResponse(
    Long id,
    String content,
    LocalDateTime createdAt,
    String email,
    String nickname,
    String userId,
    Long parentCommentId,
    Set<ArticleCommentResponse> childComments
) {

  public static ArticleCommentResponse of(Long id,
      String content,
      LocalDateTime createdAt,
      String email,
      String nickname,
      String userId,
      Long parentCommentId,
      Set<ArticleCommentResponse> childComments) {
    return new ArticleCommentResponse(id, content, createdAt, email, nickname, userId, parentCommentId, childComments);
  }

  public static ArticleCommentResponse of(Long id,
      String content,
      LocalDateTime createdAt,
      String email,
      String nickname,
      String userId) {
    return ArticleCommentResponse.of(id,
        content,
        createdAt,
        email,
        nickname,
        userId,
        null);
  }

  public static ArticleCommentResponse of(Long id,
      String content,
      LocalDateTime createdAt,
      String email,
      String nickname,
      String userId,
      Long parentCommentId){
    Comparator<ArticleCommentResponse> childCommentComparator = Comparator
        .comparing(ArticleCommentResponse::createdAt)
        .thenComparingLong(ArticleCommentResponse::id);
      return ArticleCommentResponse.of(id, content, createdAt, email, nickname, userId, parentCommentId, new TreeSet<>(childCommentComparator));
  }

  public static ArticleCommentResponse from(ArticleCommentDto dto) {
    String nickname = dto.userAccountDto().nickname();
    if (nickname == null || nickname.isBlank()) {
      nickname = dto.userAccountDto().userId();
    }
    return ArticleCommentResponse.of(
        dto.id(),
        dto.content(),
        dto.createdAt(),
        dto.userAccountDto().email(),
        nickname,
        dto.userAccountDto().userId(),
        dto.parentCommentId()
    );
  }

  public boolean hasParentComment () {
    return parentCommentId != null;
  }
}