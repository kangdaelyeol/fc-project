package com.example.board.controller;


import com.example.board.domain.SearchType;
import com.example.board.dto.response.ArticleResponse;
import com.example.board.dto.response.ArticleWithCommentsResponse;
import com.example.board.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RequestMapping("/articles")
@Controller
public class ArticleController {

  private final ArticleService articleService;

  @GetMapping
  public String articles(@RequestParam(required = false) SearchType searchType,
      @RequestParam(required = false) String searchValue,
      @PageableDefault(size = 10, sort = "createdAt", direction = Direction.DESC) Pageable pageable,
      ModelMap map) {

    map.addAttribute("articles", articleService.searchArticles(searchType, searchValue, pageable)
        .map(ArticleResponse::from));

    return "articles/index";
  }

  @GetMapping("/{articleId}")
  public String article(@PathVariable Long articleId, ModelMap map) {
    ArticleWithCommentsResponse article = ArticleWithCommentsResponse.from(
        articleService.getArticle(articleId));
    map.addAttribute("article", article); // TODO: 구현할 때 실제 데이터를 주어야 함
    map.addAttribute("articleComments", article.articleCommentsResponse());

    return "articles/detail";
  }
}
