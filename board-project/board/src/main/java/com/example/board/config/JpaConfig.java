package com.example.board.config;

import com.example.board.dto.security.BoardPrincipal;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@EnableJpaAuditing
@Configuration
public class JpaConfig {

  @Bean
  public AuditorAware<String> auditorAware() {
    return () -> Optional.ofNullable(SecurityContextHolder.getContext())
        .map(SecurityContext::getAuthentication)
        .filter(Authentication::isAuthenticated)
        .map(Authentication::getPrincipal)
        .map(BoardPrincipal.class::cast)
        .map(BoardPrincipal::getUsername);

    // getContext - 모든 인증정보가 있는 컨텍스트 받아오기
    // getAuthentication - 인증 정보 받아오기
    // filter(Authentication::isAuthenticated) - 인증된 사용자인가 필터링
    // Authentication::getPrincipal - principal(UserDetails 구현체) 받아오기(어떤 Principal과 관련 있는지 몰라 Object형태로 return함)
    // BoardPrincipal.class::cast - 받아온 principal정보를 우리가 구현한 principal형으로 type casting함 (같은 UserDetails를 구현하므로 가능)
    // BoardPrincipal::getUsername - 우리가 구현한 principal에서 userName을 받아오기.
  }
}
