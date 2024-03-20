package com.example.board.dto.request;

import com.example.board.dto.ArticleDto;
import com.example.board.dto.HashtagDto;
import com.example.board.dto.UserAccountDto;
import java.util.Set;

/**
 * DTO for {@link com.example.board.dto.ArticleDto} DTO for
 * {@link com.example.board.dto.UserAccountDto}
 */

public record ArticleRequest(
    String title,
    String content
) {

  public static ArticleRequest of(String title, String content) {
    return new ArticleRequest(title, content);
  }

  public ArticleDto toDto(UserAccountDto userAccountDto){
    return this.toDto(userAccountDto, null);
  }

  public ArticleDto toDto(UserAccountDto userAccountDto, Set<HashtagDto> hashtagDtos) {
    return ArticleDto.of(userAccountDto, title, content, hashtagDtos);
  }
}