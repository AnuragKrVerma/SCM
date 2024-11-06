package com.project.scm.services.oauth;

import com.project.scm.models.Providers;
import com.project.scm.models.User;
import com.project.scm.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);
    private static final String GOOGLE = "google";
    private static final String GITHUB = "github";

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Log all attributes
//        oAuth2User.getAttributes().forEach((k, v) -> logger.info("Key: {}, Value: {}", k, v));

        // Determine the provider
        String provider = userRequest.getClientRegistration().getRegistrationId();
        logger.info("User signed in provider: {}", provider);

        // Extract user information from OAuth2User
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        User user = new User();
        user.setEnabled(true);
        user.setId(UUID.randomUUID().toString());
        user.setEmailVerified(true);
        user.setRoleList(List.of("ROLE_USER"));
        user.setPassword(" ");

        switch (provider) {
            case GOOGLE:
                user.setProfilePic(oAuth2User.getAttribute("picture"));
                user.setEmail(email);
                user.setName(name);
                user.setProviderId(oAuth2User.getName());
                user.setAbout("This account is created using Google");
                user.setProvider(Providers.GOOGLE);
                break;
            case GITHUB:
                user.setProfilePic(oAuth2User.getAttribute("avatar_url"));
                user.setName(name);

                // Extract the GitHub username
                String githubUsername = oAuth2User.getAttribute("login");
                if (githubUsername == null) {
                    logger.error("GitHub username not found for user: {}", oAuth2User.getName());
                    throw new IllegalStateException("GitHub username not found");
                }

                // Construct the email address
                user.setEmail(githubUsername + "@github.com");
                user.setProviderId(oAuth2User.getName());
                user.setAbout("This account is created using GitHub");
                user.setProvider(Providers.GITHUB);
                break;
            default:
                logger.error("Unsupported provider: {}", provider);
                throw new IllegalStateException("Unsupported provider: " + provider);
        }

        // Save or update user in the database
        User existingUser = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (existingUser == null) {
            userRepository.save(user);
            logger.info("New user saved: {}", user);
        } else {
            logger.info("User already exists: {}", existingUser);
        }

        logger.info("User Info: Email: {}, Name: {}", user.getEmail(), user.getName());

        return oAuth2User;
    }
}