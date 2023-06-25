package com.project.movies.movies.api.service;

import com.project.movies.movies.api.repo.ReviewRepository;
import com.project.movies.movies.api.model.Movie;
import com.project.movies.movies.api.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Review createReview(String reviewBody, String imdbId) {
        Review review = repository.insert(new Review(reviewBody, LocalDateTime.now(), LocalDateTime.now()));

        mongoTemplate.update(Movie.class)
                .matching(Criteria.where("imdbId").is(imdbId))
                .apply(new Update().push("reviews").value(review))
                .first();

        return review;
    }
}
