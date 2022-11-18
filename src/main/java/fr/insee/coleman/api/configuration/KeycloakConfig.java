package fr.insee.coleman.api.configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.account.KeycloakRole;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.preauth.x509.X509AuthenticationFilter;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * This class defines the KeyCloak configuration
 *
 * @author scorcaud
 */
@Configuration
@ConditionalOnExpression("'${fr.insee.coleman.application.mode}' == 'keycloak'")
@ComponentScan(
        basePackageClasses = KeycloakSecurityComponents.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "org.keycloak.adapters.springsecurity.management.HttpSessionManager"))
@EnableWebSecurity
public class KeycloakConfig extends KeycloakWebSecurityConfigurerAdapter {

    @Value("${fr.insee.coleman.respondent.role:#{null}}")
    private String respondentRole;

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

    @Value("${fr.insee.coleman.token.claim.role:#{null}}")
    private String otherClaimRole;

    /**
     * Configure the accessible URI without any roles or permissions
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // disable csrf because of API mode
                .csrf().disable()
                .sessionManagement()
                // use previously declared bean
                .sessionAuthenticationStrategy(sessionAuthenticationStrategy())
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // keycloak filters for securisation
                .and()
                .addFilterBefore(keycloakPreAuthActionsFilter(), LogoutFilter.class)
                .addFilterBefore(keycloakAuthenticationProcessingFilter(), X509AuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
                // delegate logout endpoint to spring security
                .and()
                .logout()
                .addLogoutHandler(keycloakLogoutHandler())
                .logoutUrl("/logout").logoutSuccessHandler(
                // logout handler for API
                (HttpServletRequest request, HttpServletResponse response, Authentication authentication) ->
                        response.setStatus(HttpServletResponse.SC_OK))
                .and()
                // manage routes securisation
                .authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll()
                // configuration for Swagger
                .antMatchers("/swagger-ui.html/**", "/v2/api-docs", "/csrf", "/", "/webjars/**", "/swagger-resources/**").permitAll()
                .antMatchers("/environnement", "/healthcheck").permitAll()
                // Autorize GET requests for respondent
                .antMatchers(HttpMethod.GET, "/campaigns/redirect-unit/contact/**")
                .hasAnyRole(respondentRole,adminRole)
                .antMatchers(HttpMethod.GET, "/api/check-habilitation")
                .hasAnyRole(respondentRole,managerRole,adminRole)
                // Autorize GET requests for all roles except respondent
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
                .antMatchers(HttpMethod.DELETE, "/campaigns/**").hasAnyRole(adminRole)
                .antMatchers(HttpMethod.DELETE, "/management-monitoring-infos/**").hasAnyRole(adminRole, managerRole)
                .antMatchers(HttpMethod.DELETE, "/uploads/**").hasAnyRole(adminRole, managerRole)
                .antMatchers(HttpMethod.POST, "/campaigns/**/uploads").hasAnyRole(adminRole, managerRole)
                // Autorize admin and web client to send mail
                .antMatchers(HttpMethod.POST, "/contact/send-mail").hasAnyRole(adminRole, webclientRole)
                .anyRequest().denyAll(); // refuse all other requests
    }

    /**
     * Registers the KeycloakAuthenticationProvider with the authentication
     * manager.
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(new KeycloakAuthenticationProvider() {

            @SuppressWarnings("unchecked")
            @Override
            public Authentication authenticate(Authentication authentication) {
                KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) authentication;
                List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
                List<String> inseeGroupeDefaut = new ArrayList<>();
                Object principal = authentication.getPrincipal();
                AccessToken accessToken;
                if (principal instanceof KeycloakPrincipal) {
                    KeycloakPrincipal<KeycloakSecurityContext> kp = (KeycloakPrincipal<KeycloakSecurityContext>) principal;
                    accessToken = kp.getKeycloakSecurityContext().getToken();
                    if (otherClaimRole != null) {
                        inseeGroupeDefaut = (List<String>) accessToken.getOtherClaims().get(otherClaimRole);
                    }
                }
                for (String role : token.getAccount().getRoles()) {
                    grantedAuthorities.add(new KeycloakRole(role));
                }
                if (inseeGroupeDefaut != null) {
                    for (String role : inseeGroupeDefaut) {
                        grantedAuthorities.add(new KeycloakRole(role));
                    }
                }
                return new KeycloakAuthenticationToken(token.getAccount(), token.isInteractive(), new SimpleAuthorityMapper().mapAuthorities(grantedAuthorities));
            }

        });
    }

    /**
     * Required to handle spring boot configurations
     *
     * @return
     */
    @ConditionalOnExpression("'${fr.insee.coleman.application.mode}' == 'keycloak'")
    @Bean
    public KeycloakSpringBootConfigResolver keycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    /**
     * Defines the session authentication strategy.
     */
    @Bean
    @ConditionalOnExpression("'${fr.insee.coleman.application.mode}' == 'keycloak'")
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        // required for bearer-only applications.
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }
}