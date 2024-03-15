package com.example.board.repository.querydsl;

import com.example.board.domain.Article;
import com.example.board.domain.QArticle;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ArticleRepositoryCustomImpl extends QuerydslRepositorySupport implements
    ArticleRepositoryCustom {

  public ArticleRepositoryCustomImpl() {
    super(Article.class);
  }

  @Override
  public List<String> findAllDistinctHashtags() {
    QArticle article = QArticle.article;

    JPQLQuery<String> query = from(article)
        .distinct()
        .select(article.hashtag)
        .where(article.hashtag.isNotNull());

    return query.fetch();
  }

}
