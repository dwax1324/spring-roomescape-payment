package roomescape.system.exception;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import roomescape.system.dto.response.ErrorResponse;
import roomescape.system.exception.error.ErrorType;
import roomescape.system.exception.model.AssociatedDataExistsException;
import roomescape.system.exception.model.CustomException;
import roomescape.system.exception.model.DataDuplicateException;
import roomescape.system.exception.model.ForbiddenException;
import roomescape.system.exception.model.NotFoundException;
import roomescape.system.exception.model.UnauthorizedException;
import roomescape.system.exception.model.ValidateException;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(value = {NotFoundException.class, ValidateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotFoundException(final CustomException e) {
        logger.error(e.getMessage(), e);
        return ErrorResponse.of(e.getErrorType(), e.getMessage());
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException(final HttpMessageNotReadableException e) {
        logger.error(e.getMessage(), e);
        return ErrorResponse.of(ErrorType.INVALID_REQUEST_DATA_TYPE,
                ErrorType.INVALID_REQUEST_DATA_TYPE.getDescription());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        logger.error(e.getMessage(), e);

        return ErrorResponse.of(ErrorType.INVALID_REQUEST_DATA, e.getMessage());
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnauthorizedException(final UnauthorizedException e) {
        logger.error(e.getMessage(), e);
        return ErrorResponse.of(e.getErrorType(), e.getMessage());
    }

    @ExceptionHandler(value = ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbiddenException(final ForbiddenException e) {
        logger.error(e.getMessage(), e);
        return ErrorResponse.of(e.getErrorType(), e.getMessage());
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
        logger.error(e.getMessage(), e);
        return ErrorResponse.of(ErrorType.METHOD_NOT_ALLOWED, ErrorType.METHOD_NOT_ALLOWED.getDescription());
    }

    @ExceptionHandler(value = HttpClientErrorException.class)
    public ErrorResponse handlePaymentException(final HttpClientErrorException e, final HttpServletResponse response) {
        logger.error(e.getMessage(), e);
        response.setStatus(e.getStatusCode().value());

        return ErrorResponse.of(ErrorType.PAYMENT_ERROR, e.getMessage());
    }

    @ExceptionHandler(value = {DataDuplicateException.class, AssociatedDataExistsException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(final CustomException e) {
        logger.error(e.getMessage(), e);
        return ErrorResponse.of(e.getErrorType(), e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(final Exception e) {
        logger.error(e.getMessage(), e);
        return ErrorResponse.of(ErrorType.INTERNAL_SERVER_ERROR, ErrorType.INTERNAL_SERVER_ERROR.getDescription());
    }
}