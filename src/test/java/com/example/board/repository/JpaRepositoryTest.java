package com.example.board.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.board.domain.Article;
import com.example.board.domain.ArticleComment;
import com.example.board.domain.Hashtag;
import com.example.board.domain.UserAccount;
import com.example.board.repository.JpaRepositoryTest.TestJpaConfig;
import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@DisplayName("JPA 연결 테스트")
@Import(TestJpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {

  private final ArticleRepository articleRepository;
  private final ArticleCommentRepository articleCommentRepository;
  private final UserAccountRepository userAccountRepository;
  private final HashtagRepository hashtagRepository;

  JpaRepositoryTest(
      @Autowired ArticleRepository articleRepository,
      @Autowired ArticleCommentRepository articleCommentRepository,
      @Autowired UserAccountRepository userAccountRepository,
      @Autowired HashtagRepository hashtagRepository) {
    this.articleRepository = articleRepository;
    this.articleCommentRepository = articleCommentRepository;
    this.userAccountRepository = userAccountRepository;
    this.hashtagRepository = hashtagRepository;
  }

  @DisplayName("select test")
  @Test
  void givenTestData_whenSelecting_thenWorksFine() {
    // Given

    // When
    List<Article> articles = articleRepository.findAll();

    // Then
    assertThat(articles).isNotNull().hasSize(123); // classpath:resources/data.sql 참조
  }

  @DisplayName("insert test")
  @Test
  void givenTestData_whenInserting_thenWorksFine() {
    // Given
    long previousCount = articleRepository.count();
    UserAccount userAccount = userAccountRepository.save(
        UserAccount.of("uno", "pw", null, null, null));
    Article article = Article.of(userAccount, "new article", "new content");
    article.addHashtags(Set.of(Hashtag.of("spring")));
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
    Hashtag updatedHashtag = Hashtag.of("springboot");
    article.clearHashtags();
    article.addHashtags(Set.of(updatedHashtag));

    // When
    Article savedArticle = articleRepository.saveAndFlush(article);

    // Then
    assertThat(savedArticle.getHashtags())
        .hasSize(1)
        .extracting("hashtagName", String.class)
        .containsExactly(updatedHashtag.getHashtagName());
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

  @DisplayName("대댓글 조회 리스트")
  @Test
  void givenParentCommentId_whenSelecting_thenReturnsChildComments() {
    // Given

    // When
    Optional<ArticleComment> parentComment = articleCommentRepository.findById(1L);

    // Then
    assertThat(parentComment).get()
        .hasFieldOrPropertyWithValue("parentCommentId", null)
        .extracting("childComments", InstanceOfAssertFactories.COLLECTION)
            .hasSize(4); // extracting 기존 데이터가 아닌 데이터 안에 데이터를 조회하는 거라 extracting하면 따로 보는 것이 좋아 indent처리 했다.
  }

  @DisplayName("댓글에 대댓글 삽입 테스트")
  @Test
  void givenParentComment_whenSaving_thenInsertsChildComment() {
    // Given
    ArticleComment parentComment = articleCommentRepository.getReferenceById(1L);
    ArticleComment childComment = ArticleComment.of(
        parentComment.getArticle(),
        parentComment.getUserAccount(),
        "대댓글"
    );

    // When
    parentComment.addChildComment(childComment);
    articleRepository.flush();
    // Then
    assertThat(articleCommentRepository.findById(1L)).get()
        .hasFieldOrPropertyWithValue("parentCommentId", null)
        .extracting("childComments", InstanceOfAssertFactories.COLLECTION)
            .hasSize(5);
  }

  @DisplayName("대댓글 삭제 연동 테스트")
  @Test
  void givenArticleCommentHavingChildComments_whenDeletingParentComment_thenDeletesEveryComment() {
    // Given
    ArticleComment parentComment = articleCommentRepository.getReferenceById(1L);
    long previousArticleComment = articleCommentRepository.count();

    // When
    articleCommentRepository.delete(parentComment);

    // Then
    assertThat(articleCommentRepository.count()).isEqualTo(previousArticleComment - 5);
  }

  @DisplayName("댓글 삭제와 대댓글 전체 연동 삭제 테스트 - 댓글 ID + 유저 ID")
  @Test
  void givenArticleCommentIdHavingChildCommentsAndUserId_whenDeletingParentComment_thenDeletesEveryComment() {
    // Given
    long previousArticleCommentCount = articleCommentRepository.count();

    // When
    articleCommentRepository.deleteByIdAndUserAccount_UserId(1L, "uno");

    // Then
    assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - 5);
  }

  @DisplayName("[Querydsl] 전체 hashtag 리스트에서 이름만 조회하기")
  @Test
  void givenNothing_whenQueryingHashtags_thenReturnsHashtagNames() {
    // Given

    // When
    List<String> hashtagNames = hashtagRepository.findAllHashtagNames();
    // Then
    assertThat(hashtagNames).hasSize(19);
  }

  @DisplayName("[Querydsl] hashtag로 페이징된 게시글 검색하기")
  @Test
  void givenHashtagNamesAndPageable_whenQueryingArticles_thenReturnsArticlePage() {
    // Given
    List<String> hashtagNames = List.of("blue", "crimson", "fuscia");
    Pageable pageable = PageRequest.of(0, 5, Sort.by(
        Sort.Order.desc("hashtags.hashtagName"),
        Sort.Order.asc("title")
    ));

    // When
    Page<Article> articlePage = articleRepository.findByHashtagNames(hashtagNames, pageable);

    // Then
    assertThat(articlePage.getContent()).hasSize(pageable.getPageSize());
    assertThat(articlePage.getContent().get(0).getTitle()).isEqualTo("Fusce posuere felis sed lacus.");
    assertThat(articlePage.getContent().get(0).getHashtags()).extracting("hashtagName", String.class)
        .containsExactly("fuscia");
    assertThat(articlePage.getTotalElements()).isEqualTo(17);
    assertThat(articlePage.getTotalPages()).isEqualTo(4);
  }

  // 인증과 분리 시키기 위해 Test를 위한 Auditing config class도 생성해준다 @TestConfiguration을 통해 Test시에만 포함 될 수 있게 한다.
  @EnableJpaAuditing
  @TestConfiguration
  static class TestJpaConfig {

    @Bean
   AuditorAware<String> auditorAware() {
      return () -> Optional.of("uno");
    }
  }
}