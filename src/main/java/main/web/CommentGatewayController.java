package main.web;

import main.client.CommentClient;
import main.web.dto.Comment;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentGatewayController {

    private final CommentClient commentClient;

    public CommentGatewayController(CommentClient commentClient) {
        this.commentClient = commentClient;
    }

    @PostMapping
    public Comment create(@RequestBody Comment comment) {
        return commentClient.createComment(comment);
    }

    @GetMapping("/trip/{tripId}")
    public List<Comment> getByTrip(@PathVariable String tripId) {
        return commentClient.getCommentsByTripId(tripId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        commentClient.deleteComment(id);
    }
}

