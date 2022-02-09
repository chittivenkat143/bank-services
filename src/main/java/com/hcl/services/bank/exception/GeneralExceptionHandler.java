package com.hcl.services.bank.exception;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.hcl.services.bank.domain.dto.BaseResponse;

@RestControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {
	public static final String ACCESS_DENIED = "Access denied!";
	public static final String INVALID_REQUEST = "Invalid request";
	public static final String ERROR_MESSAGE_TEMPLATE = "message: %s %n requested uri: %s";
	public static final String LIST_JOIN_DELIMITER = ",";
	public static final String FIELD_ERROR_SEPARATOR = ": ";
	private static final Logger local_logger = LoggerFactory.getLogger(GeneralExceptionHandler.class);
	private static final String ERRORS_FOR_PATH = "errors {} for path {}";
	private static final String PATH = "path";
	private static final String ERRORS = "error";
	private static final String STATUS = "status";
	private static final String MESSAGE = "message";
	private static final String TIMESTAMP = "timestamp";
	private static final String TYPE = "type";

	private DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
			.withLocale(Locale.ENGLISH).withZone(ZoneId.systemDefault());

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> validationErrors = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + FIELD_ERROR_SEPARATOR + error.getDefaultMessage())
				.collect(Collectors.toList());
		return getExceptionResponseEntity(ex, HttpStatus.BAD_REQUEST, request, validationErrors);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return getExceptionResponseEntity(ex, status, request, Collections.singletonList(ex.getLocalizedMessage()));
	}

	@ExceptionHandler({ ConstraintViolationException.class })
	public BaseResponse handleConstraintViolation(ConstraintViolationException exception, WebRequest request) {
		final List<String> validationErrors = exception.getConstraintViolations().stream()
				.map(violation -> violation.getPropertyPath() + FIELD_ERROR_SEPARATOR + violation.getMessage())
				.collect(Collectors.toList());
		return getExceptionResponseEntity(exception, HttpStatus.BAD_REQUEST, request, validationErrors);
	}

	@ExceptionHandler({ Exception.class })
	public BaseResponse handleAllExceptions(Exception ex, WebRequest request) {
		ResponseStatus responseStatus = ex.getClass().getAnnotation(ResponseStatus.class);
		final HttpStatus status = responseStatus != null ? responseStatus.value() : HttpStatus.INTERNAL_SERVER_ERROR;
		final String localizedMessage = ex.getLocalizedMessage();
		final String path = request.getDescription(false);
		String message = (StringUtils.isNotEmpty(localizedMessage) ? localizedMessage : status.getReasonPhrase());
		local_logger.error(String.format(ERROR_MESSAGE_TEMPLATE, message, path), ex);
		return getExceptionResponseEntity(ex, status, request, Collections.singletonList(message));
	}

	public BaseResponse getExceptionResponseEntity(final Exception exception, final HttpStatus status,
			final WebRequest request, final List<String> errors) {

		final Map<String, Object> body = new LinkedHashMap<>();
		final String path = request.getDescription(false);
		body.put(TIMESTAMP, formatter.format(Instant.now()));
		body.put(STATUS, status.value());
		body.put(ERRORS, errors);
		body.put(TYPE, exception.getClass().getSimpleName());
		body.put(PATH, path);
		body.put(MESSAGE, getMessageForStatus(status));
		final String errorsMessage = CollectionUtils.isNotEmpty(errors)
				? errors.stream().filter(StringUtils::isNotEmpty).collect(Collectors.joining(LIST_JOIN_DELIMITER))
				: status.getReasonPhrase();
		local_logger.error(ERRORS_FOR_PATH, errorsMessage, path);

		return new BaseResponse(body, status);// new ResponseEntity<>(body, status);
	}

	private String getMessageForStatus(HttpStatus status) {
		switch (status) {
		case UNAUTHORIZED:
			return ACCESS_DENIED;
		case BAD_REQUEST:
			return INVALID_REQUEST;
		default:
			return status.getReasonPhrase();
		}
	}

}
