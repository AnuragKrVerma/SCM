package com.project.scm.controller;

import com.project.scm.helper.GetEmailOfLoggedInUser;
import com.project.scm.models.User;
import com.project.scm.services.impl.ContactServiceImpl;
import com.project.scm.services.impl.UserServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;


@Controller
@RequestMapping("/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserServiceImpl userServiceImpl;
    private final ContactServiceImpl contactServiceImpl;

    public UserController(UserServiceImpl userServiceImpl, ContactServiceImpl contactServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.contactServiceImpl = contactServiceImpl;
    }

//    user dashboard page

    @GetMapping("/dashboard")
    public String userDashboard() {
        return "user/dashboard";
    }


    @GetMapping("/profile")
    public String userProfile(Model model, Principal principal) {


        return "user/profile";
    }


}
