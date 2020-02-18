package sber.edu.webserver.common;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import sber.edu.session.SessionKeeper;
import sber.edu.session.info.SessionId;
import sber.edu.webserver.responses.exceptions.UnauthorizedException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

@Component
public class RequestsInterceptor implements HandlerInterceptor {
  public static final Logger logger = LogManager.getLogger(RequestsInterceptor.class);

  private Optional<SessionId> getTokenCookie(Cookie[] cookies) {
    if (cookies == null ||
      cookies.length == 0) {
      return Optional.empty();
    }
    Optional<Cookie> tokenCookie = Arrays.stream(cookies).filter(cookie -> "token".equals(cookie.getName())).findFirst();

    if (!tokenCookie.isPresent()) {
      return Optional.empty();
    }

    return Optional.of(new SessionId(tokenCookie.get().getValue()));
  }

  @Override
  public boolean preHandle(
    HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String url = request.getRequestURI();
    logger.info("Start processing request " + url);

    if (url.isEmpty() ||
      url.endsWith(".css") ||
      url.endsWith(".js") ||
      url.endsWith(".html") ||
      url.endsWith(".jpg")) {
      return true;
    }

    if ("/".equals(url) ||
      "/login".equals(url) ||
      "/register".equals(url) ||
      "/administration/users".equals(url)) {
      return true;
    }

    Optional<SessionId> tokenCookie = getTokenCookie(request.getCookies());

    if (!tokenCookie.isPresent() ||
      !SessionKeeper.getInstance().validateSession(tokenCookie.get())) {
      throw new UnauthorizedException();
    }

    return true;
  }

  @Override
  public void postHandle(
    HttpServletRequest request, HttpServletResponse response, Object handler,
    ModelAndView modelAndView) throws Exception {
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                              Object handler, Exception exception) throws Exception {
    logger.info("Finish processing request " + request.getRequestURI());
  }
}
