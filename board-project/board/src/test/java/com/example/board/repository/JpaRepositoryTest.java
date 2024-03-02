package com.example.board.repository;

import static org.assertj.core.api.Assertions.*;

import com.example.board.config.JpaConfig;
import com.example.board.domain.Article;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;


@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {
  private final ArticleRepository articleRepository;
  private final ArticleCommentRepository articleCommentRepository;


  public JpaRepositoryTest(
      @Autowired ArticleRepository articleRepository,
      @Autowired ArticleCommentRepository articleCommentRepository) {
    this.articleRepository = articleRepository;
    this.articleCommentRepository = articleCommentRepository;
  }

  @DisplayName("select test")
  @Test
  void givenTestData_whenSelecting_thenWorksFine () {
    // Given


    // When
    List<Article> articles = articleRepository.findAll();


    // Then
    assertThat(articles).isNotNull().hasSize(100);
  }

  @DisplayName("insert test")
  @Test
  void givenTestData_whenInserting_thenWorksFine () {
    // Given
    long previousCount = articleRepository.count();

    // When
    Article savedArticle = articleRepository.save(Article.of("new article", "new content", "#spring"));

    // Then
    assertThat(articleRepository.count()).isEqualTo(previousCount + 1);
  }

  @DisplayName("update test")
  @Test
  void givenTestData_whenUpdating_thenWorksFine () {
    // Given
    Article article = articleRepository.findById(1L).orElseThrow();
    String updatedHashtag = "#Springboot";
    article.setHashtag(updatedHashtag);

    // When
    Article savedArticle = articleRepository.saveAndFlush(article);

    // Then
    assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag", updatedHashtag);
  }

  @DisplayName("delete test")
  @Test
  void givenTestData_whenDeleting_thenWorksFine () {
    // Given
    Article article = articleRepository.findById(1L).orElseThrow();
    long prevArticleCount = articleRepository.count();
    long prevCommentCount = articleCommentRepository.count();
    long deletedCommentSize = article.getArticleComments().size();

    // When
    articleRepository.delete(article);

    // Then
    assertThat(articleRepository.count()).isEqualTo(prevArticleCount - 1);
    assertThat(articleCommentRepository.count()).isEqualTo(prevCommentCount - deletedCommentSize);
  }
}