package com.example.board.dto;

import com.example.board.domain.Hashtag;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Value;

/**
 * DTO for {@link HashtagDto}
 */

// 하나의 해시태그 정보로 여러 게시글을 검색할 수 있게끔 디자인
public record HashtagWithArticlesDto  ( // hashtag 기준 여러 개의 Article
    Long id,
    Set<ArticleDto> articles,
    String hashtagName,
    LocalDateTime createdAt,
    String createdBy,
    LocalDateTime modifiedAt,
    String modifiedBy
){
  public static HashtagWithArticlesDto of(Long id, Set<ArticleDto> articles, String hashtagName, LocalDateTime createdAt,String createdBy, LocalDateTime modifiedAt, String modifiedBy){
    return new HashtagWithArticlesDto(id, articles, hashtagName, createdAt, createdBy, modifiedAt, modifiedBy);
  }

  public static HashtagWithArticlesDto of(Set<ArticleDto> articles, String hashtagName){
    return new HashtagWithArticlesDto(null, articles, hashtagName, null, null, null, null);
  }

  public static HashtagWithArticlesDto from(Hashtag entity) { // from -> Input Entity, 받는 엔티티와 밀접환 연관
    return new HashtagWithArticlesDto(
        entity.getId(),
        // getArticles -> LinkedHashSet 반환 -> 이를 일반 Set(UnmodifiableSet)으로 바꾸어 주어야 한다.
        entity.getArticles().stream()
            .map(ArticleDto::from)
            .collect(Collectors.toUnmodifiableSet()),
        entity.getHashtagName(),
        entity.getCreatedAt(),
        entity.getCreatedBy(),
        entity.getModifiedAt(),
        entity.getModifiedBy()
    );
  }

  public Hashtag toEntity() {
    return Hashtag.of(hashtagName);
  }
}