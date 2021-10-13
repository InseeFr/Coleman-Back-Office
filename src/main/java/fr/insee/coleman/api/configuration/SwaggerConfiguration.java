package fr.insee.coleman.api.configuration;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.insee.coleman.api.configuration.ApplicationProperties.Mode;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationCodeGrant;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.Contact;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.OAuth;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.TokenEndpoint;
import springfox.documentation.service.TokenRequestEndpoint;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

	@Value("${spring.application.name}")
	private String name;

	@Value("${keycloak.resource}")
	private String clientId;

	@Value("${keycloak.auth-server-url}")
	private String serverUrl;

	@Value("${keycloak.realm}")
	private String realm;

	@Autowired
	BuildProperties buildProperties;

	@Autowired
	private ApplicationProperties applicationProperties;

	public static final String SECURITY_SCHEMA_OAUTH2 = "oauth2";

	@Bean
	public Docket productApi() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2);
		List<ResponseMessage> messages = Arrays.asList(
				new ResponseMessageBuilder().code(200).message("Success!").build(),
				new ResponseMessageBuilder().code(401).message("Not authorized!").build(),
				new ResponseMessageBuilder().code(403).message("Forbidden!").build(),
				new ResponseMessageBuilder().code(404).message("Not found!").build());
		docket.select().apis(RequestHandlerSelectors.basePackage("fr.insee.coleman.api.controller")).build()
				.apiInfo(apiInfo())
				.useDefaultResponseMessages(false)
				.globalResponseMessage(RequestMethod.GET, messages)
				.globalResponseMessage(RequestMethod.PUT, messages)
				.securitySchemes(securitySchema())
				.securityContexts(securityContext());
		return docket;
	}

	private ApiInfo apiInfo() {
		return new ApiInfo(buildProperties.getName(), "Back-office services for Coleman application",
			buildProperties.getVersion(), "",
			new Contact("Metallica", "https://github.com/InseeFr/Coleman-Back-Office", ""), "", "",
			Collections.emptyList());
	}

	private List<SecurityScheme> securitySchema() {
		switch (this.applicationProperties.getMode()) {
		case basic:
			return List.of(new BasicAuth(name));
		case keycloak:
			final String AUTH_SERVER = serverUrl + "/realms/" + realm + "/protocol/openid-connect/auth";
			final String AUTH_SERVER_TOKEN_ENDPOINT = serverUrl + "/realms/" + realm + "/protocol/openid-connect/token";
			final GrantType grantType = new AuthorizationCodeGrant(
					new TokenRequestEndpoint(AUTH_SERVER, clientId, null),
					new TokenEndpoint(AUTH_SERVER_TOKEN_ENDPOINT, "access_token"));
			final List<AuthorizationScope> scopes = new ArrayList<>();
			scopes.add(new AuthorizationScope("sampleScope", "there must be at least one scope here"));
			return List.of(new OAuth(SECURITY_SCHEMA_OAUTH2, scopes, Collections.singletonList(grantType)));
		default:
			return List.of();
		}
	}

	private List<SecurityContext> securityContext() {
		switch (this.applicationProperties.getMode()) {
		case basic:
			return List.of(SecurityContext.builder()
					.securityReferences(List.of(new SecurityReference(name, new AuthorizationScope[0])))
					.forPaths(PathSelectors.any()).build());
		case keycloak:
			return List.of(SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build());
		default:
			return List.of();
		}
	}

	private List<SecurityReference> defaultAuth() {
		final AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		final AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Collections.singletonList(new SecurityReference(SECURITY_SCHEMA_OAUTH2, authorizationScopes));
	}

	@Bean
	public SecurityConfiguration security() {

		Map<String, Object> additionalQueryStringParams=new HashMap<>();
		additionalQueryStringParams.put("kc_idp_hint","sso-insee");

		if(this.applicationProperties.getMode()==Mode.keycloak)
			return SecurityConfigurationBuilder.builder().clientId(clientId).realm(realm).scopeSeparator(",").additionalQueryStringParams(additionalQueryStringParams).build();
		else
			return null;
	}

}
