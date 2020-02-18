package sber.edu.webserver.common;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RequestsInterceptor implements HandlerInterceptor {
  public static final Logger logger = LogManager.getLogger(RequestsInterceptor.class);

  @Override
  public boolean preHandle(
    HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String url = request.getRequestURI();
    logger.info("Start processing request " + url);
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
