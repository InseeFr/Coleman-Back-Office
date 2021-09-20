package fr.insee.coleman.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestBasic {
	static final Logger LOGGER = LoggerFactory.getLogger(RestBasic.class);

	@Value("${coleman.environnement}")
	private String environnement;

	@RequestMapping(value = "/helloworld", method = RequestMethod.GET)
	public String helloWorld() {
		LOGGER.warn("Passing in HW !!!");
		return "Hello World";
	}

	@RequestMapping(value = "/env", method = RequestMethod.GET)
	public String env() {
		LOGGER.warn("Passing in env !!!");
		return "Environnement : " + environnement;
	}
}
