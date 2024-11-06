package com.project.scm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LogoutController {

    private static final Logger logger = LoggerFactory.getLogger(LogoutController.class);

    @PostMapping("/logout-success")
    public String logoutPost(Model model, CsrfToken csrfToken) {
        model.addAttribute("_csrf", csrfToken);
        logger.info("Logout request received via POST");
        return "logout";
    }

    @GetMapping("/logout-success")
    public String logoutGet(Model model, CsrfToken csrfToken) {
        model.addAttribute("_csrf", csrfToken);
        logger.info("Logout request received via GET");
        return "logout";
    }
}