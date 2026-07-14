package com.frankmoley.lil.reviewratingservice.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.frankmoley.lil.reviewratingservice.data.Review;
import com.frankmoley.lil.reviewratingservice.data.ReviewRepository;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewRepository reviewRepository;

    public ReviewController(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @GetMapping
    public Iterable<Review> getReviews() {
        return this.reviewRepository.findAll();
    }
    @PostMapping
    public Review addReview(@RequestBody Review review) {
        return this.reviewRepository.save(review);
    }
}