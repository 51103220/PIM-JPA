package com.dedorewan.website.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.dedorewan.website.exception.CustomException;
import com.dedorewan.website.exception.ProjectNumberAlreadyExistsException;

@ControllerAdvice
public class GlobalExceptionHandlerController {
	@Value("${application.errors.default}")
	private String default_errors;

	@ExceptionHandler(value = { Exception.class, RuntimeException.class,
			ProjectNumberAlreadyExistsException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public String handleAllException(Exception ex) {
		return ex.getMessage();

	}

	@ExceptionHandler(CustomException.class)
	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ResponseBody
	public String handleCustomException(CustomException ex) {
		return ex.getErrMsg();
	}
}
