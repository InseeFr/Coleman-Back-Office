package fr.insee.coleman.api.services;


import javax.servlet.http.HttpServletRequest;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import fr.insee.coleman.api.configuration.ApplicationProperties;
import fr.insee.coleman.api.constants.Constants;

@Service
public class UserService {
	
	@Autowired
	private ApplicationProperties applicationProperties;

	public String getUserId(HttpServletRequest request) {
		String userId = null;
		switch (applicationProperties.getMode()) {
		case basic:
			Object basic = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (basic instanceof UserDetails) {
				userId = ((UserDetails) basic).getUsername();
			} else {
				userId = basic.toString();
			}
			break;
		case keycloak:
			KeycloakAuthenticationToken keycloak = (KeycloakAuthenticationToken) request.getUserPrincipal();
			userId = keycloak.getPrincipal().toString();
			break;
		default:
			userId = Constants.GUEST;
			break;
		}
		return userId;
	}

}
