package com.example.board.config;


import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.example.board.domain.UserAccount;
import com.example.board.dto.UserAccountDto;
import com.example.board.repository.UserAccountRepository;
import com.example.board.service.UserAccountService;
import java.util.Optional;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;


// 인증 config 클래스 자체가 테스트 환경는 아니므로 테스트 관련된 어노테이션을 클래스 자체에 주지 않음.
@Import(SecurityConfig.class)
public class TestSecurityConfig {

  @MockBean
  private UserAccountService userAccountService;

  // spring 과 관련된 테스트를 할때만 이 셋업이 진행됨, 각 테스트가 실행되기 이전에 이 메서드가 실행되게 한다.
  @BeforeTestMethod
  public void securitySetup() {
    given(userAccountService.searchUser(anyString())).willReturn(Optional.of(createUserAccountDto()));
    given(userAccountService.saveUser(anyString(), anyString(),anyString(),anyString(),anyString())).willReturn(
        createUserAccountDto()
    );
  }

  private UserAccountDto createUserAccountDto() {
    return UserAccountDto.of(
        "unoTest",
        "pw",
        "uno-test@email.com",
        "uno-test",
        "test memo"
    );
  }
}
