package com.example.board.domain;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
    @Index(columnList = "title"),
    @Index(columnList = "hashtag"),
    @Index(columnList = "createdAt"),
    @Index(columnList = "modifiedBy")
})
@Entity
public class Article extends AuditingFields {

  @ToString.Exclude
  @OrderBy("createdAt DESC")
  @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
  private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  @ManyToOne(optional = false)
  @JoinColumn(name = "userId")
  private UserAccount userAccount;

  @Setter
  @Column(nullable = false)
  private String title; // 제목

  @Setter
  @Column(nullable = false, length = 10000)
  private String content; // 본문

  @ToString.Exclude
  @JoinTable(
      name = "article_hashtag",
      joinColumns = @JoinColumn(name = "articleId"), // 주인 입장
      inverseJoinColumns = @JoinColumn(name = "hashtagId") // 상대방 입장
      // 여기서 join / inverseColumn 설정 따로 안해도 상관 없지만 JoinColumn name을 따로 해줌으로써
  )
  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // persist(insert) merge(update)
  private Set<Hashtag> hashtags = new LinkedHashSet<>();


  protected Article() {
  }

  private Article(UserAccount userAccount, String title, String content) {
    this.userAccount = userAccount;
    this.title = title;
    this.content = content;
  }

  // Factory Method 방식을 통해 전달
  public static Article of(UserAccount userAccount, String title, String content) {
    return new Article(userAccount, title, content);
  }

  public void addHashtag(Hashtag hashtag){
    this.getHashtags().add(hashtag);
  }

  public void addHashtags(Collection<Hashtag> hashtags){
    this.getHashtags().addAll(hashtags);
  }

  public void clearHashtags() {
    this.getHashtags().clear();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Article article)) {
      return false;
    }
    return id != null && id.equals(article.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}
