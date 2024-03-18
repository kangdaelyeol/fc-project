package com.example.board.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.board.domain.Article;
import com.example.board.domain.UserAccount;
import com.example.board.repository.JpaRepositoryTest.TestJpaConfig;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@DisplayName("JPA 연결 테스트")
@Import(TestJpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {

  private final ArticleRepository articleRepository;
  private final ArticleCommentRepository articleCommentRepository;
  private final UserAccountRepository userAccountRepository;

  public JpaRepositoryTest(
      @Autowired ArticleRepository articleRepository,
      @Autowired ArticleCommentRepository articleCommentRepository,
      @Autowired UserAccountRepository userAccountRepository) {
    this.articleRepository = articleRepository;
    this.articleCommentRepository = articleCommentRepository;
    this.userAccountRepository = userAccountRepository;
  }

  @DisplayName("select test")
  @Test
  void givenTestData_whenSelecting_thenWorksFine() {
    // Given

    // When
    List<Article> articles = articleRepository.findAll();

    // Then
    assertThat(articles).isNotNull().hasSize(123);
  }

  @DisplayName("insert test")
  @Test
  void givenTestData_whenInserting_thenWorksFine() {
    // Given
    long previousCount = articleRepository.count();
    UserAccount userAccount = userAccountRepository.save(
        UserAccount.of("uno", "pw", null, null, null));
    Article article = Article.of(userAccount, "new article", "new content", "#spring");
    // When
    Article savedArticle = articleRepository.save(
        article);

    // Then
    assertThat(articleRepository.count()).isEqualTo(previousCount + 1);
  }

  @DisplayName("update test")
  @Test
  void givenTestData_whenUpdating_thenWorksFine() {
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
  void givenTestData_whenDeleting_thenWorksFine() {
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


  // 인증과 분리 시키기 위해 Test를 위한 Auditing config class도 생성해준다 @TestConfiguration을 통해 Test시에만 포함 될 수 있게 한다.
  @EnableJpaAuditing
  @TestConfiguration
  public static class TestJpaConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
      return () -> Optional.of("uno");
    }
  }
}