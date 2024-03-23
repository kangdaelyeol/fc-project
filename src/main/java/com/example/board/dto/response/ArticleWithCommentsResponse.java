package com.example.board.dto.response;

import com.example.board.dto.ArticleCommentDto;
import com.example.board.dto.ArticleWithCommentsDto;
import com.example.board.dto.HashtagDto;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * DTO for {@link com.example.board.dto.ArticleWithCommentsDto}
 */
public record ArticleWithCommentsResponse(
    Long id,
    String title,
    String content,
    Set<String> hashtags,
    LocalDateTime createdAt,
    String email,
    String nickname,
    Set<ArticleCommentResponse> articleCommentsResponse,
    String userId
) {

  public static ArticleWithCommentsResponse of(Long id,
      String title,
      String content,
      Set<String> hashtags,
      LocalDateTime createdAt,
      String email,
      String nickname,
      Set<ArticleCommentResponse> articleCommentResponses,
      String userId) {
    return new ArticleWithCommentsResponse(id,
        title,
        content,
        hashtags,
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
        dto.hashtagDtos().stream()
            .map(HashtagDto::hashtagName)
            .collect(Collectors.toUnmodifiableSet()),
        dto.createdAt(),
        dto.userAccountDto().email(),
        nickname,
        organizeChildComments(dto.articleCommentDtos()),
        dto.userAccountDto().userId());
  }

  private static Set<ArticleCommentResponse> organizeChildComments(Set<ArticleCommentDto> dtos){
    Map<Long, ArticleCommentResponse> map = dtos.stream()
        .map(ArticleCommentResponse::from)
        .collect(Collectors.toMap(ArticleCommentResponse::id, Function.identity()));

    map.values().stream()
        .filter(ArticleCommentResponse::hasParentComment) // 부모 댓글이 있는 댓글, 즉 자식 댓글만 필터링 한다
        .forEach(comment -> {
          ArticleCommentResponse parentComment = map.get(comment.parentCommentId()); // 해당 자식 댓글들의 부모 댓글을 꺼낸다.
          parentComment.childComments().add(comment); // 꺼낸 부모 댓글에 자식 댓글을 추가한다.
        });

    return map.values().stream()
        .filter(comment -> !comment.hasParentComment()) // root 댓글만 필터링 한다
        .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator
            .comparing(ArticleCommentResponse::createdAt).reversed()
            .thenComparingLong(ArticleCommentResponse::id))));
  }
}