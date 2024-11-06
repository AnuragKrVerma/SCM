package com.project.scm.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

public class GetEmailOfLoggedInUser {

    private static final Logger logger = LoggerFactory.getLogger(GetEmailOfLoggedInUser.class);
    private static final String GOOGLE = "google";
    private static final String GITHUB = "github";

    public static String getEmail(Authentication authentication) {
        if (authentication.getPrincipal() instanceof OAuth2AuthenticatedPrincipal oAuth2Principal) {
            String clientId = authentication.getPrincipal().toString();

            if (clientId.contains(GOOGLE)) {
                logger.info("Provider: GOOGLE");
                String email = oAuth2Principal.getAttribute("email");
                if (email != null) {
                    return email;
                }
            } else if (clientId.contains(GITHUB)) {
                logger.info("Provider: GITHUB");
                String githubUsername = oAuth2Principal.getAttribute("login");
                if (githubUsername != null) {
                    return githubUsername + "@github.com";
                }
            }
        }
        return authentication.getName();
    }
}