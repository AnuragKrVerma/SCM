package com.project.scm.controller;

import com.project.scm.forms.UserForm;
import com.project.scm.helper.Message;
import com.project.scm.helper.MessageType;
import com.project.scm.models.User;
import com.project.scm.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    private UserService userService;

    public PageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String home(Model model) {
        model.addAttribute("name", "Substring Technologies");
        model.addAttribute("youtubeChannel", "Learn Code With Durgesh");
        model.addAttribute("githubRepo", "https://github.com/learncodewithdurgesh/");
        return "home";
    }

    @RequestMapping("/about")
    public String aboutPage(Model model) {
        model.addAttribute("isLogin", true);
        return "about";
    }

    @RequestMapping("/services")
    public String servicesPage() {
        System.out.println("services page loading");
        return "services";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }


    @GetMapping("/login")
    public String logIn(Model model, CsrfToken csrfToken) {
        model.addAttribute("_csrf", csrfToken);
        return "login";
    }

    @GetMapping("/signup")
    public String signUp(Model model, CsrfToken csrfToken) {
        model.addAttribute("_csrf", csrfToken);
        UserForm userForm = new UserForm();
        model.addAttribute("userForm", userForm);
        return "signup";
    }

    @PostMapping("do-signup")
    public String doSignUp(@Valid @ModelAttribute UserForm userForm, BindingResult bindingResult, HttpSession session) {
        System.out.println(userForm);

        if (bindingResult.hasErrors()) {
            return "signup";
        }

        User user = new User();
        user.setEmail(userForm.getEmail());
        user.setName(userForm.getName());
        user.setPassword(userForm.getPassword());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setProfilePic("https://upload.wikimedia.org/wikipedia/commons/f/f9/Phoenicopterus_ruber_in_S%C3%A3o_Paulo_Zoo.jpg");
        user.setAbout(userForm.getAbout());
        user.setEnabled(true);

        userService.saveUser(user);

        Message message = Message.builder().content("User Registered Successfully").type(MessageType.green).build();
        session.setAttribute("message", message);
        return "redirect:/signup";
    }
}