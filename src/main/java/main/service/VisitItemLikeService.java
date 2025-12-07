package main.service;

import lombok.RequiredArgsConstructor;
import main.model.VisitItemLike;
import main.repository.VisitItemLikeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VisitItemLikeService {

    private final VisitItemLikeRepository likeRepository;

    public VisitItemLike toggleLike(String visitItemId, String userId) {
        Optional<VisitItemLike> existing = likeRepository.findByVisitItemIdAndUserId(visitItemId, userId);

        if (existing.isPresent()) {
            VisitItemLike like = existing.get();
            like.setLiked(!like.isLiked());
            return likeRepository.save(like);
        }

        VisitItemLike newLike = VisitItemLike.builder()
                .visitItemId(visitItemId)
                .userId(userId)
                .liked(true)
                .build();

        return likeRepository.save(newLike);
    }

    public long getLikeCount(String visitItemId) {
        return likeRepository.countByVisitItemId(visitItemId);
    }

    public boolean isLikedByUser(String visitItemId, String userId) {
        return likeRepository.findByVisitItemIdAndUserId(visitItemId, userId)
                .map(VisitItemLike::isLiked)
                .orElse(false);
    }

    public List<VisitItemLike> getLikesForItem(String visitItemId) {
        return likeRepository.findAllByVisitItemId(visitItemId);
    }
}

