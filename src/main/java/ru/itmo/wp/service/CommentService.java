package ru.itmo.wp.service;

import ru.itmo.wp.repository.CommentRepository;

public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

}