package main.repository;

import main.model.VisitItemLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface VisitItemLikeRepository extends JpaRepository<VisitItemLike, String> {

    Optional<VisitItemLike> findByVisitItemIdAndUserId(String visitItemId, String userId);

    long countByVisitItemId(String visitItemId);

    List<VisitItemLike> findAllByVisitItemId(String visitItemId);
}


