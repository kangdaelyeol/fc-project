package com.example.board.repository.querydsl;

import com.example.board.domain.Article;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleRepositoryCustom {

  @Deprecated
  List<String> findAllDistinctHashtags();

  Page<Article> findByHashtagNames(Collection<String> hashtagNames, Pageable pageable);
}
