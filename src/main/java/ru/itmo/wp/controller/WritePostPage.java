package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.domain.Role;
import ru.itmo.wp.form.PostCredentials;
import ru.itmo.wp.form.validator.PostCredentialsValidator;
import ru.itmo.wp.security.AnyRole;
import ru.itmo.wp.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class WritePostPage extends Page {
    private final UserService userService;
    private final PostCredentialsValidator postCredentialsValidator;

    public WritePostPage(UserService userService, PostCredentialsValidator postCredentialsValidator) {
        this.userService = userService;
        this.postCredentialsValidator = postCredentialsValidator;
    }

    @InitBinder("postForm")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(postCredentialsValidator);
    }

    @AnyRole({Role.Name.WRITER, Role.Name.ADMIN})
    @GetMapping("/writePost")
    public String writePostGet(Model model) {
        model.addAttribute("postForm", new PostCredentials());
        return "WritePostPage";
    }

    @AnyRole({Role.Name.WRITER, Role.Name.ADMIN})
    @PostMapping("/writePost")
    public String writePostPost(@Valid @ModelAttribute("postForm") PostCredentials post,
                                BindingResult bindingResult,
                                HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            return "WritePostPage";
        }

        userService.writePost(getUser(httpSession), post);
        putMessage(httpSession, "You published new post");

        return "redirect:/posts";
    }
}