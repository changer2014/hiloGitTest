package com.hilo.service.api.confiuration.security;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class SkipPathRequestMatcher implements RequestMatcher {

  private OrRequestMatcher matchers;
  private RequestMatcher processingMatcher;

  public SkipPathRequestMatcher(List<RequestMatcher> skipMatcher, RequestMatcher matcher) {
    matchers = new OrRequestMatcher(skipMatcher);
    processingMatcher = matcher;
  }


  @Override
  public boolean matches(HttpServletRequest request) {
    if (matchers.matches(request)) {
      return false;
    }
    return processingMatcher.matches(request);
  }
}
