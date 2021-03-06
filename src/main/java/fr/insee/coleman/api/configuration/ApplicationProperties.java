package fr.insee.coleman.api.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Component which defines the application configuration according
 * to the properties set by the user.
 * @author scorcaud
 *
 */
@Component
@ConfigurationProperties(prefix = "fr.insee.coleman.application", ignoreUnknownFields = false)
public class ApplicationProperties {
	
	/**
	 * The three ways of authentication
	 * Basic with SpringSecurity
	 * KeyCloack
	 * No Authentication
	 * @author scorcaud
	 *
	 */
	public enum Mode {basic, keycloak, noauth};
	
	/**
	 * The mode of authentication in the application
	 */
	private Mode mode;
	
	/**
	 * The crosOrigin for the configuration
	 */
	private String crosOrigin;

	/**
	 * @return the mode of authentication
	 */
	public Mode getMode() {
		return mode;
	}
	
	/**
	 * Set the mode of authentication in the application
	 * @param mode
	 */
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	
	/**
	 * @return the crosOrigin of the configuration
	 */
	public String getCrosOrigin() {
		return crosOrigin;
	}
	
	/**
	 * Set the crosOrigin for the configuration
	 * @param crosOrigin
	 */
	public void setCrosOrigin(String crosOrigin) {
		this.crosOrigin = crosOrigin;
	}
}
