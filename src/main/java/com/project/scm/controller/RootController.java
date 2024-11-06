package com.project.scm.controller;

import com.project.scm.helper.GetEmailOfLoggedInUser;
import com.project.scm.models.User;
import com.project.scm.services.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class RootController {

    private final Logger logger = LoggerFactory.getLogger(RootController.class);
    private final UserServiceImpl userServiceImpl;

    public RootController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @ModelAttribute
    public void addLoggedInUserToModel(Model model, Authentication authentication) {
        if (authentication == null) {
            return;
        }
        String email = GetEmailOfLoggedInUser.getEmail(authentication);
        logger.info("User profile request received {}", email);
        User user = userServiceImpl.getUserByEmail(email);
        logger.info("User profile request received {}", user.getName());
        logger.info("User profile request received {}", user.getEmail());


        model.addAttribute("loggedInUser", user);
    }

}
