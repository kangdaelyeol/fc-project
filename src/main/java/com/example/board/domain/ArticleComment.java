package com.example.board.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
    @Index(columnList = "content"),
    @Index(columnList = "createdAt"),
    @Index(columnList = "createdBy")
})
@Entity
public class ArticleComment extends AuditingFields {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  @JoinColumn(name = "userId")
  @ManyToOne(optional = false)
  private UserAccount userAccount;

  @Setter
  @ManyToOne(optional = false)
  private Article article; // 게시글(ID)

  @Setter
  @Column(nullable = false, length = 500)
  private String content; // 본문

  /*
   * Lombok Annotation
   * @NoArgsConstructor(access = AccessLevel.PROTECTED (same)
   * */
  protected ArticleComment() {
  }

  private ArticleComment(Article article, UserAccount userAccount, String content) {
    this.userAccount = userAccount;
    this.article = article;
    this.content = content;
  }

  public static ArticleComment of(Article article, UserAccount userAccount, String content) {
    return new ArticleComment(article, userAccount, content);
  }

  // 동일성, 동등성 검사 equals and hashcode
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ArticleComment that)) {
      return false;
    }
    return id != null && id.equals(that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}
