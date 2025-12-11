package main.service;

import main.client.CommentClient;
import main.web.dto.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    private CommentClient commentClient;
    private CommentService commentService;

    @BeforeEach
    void setup() {
        commentClient = Mockito.mock(CommentClient.class);
        commentService = new CommentService(commentClient);
    }

    @Test
    void testAddComment() {
        Comment input = new Comment("1", "trip1", "hello", "user1");
        Comment expected = new Comment("1", "trip1", "hello", "user1");

        when(commentClient.createComment(input)).thenReturn(expected);

        Comment result = commentService.addComment(input);

        assertEquals(expected, result);
        verify(commentClient, times(1)).createComment(input);
    }

    @Test
    void testGetComments() {
        List<Comment> comments = List.of(
                new Comment("1", "trip1", "msg1", "user1"),
                new Comment("2", "trip1", "msg2", "user2")
        );

        when(commentClient.getCommentsByTripId("trip1")).thenReturn(comments);

        List<Comment> result = commentService.getComments("trip1");

        assertEquals(2, result.size());
        assertEquals(comments, result);
        verify(commentClient, times(1)).getCommentsByTripId("trip1");
    }

    @Test
    void testDeleteComment() {
        commentService.deleteComment("123");

        verify(commentClient, times(1)).deleteComment("123");
    }
}
