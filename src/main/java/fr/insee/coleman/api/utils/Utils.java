package fr.insee.coleman.api.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Utils {
	
	
	public static Connection openConnection() throws Exception {
		Properties properties = loadProperties();
		Class.forName(properties.getProperty("driver"));
		String url = properties.getProperty("url");
		Connection conn = DriverManager.getConnection(url,properties.getProperty("username"), properties.getProperty("password"));
		return conn;
	}
	
	/**
	 * Charge les properties de l'application
	 * @return Les properties de l'application en fonction de l'environnement
	 * @throws IOException Si les properties n'existent pas ou ne sont pas lisibles
	 */
	public static Properties loadProperties() throws IOException {
		Properties properties = new Properties();
		properties.load(Utils.class.getClassLoader().getResourceAsStream("properties.properties"));
		return properties;
	}


}
