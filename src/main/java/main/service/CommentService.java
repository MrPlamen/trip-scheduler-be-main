package main.service;
import main.client.CommentClient;
import main.web.dto.Comment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentClient commentClient;

    public CommentService(CommentClient commentClient) {
        this.commentClient = commentClient;
    }

    public Comment addComment(Comment comment) {
        return commentClient.createComment(comment);
    }

    public List<Comment> getComments(String tripId) {
        return commentClient.getCommentsByTripId(tripId);
    }

    public void deleteComment(String id) {
        commentClient.deleteComment(id);
    }
}

