package com.yak.security.web;
import com.yak.security.service.*;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
public class TokenAuthenticationFilter implements Filter {
  private final TokenService tokens;
  private final String header;
  public TokenAuthenticationFilter(TokenService t, String h) {
    tokens = t;
    header = h;
  }
  public void doFilter(ServletRequest request, ServletResponse response,
                       FilterChain chain) throws IOException, ServletException {
    try {
      SecurityContext.set(
          tokens.resolve(((HttpServletRequest)request).getHeader(header)));
      chain.doFilter(request, response);
    } finally {
      SecurityContext.clear();
    }
  }
}
