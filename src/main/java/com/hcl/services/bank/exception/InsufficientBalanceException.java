package com.hcl.services.bank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InsufficientBalanceException extends BaseException {
	private static final long serialVersionUID = 1L;
	public InsufficientBalanceException(String message) {
		super(message);
	}
}
