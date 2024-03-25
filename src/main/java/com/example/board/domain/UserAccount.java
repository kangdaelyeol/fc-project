package com.example.board.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.catalina.User;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
    @Index(columnList = "email", unique = true),
    @Index(columnList = "createdAt"),
    @Index(columnList = "createdBy")
})
@Entity
public class UserAccount extends AuditingFields {

  @Id
  @Column(length = 50)
  private String userId;

  @Setter
  @Column(nullable = false)
  private String userPassword;

  @Setter
  @Column(length = 100)
  private String email;
  @Setter
  @Column(length = 100)
  private String nickname;
  @Setter
  private String memo;

  protected UserAccount() {
  }

  private UserAccount(String userId, String userPassword, String email, String nickname,
      String memo, String createdBy) {
    this.userId = userId;
    this.userPassword = userPassword;
    this.email = email;
    this.nickname = nickname;
    this.memo = memo;
    this.createdBy = createdBy;
    this.modifiedBy = createdBy;
  }

  public static UserAccount of(String userId, String userPassword, String email, String nickname,
      String memo) {
    return UserAccount.of(userId, userPassword, email, nickname, memo, null);
  }

  public static UserAccount of(String userId, String userPassword, String email, String nickname,
      String memo, String createdBy) {
    return new UserAccount(userId, userPassword, email, nickname, memo, createdBy);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof UserAccount userAccount)) {
      return false;
    }

    return userId != null && userId.equals(userAccount.getUserId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getUserId());
  }
}
