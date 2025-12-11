package main.service;

import main.model.VisitItemLike;
import main.repository.VisitItemLikeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VisitItemLikeServiceTest {

    private VisitItemLikeRepository likeRepository;
    private VisitItemLikeService sut;

    @BeforeEach
    void setup() {
        likeRepository = mock(VisitItemLikeRepository.class);
        sut = new VisitItemLikeService(likeRepository);
    }

    // ---------------------------------------------------------
    // toggleLike()
    // ---------------------------------------------------------
    @Test
    void toggleLike_newLike_createsLike() {
        String visitItemId = "item1";
        String userId = "user1";

        when(likeRepository.findByVisitItemIdAndUserId(visitItemId, userId)).thenReturn(Optional.empty());

        VisitItemLike savedLike = VisitItemLike.builder()
                .visitItemId(visitItemId)
                .userId(userId)
                .liked(true)
                .build();

        when(likeRepository.save(any(VisitItemLike.class))).thenReturn(savedLike);

        VisitItemLike result = sut.toggleLike(visitItemId, userId);

        assertTrue(result.isLiked());
        assertEquals(visitItemId, result.getVisitItemId());
        assertEquals(userId, result.getUserId());
        verify(likeRepository).save(any(VisitItemLike.class));
    }

    @Test
    void toggleLike_existingLike_togglesLiked() {
        String visitItemId = "item1";
        String userId = "user1";

        VisitItemLike existing = VisitItemLike.builder()
                .visitItemId(visitItemId)
                .userId(userId)
                .liked(true)
                .build();

        when(likeRepository.findByVisitItemIdAndUserId(visitItemId, userId)).thenReturn(Optional.of(existing));
        when(likeRepository.save(existing)).thenReturn(existing);

        VisitItemLike result = sut.toggleLike(visitItemId, userId);

        assertFalse(result.isLiked()); // toggled
        verify(likeRepository).save(existing);
    }

    // ---------------------------------------------------------
    // getLikeCount()
    // ---------------------------------------------------------
    @Test
    void getLikeCount_returnsCorrectCount() {
        String visitItemId = "item1";
        when(likeRepository.countByVisitItemIdAndLikedTrue(visitItemId)).thenReturn(5L);

        long count = sut.getLikeCount(visitItemId);

        assertEquals(5L, count);
        verify(likeRepository).countByVisitItemIdAndLikedTrue(visitItemId);
    }

    // ---------------------------------------------------------
    // isLikedByUser()
    // ---------------------------------------------------------
    @Test
    void isLikedByUser_returnsTrueIfLiked() {
        String visitItemId = "item1";
        String userId = "user1";

        VisitItemLike like = VisitItemLike.builder().liked(true).build();
        when(likeRepository.findByVisitItemIdAndUserId(visitItemId, userId)).thenReturn(Optional.of(like));

        boolean result = sut.isLikedByUser(visitItemId, userId);

        assertTrue(result);
    }

    @Test
    void isLikedByUser_returnsFalseIfNotLiked() {
        String visitItemId = "item1";
        String userId = "user1";

        when(likeRepository.findByVisitItemIdAndUserId(visitItemId, userId)).thenReturn(Optional.empty());

        boolean result = sut.isLikedByUser(visitItemId, userId);

        assertFalse(result);
    }

    @Test
    void isLikedByUser_returnsFalseIfLikedFalse() {
        String visitItemId = "item1";
        String userId = "user1";

        VisitItemLike like = VisitItemLike.builder().liked(false).build();
        when(likeRepository.findByVisitItemIdAndUserId(visitItemId, userId)).thenReturn(Optional.of(like));

        boolean result = sut.isLikedByUser(visitItemId, userId);

        assertFalse(result);
    }

    // ---------------------------------------------------------
    // getLikesForItem()
    // ---------------------------------------------------------
    @Test
    void getLikesForItem_returnsList() {
        String visitItemId = "item1";

        VisitItemLike like1 = VisitItemLike.builder().build();
        VisitItemLike like2 = VisitItemLike.builder().build();
        when(likeRepository.findAllByVisitItemId(visitItemId)).thenReturn(List.of(like1, like2));

        List<VisitItemLike> result = sut.getLikesForItem(visitItemId);

        assertEquals(2, result.size());
        verify(likeRepository).findAllByVisitItemId(visitItemId);
    }
}

