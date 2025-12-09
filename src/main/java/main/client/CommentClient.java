package main.client;

import main.web.dto.Comment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@FeignClient(name = "comments-service", url = "http://localhost:8081") // URL of your comments microservice
public interface CommentClient {

    @PostMapping("/comments")
    Comment createComment(@RequestBody Comment comment);

    @GetMapping("/comments/trip/{tripId}")
    List<Comment> getCommentsByTripId(@PathVariable("tripId") String tripId);

    @DeleteMapping("/comments/{id}")
    void deleteComment(@PathVariable("id") String id);
}


