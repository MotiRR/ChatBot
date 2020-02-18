package sber.edu.webserver.responses;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sber.edu.webserver.common.RequestsInterceptor;
import sber.edu.webserver.responses.exceptions.BadRequestException;
import sber.edu.webserver.responses.exceptions.BusinessLogicException;
import sber.edu.webserver.responses.exceptions.InternalServerErrorException;
import sber.edu.webserver.responses.exceptions.UnauthorizedException;
import sber.edu.webserver.responses.responses.ServerError;

@ControllerAdvice
public class GlobalErrorHandler {
  public static final Logger logger = LogManager.getLogger(GlobalErrorHandler.class);

  @ExceptionHandler
  public ResponseEntity<Object> handler(Throwable exception) {
    try {
      Throwable causedException = exception.getCause();
      Throwable exceptionToThrow = causedException != null ? causedException : exception;
      logger.info("Exception occurred: " + exceptionToThrow);
      throw exceptionToThrow;
    } catch (UnauthorizedException ex) {
      return ServerError.generate(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    } catch (InternalServerErrorException ex) {
      logger.error("StackTrace: " + ex.getStackTrace());
      return ServerError.generateInternalError();
    } catch (BadRequestException ex) {
      return ServerError.generate(ex.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (BusinessLogicException ex) {
      return ServerError.generate(ex.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (Throwable ex) {
      logger.error("StackTrace: " + ex.getStackTrace());
      return ServerError.generateInternalError();
    }
  }
}