package com.frankmoley.lil.reviewratingservice.data;

import java.time.LocalDateTime;

import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends CrudRepository<Review, String> {
    Iterable<Review> findReviewsByRoomId(String roomId);
    Iterable<Review> findReviewsByGuestId(String guestId);
    Iterable<Review> findReviewsByRating(int rating);
    Iterable<Review> findReviewsByComment(String comment);
    Iterable<Review> findReviewsByCreatedAt(LocalDateTime createdAt);
    Iterable<Review> findReviewsByUpdatedAt(LocalDateTime updatedAt);
}


