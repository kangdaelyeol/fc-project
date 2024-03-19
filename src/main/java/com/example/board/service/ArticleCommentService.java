package com.example.board.service;

import com.example.board.domain.Article;
import com.example.board.domain.ArticleComment;
import com.example.board.domain.UserAccount;
import com.example.board.dto.ArticleCommentDto;
import com.example.board.repository.ArticleCommentRepository;
import com.example.board.repository.ArticleRepository;
import com.example.board.repository.UserAccountRepository;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleCommentService {

  private final ArticleCommentRepository articleCommentRepository;
  private final ArticleRepository articleRepository;
  private final UserAccountRepository userAccountRepository;

  @Transactional(readOnly = true)
  public List<ArticleCommentDto> searchArticleComments(Long articleId) {

    return articleCommentRepository.findByArticle_Id(articleId)
        .stream()
        .map(ArticleCommentDto::from)
        .toList();
  }

  public void saveArticleComment(ArticleCommentDto dto) {
    try {
      Article article = articleRepository.getReferenceById(dto.articleId());
      UserAccount userAccount = userAccountRepository.getReferenceById(
          dto.userAccountDto().userId());
      articleCommentRepository.save(
          dto.toEntity(article, userAccount));
    } catch (EntityNotFoundException e) {
      log.warn("댓글 저장 실패, 댓글 작성에 필요한 정보를 찾을 수 없습니다 error: {}", e.getLocalizedMessage());
    }
  }

  public void updateArticleComment(ArticleCommentDto dto) {
    try {
      ArticleComment articleComment = articleCommentRepository.getReferenceById(dto.id());
      if (dto.content() != null) {
        articleComment.setContent(dto.content());
      }
    } catch (EntityNotFoundException e) {
      log.warn("댓글 업데이트 실패, 댓글을 찾을 수 없습니다 - dto: {}", dto);
    }
  }

  public void deleteArticleComment(Long id, String userId) {
    articleCommentRepository.deleteByIdAndUserAccount_UserId(id, userId);
  }
}