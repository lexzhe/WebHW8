package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.wp.domain.Comment;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.security.Guest;
import ru.itmo.wp.service.PostService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class PostPage extends Page {
    private final PostService postService;

    public PostPage(PostService postService) {
        this.postService = postService;
    }

    @Guest
    @GetMapping("/post/{stringId}")
    public String postGet(Model model,
                          @PathVariable("stringId") String stringId,
                          HttpSession httpSession) {
        long id;
        try {
            id = Long.parseLong(stringId);
        } catch (NumberFormatException e) {
            model.addAttribute("message", "Invalid request: corrupted post ID");
            return "redirect:/";
        }

        Post post = postService.findById(id);
        if (post == null) {
            putMessage(httpSession, "Invalid request: no such post");
            return "redirect:/";
        }

        model.addAttribute("post", post);
        model.addAttribute("newComment", new Comment());
        return "PostPage";
    }

    @PostMapping("/post/{stringId}")
    public String postPost(@PathVariable("stringId") String stringId,
                           @Valid @ModelAttribute("newComment") Comment comment,
                           BindingResult bindingResult,
                           Model model,
                           HttpSession httpSession) {
        long id;
        try {
            id = Long.parseLong(stringId);
        } catch (NumberFormatException e) {
            putMessage(httpSession, "Invalid request: corrupted post ID");
            return "redirect:/";
        }

        Post post = postService.findById(id);
        if (post == null) {
            putMessage(httpSession, "Invalid request: no such post");
            return "redirect:/";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("post", post);
            model.addAttribute("newComment", comment);
            return "PostPage";
        }

        User user = getUser(httpSession);
        if (user == null) {
            return "redirect:/accessDenied";
        }

        postService.writeComment(post, comment, user);
        putMessage(httpSession, "You published new comment");

        return "redirect:/post/" + stringId;
    }
}