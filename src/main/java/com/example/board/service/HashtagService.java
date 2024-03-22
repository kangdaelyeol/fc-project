package com.example.board.service;

import com.example.board.domain.Hashtag;
import com.example.board.repository.HashtagRepository;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HashtagService {

  private final HashtagRepository hashtagRepository;

  public Set<String> parseHashtagNames(String content){
    if(content == null) {
      return Set.of();
    }
    Pattern pattern = Pattern.compile("#[\\w가-힣]+");
    Matcher matcher = pattern.matcher(content.strip());
    Set<String> result = new HashSet<>();

    while (matcher.find()){
      result.add(matcher.group().replace("#", ""));
    }

    // unModifiable, 불변 형태로 보내주기 위해 Set.copyOf 사용
    return Set.copyOf(result);
  }

  public Set<Hashtag> findHashtagsByNames(Set<String> hashtagNames){
    return new HashSet<>(hashtagRepository.findByHashtagNameIn(hashtagNames));
  }
  
  public void deleteHashtagWithoutArticles(Long hashtagId){
    Hashtag hashtag = hashtagRepository.getReferenceById(hashtagId);
    if(hashtag.getArticles().isEmpty()){
      hashtagRepository.delete(hashtag);
    }
  }
}
