package fr.insee.coleman.api.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateImportConverter {

	static final Logger LOGGER = LoggerFactory.getLogger(DateImportConverter.class);

	private static SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm");

	public static Date convertToDate(String dateStr) {
		try {
			return format.parse(dateStr);
		} catch (ParseException e) {
			LOGGER.error("Error of parsing for value: {}", dateStr);
			return new Date();
		}

	}

}
