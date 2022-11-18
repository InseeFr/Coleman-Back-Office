package fr.insee.coleman.api.configuration;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.keycloak.adapters.springsecurity.management.HttpSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import fr.insee.coleman.api.configuration.ApplicationProperties.Mode;

/**
 * SecurityConfiguration is the class using to configure security.<br>
 * 2 ways to authenticated : <br>
 * 0 - without authentication,<br>
 * 1 - basic authentication <br>
 * 
 * @author Claudel Benjamin
 * 
 */
@Configuration
@EnableWebSecurity
@ConditionalOnExpression("'${fr.insee.coleman.application.mode}' == 'basic' or '${fr.insee.coleman.application.mode}' == 'noauth'")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	/**
	 * The environment define in Spring application Generate with the application
	 * property environment
	 */
	@Autowired
	private Environment environment;

	@Autowired
	private ApplicationProperties applicationProperties;
	
	@Value("${fr.insee.coleman.helpdesk.role:#{null}}")
	private String helpdeskRole;
	
	@Value("${fr.insee.coleman.manager.role:#{null}}")
	private String managerRole;
	
	@Value("${fr.insee.coleman.batch.role:#{null}}")
	private String batchRole;
	
	@Value("${fr.insee.coleman.admin.role:#{null}}")
	private String adminRole;
	
	@Value("${fr.insee.coleman.webclient.role:#{null}}")
	private String webclientRole;

	/**
	 * This method check if environment is development or test
	 * 
	 * @return true if environment matchs
	 */
	protected boolean isDevelopment() {
		return ArrayUtils.contains(environment.getActiveProfiles(), "dev")
				|| ArrayUtils.contains(environment.getActiveProfiles(), "test");
	}

	/**
	 * This method configure the HTTP security access
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		System.setProperty("keycloak.enabled", applicationProperties.getMode() != Mode.keycloak ? "false" : "true");
		http
			// disable csrf because of API mode
			.csrf().disable().sessionManagement()
			// use previously declared bean
			.sessionAuthenticationStrategy(sessionAuthenticationStrategy())
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		if(this.applicationProperties.getMode() == Mode.basic) {
			http.httpBasic().authenticationEntryPoint(unauthorizedEntryPoint());
			http.authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll()
			// Autorize GET requests for all roles
	        .antMatchers(HttpMethod.GET, "/**")
	        .hasAnyRole(adminRole, helpdeskRole, managerRole, batchRole)
	        // Coleman Batch 
	        // Autorize Coleman batch to execute POST 
	        .antMatchers(HttpMethod.POST, "/campaigns/**/management-monitoring-info")
	        .hasAnyRole(adminRole, batchRole, managerRole)
	        .antMatchers(HttpMethod.POST, "/campaigns/**/management-monitoring-infos").hasAnyRole(adminRole, batchRole)
	        .antMatchers(HttpMethod.POST, "/campaigns/**/survey-units").hasAnyRole(adminRole, batchRole)
	        // Autorize manager to create and update campaigns
	        .antMatchers(HttpMethod.POST, "/campaigns").hasRole(adminRole)
	        .antMatchers(HttpMethod.PUT, "/campaigns/**").hasRole(adminRole)
	        .antMatchers(HttpMethod.DELETE, "/campaigns/**").hasAnyRole(adminRole, managerRole)
	        .antMatchers(HttpMethod.DELETE, "/management-monitoring-infos/**").hasAnyRole(adminRole, managerRole)
	        .antMatchers(HttpMethod.DELETE, "/uploads/**").hasAnyRole(adminRole, managerRole)
	        .antMatchers(HttpMethod.POST, "/campaigns/**/uploads").hasAnyRole(adminRole, managerRole)
	        // Autorize admin and web client to send mail
	        .antMatchers(HttpMethod.POST, "/contact/send-mail").hasAnyRole(adminRole, webclientRole)
	        .anyRequest().denyAll(); // refuse all other requests
		}else{
			http.httpBasic().disable();
			http.authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll()
			// configuration for endpoints
			.antMatchers("/campaigns/**/management-monitoring-info",
					"/campaigns/**/management-monitoring-infos",
					"/campaigns/**/survey-units",
					"/campaigns/**/uploads",
					"/campaigns",
					"/campaigns/**",
					"/management-monitoring-infos/**",
					"/uploads/**",
					"/swagger-ui.html")
			.permitAll();
		}
	}

	/**
	 * This method configure the authentication manager for DEV and TEST accesses
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		if (isDevelopment()) {
			switch (this.applicationProperties.getMode()) {
			case basic:
				auth.inMemoryAuthentication().withUser("INTW1").password("{noop}intw1")
						.and()
						.withUser("ABC").password("{noop}abc")
						.and()
						.withUser("JKL").password("{noop}jkl")
						.and()
						.withUser("noWrite").password("{noop}a");
				break;
			case noauth:
				break;
			default:
				break;
			}
		}
	}

	@Bean
	protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
		return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
	}

	@Bean
	@ConditionalOnMissingBean(HttpSessionManager.class)
	protected HttpSessionManager httpSessionManager() {
		return new HttpSessionManager();
	}

	/**
	 * This method configure the unauthorized accesses
	 */
	public AuthenticationEntryPoint unauthorizedEntryPoint() {
		return (request, response, authException) -> {
			response.addHeader("WWW-Authenticate", "BasicCustom realm=\"MicroService\"");
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
		};
	}

}
