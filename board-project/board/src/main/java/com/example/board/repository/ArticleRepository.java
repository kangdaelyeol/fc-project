package com.example.board.repository;

import com.example.board.domain.Article;
import com.example.board.domain.QArticle;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository extends
    JpaRepository<Article, Long>,
    QuerydslPredicateExecutor<Article>,
    QuerydslBinderCustomizer<QArticle> {

  Page<Article> findByTitleContaining(String title, Pageable pageable);

  Page<Article> findByContentContaining(String content, Pageable pageable);

  Page<Article> findByUserAccount_UserIdContaining(String content, Pageable pageable);

  Page<Article> findByUserAccount_NicknameContaining(String content, Pageable pageable);

  Page<Article> findByHashtag(String hashtag, Pageable pageable);

  @Override
  default void customize(QuerydslBindings bindings, QArticle root) {
    bindings.excludeUnlistedProperties(true);
    bindings.including(root.title, root.hashtag, root.createdAt, root.createdBy, root.content);
    bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
    bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
    bindings.bind(root.createdAt).first(DateTimeExpression::eq);
    bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
  }
}
