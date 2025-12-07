package main.web;

import lombok.RequiredArgsConstructor;
import main.model.VisitItemLike;
import main.service.VisitItemLikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/visitItemLikes")
public class VisitItemLikeController {

    private final VisitItemLikeService likeService;

    public VisitItemLikeController(VisitItemLikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/toggle")
    public ResponseEntity<?> toggleLike(
            @RequestParam String visitItemId,
            @RequestParam String userId
    ) {
        VisitItemLike like = likeService.toggleLike(visitItemId, userId);

        return ResponseEntity.ok(Map.of(
                "visitItemId", visitItemId,
                "userId", userId,
                "liked", like.isLiked()
        ));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getLikeCount(@RequestParam String visitItemId) {
        long count = likeService.getLikeCount(visitItemId);
        return ResponseEntity.ok(Map.of("visitItemId", visitItemId, "count", count));
    }

    @GetMapping("/isLiked")
    public ResponseEntity<?> isLiked(
            @RequestParam String visitItemId,
            @RequestParam String userId
    ) {
        boolean liked = likeService.isLikedByUser(visitItemId, userId);
        return ResponseEntity.ok(Map.of(
                "visitItemId", visitItemId,
                "userId", userId,
                "liked", liked
        ));
    }

    @GetMapping("/all")
    public ResponseEntity<List<VisitItemLike>> getAllLikes(@RequestParam String visitItemId) {
        return ResponseEntity.ok(likeService.getLikesForItem(visitItemId));
    }
}

