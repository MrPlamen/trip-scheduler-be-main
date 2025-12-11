package main.web;

import main.service.CommentService;
import main.web.dto.Comment;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentsController {

    private final CommentService commentService;

    public CommentsController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/trips/{tripId}/comments")
    public List<Comment> getTripComments(@PathVariable String tripId) {
        return commentService.getComments(tripId);
    }
}

