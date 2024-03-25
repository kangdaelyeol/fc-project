package com.example.board.service;


import com.example.board.domain.UserAccount;
import com.example.board.dto.UserAccountDto;
import com.example.board.repository.UserAccountRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserAccountService {

  private final UserAccountRepository userAccountRepository;

  @Transactional(readOnly = true)
  public Optional<UserAccountDto> searchUser(String username){
    return userAccountRepository.findById(username)
        .map(UserAccountDto::from);
    // 예외처리는 Service Layer에서 해주지 않고 이것을 처리하는 Dto영역 또는 컨트롤러 영역, Security영역(여기선 Security config에서 처리)에서 처리 하도록 하고, 이 곳에서는 검색 결과 까지만 반환한다.
  }

  public UserAccountDto saveUser(String username, String password, String email, String nickname, String memo){
    return UserAccountDto.from(
        userAccountRepository.save(UserAccount.of(username, password, email, nickname, memo, username))
    );
  }
}
