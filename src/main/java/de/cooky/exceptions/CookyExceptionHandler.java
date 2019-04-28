package de.cooky.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import de.cooky.rest.RecipeController;

@ControllerAdvice
public class CookyExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(RecipeController.class);

	@ResponseBody
	@ExceptionHandler(CookyErrorMsg.class)
	public ResponseEntity<String> handleCorexException(CookyErrorMsg e) {

		LOG.error("exception", e);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	}

}
