package com.frankmoley.lil.reviewratingservice.data;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reviews")
public class Review {
    @Id
    private String id;
    private String roomId;
    private String guestId;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Review(String roomId, String guestId, int rating, String comment) {
        this.roomId = roomId;
        this.guestId = guestId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    public Review(String id, String roomId, String guestId, int rating, String comment, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.roomId = roomId;
        this.guestId = guestId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }   
    public String getId() {
        return id;
    }
    public String getRoomId() {
        return roomId;
    }
    public String getGuestId() {
        return guestId;
    }
    public int getRating() {
        return rating;
    }
    public String getComment() {
        return comment;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    @Override
    public String toString() {
        return "Review{" +
                "id='" + id + '\'' +
                ", roomId='" + roomId + '\'' +
                ", guestId='" + guestId + '\'' +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return rating == review.rating && Objects.equals(id, review.id) && Objects.equals(roomId, review.roomId) && Objects.equals(guestId, review.guestId) && Objects.equals(comment, review.comment) && Objects.equals(createdAt, review.createdAt) && Objects.equals(updatedAt, review.updatedAt);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, roomId, guestId, rating, comment, createdAt, updatedAt);
    }
}
