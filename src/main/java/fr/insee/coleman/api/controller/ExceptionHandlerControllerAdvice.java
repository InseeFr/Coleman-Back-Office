package fr.insee.coleman.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import fr.insee.coleman.api.exception.DuplicateResourceException;
import fr.insee.coleman.api.exception.RessourceNotFoundException;
import fr.insee.coleman.api.exception.RessourceNotValidatedException;

@RestControllerAdvice
public class ExceptionHandlerControllerAdvice {

	@ExceptionHandler(RessourceNotFoundException.class)
	public ResponseEntity<String> nullPointerExceptionHandler(HttpServletRequest request,
			RessourceNotFoundException exception) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
	}

	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<String> resourceAlreadyExistsExceptionHandler(HttpServletRequest request,
			DuplicateResourceException exception) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
	}

	@ExceptionHandler(RessourceNotValidatedException.class)
	public ResponseEntity<String> resourceNonValideExceptionHandler(HttpServletRequest request,
			RessourceNotValidatedException exception) {
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(exception.getMessage());
	}

}
