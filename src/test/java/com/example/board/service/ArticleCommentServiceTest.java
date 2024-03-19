package com.example.board.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.example.board.domain.Article;
import com.example.board.domain.ArticleComment;
import com.example.board.domain.UserAccount;
import com.example.board.dto.ArticleCommentDto;
import com.example.board.dto.UserAccountDto;
import com.example.board.repository.ArticleCommentRepository;
import com.example.board.repository.ArticleRepository;
import com.example.board.repository.UserAccountRepository;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;

@DisplayName("비즈니스 로직 - 댓글")
@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {

  @InjectMocks
  private ArticleCommentService sut;

  @Mock
  private ArticleRepository articleRepository;
  @Mock
  private ArticleCommentRepository articleCommentRepository;

  @Mock
  private UserAccountRepository userAccountRepository;

  @DisplayName("게시글 ID로 조회하면, 해당하는 댓글 리스트를 반환한다.")
  @Test
  void givenArticleId_whenSearchingArticleComments_thenReturnsArticleComments() {
    // Given
    Long articleId = 1L;
    ArticleComment expected = createArticleComment("content");
    given(articleCommentRepository.findByArticle_Id(articleId)).willReturn(List.of(expected));

    // When
    List<ArticleCommentDto> actual = sut.searchArticleComments(articleId);

    // Then
    assertThat(actual).hasSize(1).first()
        .hasFieldOrPropertyWithValue("content", expected.getContent());
    then(articleCommentRepository).should().findByArticle_Id(articleId);
  }

  @DisplayName("댓글 정보를 입력하면, 댓글을 저장한다.")
  @Test
  void givenArticleCommentInfo_whenSavingArticleComment_thenSavesArticleComment() {
    // Given
    ArticleCommentDto dto = createArticleCommentDto("댓글");
    given(articleRepository.getReferenceById(dto.articleId())).willReturn(createArticle());
    given(userAccountRepository.getReferenceById(dto.userAccountDto().userId())).willReturn(
        createUserAccount());
    given(articleCommentRepository.save(any(ArticleComment.class))).willReturn(null);

    // When
    sut.saveArticleComment(dto);

    // Then
    then(articleRepository).should().getReferenceById(dto.articleId());
    then(userAccountRepository).should().getReferenceById(dto.userAccountDto().userId());
    then(articleCommentRepository).should().save(any(ArticleComment.class));
  }

  @DisplayName("댓글 저장을 시도했는데 맞는 게시글이 없으면, 경고 로그를 찍고 아무것도 안 한다.")
  @Test
  void givenNonExistArticle_whenSavingArticleComment_thenLogsSituationAndDoesNothing() {
    // Given
    ArticleCommentDto dto = createArticleCommentDto("댓글");
    given(articleRepository.getReferenceById(dto.articleId())).willThrow(
        EntityNotFoundException.class);

    // When
    sut.saveArticleComment(dto);

    // Then
    then(articleRepository).should().getReferenceById(dto.articleId());
    then(userAccountRepository).shouldHaveNoInteractions();
    then(articleCommentRepository).shouldHaveNoInteractions();
  }

  @DisplayName("댓글 정보를 입력하면. 댓글을 수정한다.")
  @Test
  void givenArticleCommentInfo_whenUpdatingArticleComment_thenUpdateArticleComment() {
    // Given
    String oldContent = "content";
    String updatedContent = "댓글";
    ArticleComment articleComment = createArticleComment(oldContent);
    ArticleCommentDto dto = createArticleCommentDto(updatedContent);
    given(articleCommentRepository.getReferenceById(dto.id())).willReturn(articleComment);

    // When
    sut.updateArticleComment(dto);

    // Then
    assertThat(articleComment.getContent())
        .isNotEqualTo(oldContent)
        .isEqualTo(updatedContent);
    then(articleCommentRepository).should().getReferenceById(dto.id());
  }

  @DisplayName("없는 댓글 정보를 수정하려 하면, 경고 로그를 찍고 아무 것도 안 한다")
  @Test
  void givenNonexistArticleComment_whenUpdatingArticleComment_thenLogsWarningAndDoesNothing() {
    // Given
    ArticleCommentDto dto = createArticleCommentDto("댓글");
    given(articleCommentRepository.getReferenceById(dto.id())).willThrow(
        EntityNotFoundException.class);

    // When
    sut.updateArticleComment(dto);

    // Then
    then(articleCommentRepository).should().getReferenceById(dto.id());
  }

  @WithUserDetails(value = "testid", setupBefore = TestExecutionEvent.TEST_EXECUTION)
  @DisplayName("댓글 ID를 입력하면, 댓글을 삭제 한다")
  @Test
  void givenArticleCommentId_whenDeletingArticleComment_thenDeletesArticleComment() {
    // Given
    Long articleCommentId = 1L;
    String userId = "testid";
    willDoNothing().given(articleCommentRepository)
        .deleteByIdAndUserAccount_UserId(articleCommentId, userId);

    // When
    sut.deleteArticleComment(articleCommentId, userId);

    // Then
    then(articleCommentRepository).should()
        .deleteByIdAndUserAccount_UserId(articleCommentId, userId);
  }

  private UserAccountDto createUserAccountDto() {
    return UserAccountDto.of(
        "uno",
        "password",
        "uno@gamil.com",
        "nickname",
        "This is memo",
        LocalDateTime.now(),
        "uno",
        LocalDateTime.now(),
        "uno");
  }

  private ArticleCommentDto createArticleCommentDto(String content) {
    return ArticleCommentDto.of(1L,
        1L,
        createUserAccountDto(),
        content,
        LocalDateTime.now(),
        "uno",
        LocalDateTime.now(),
        "uno");
  }


  private ArticleComment createArticleComment(String content) {
    return ArticleComment.of(
        Article.of(createUserAccount(), "title", "content", "hashtag"),
        createUserAccount(),
        content
    );
  }

  private UserAccount createUserAccount() {
    return UserAccount.of("uno",
        "pw",
        "uno@gmain.com",
        "Uno",
        null);
  }

  private Article createArticle() {
    return Article.of(
        createUserAccount(),
        "title",
        "content",
        "#java"
    );
  }
}