package de.cooky.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class CookyExceptionHandler {

	private static final Logger LOG = LogManager.getLogger(CookyExceptionHandler.class);

	@ResponseBody
	@ExceptionHandler(CookyErrorMsg.class)
	public ResponseEntity<String> handleCorexException(CookyErrorMsg e) {

		LOG.error(e);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	}

}
