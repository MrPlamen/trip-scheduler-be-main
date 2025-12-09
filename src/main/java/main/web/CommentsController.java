package main.web;

import main.service.CommentService;
import main.service.VisitItemService;
import main.web.dto.Comment;
import main.web.dto.VisitItemResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class CommentsController {

    private final VisitItemService visitItemService;
    private final CommentService commentService;

    public CommentsController(VisitItemService visitItemService, CommentService commentService) {
        this.visitItemService = visitItemService;
        this.commentService = commentService;
    }

    @GetMapping("/trips/{tripId}/comments")
    public List<Comment> getTripComments(@PathVariable String tripId) {
        return commentService.getComments(tripId);
    }
}

