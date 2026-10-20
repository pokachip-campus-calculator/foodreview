package foodreview.dto;

import java.time.LocalDateTime;

/**
 * restaurant 테이블 한 행.
 * avgRating, reviewCount 는 컬럼이 아니라 목록·상세 조회의 집계 결과(O-03, F-08)를 담는다.
 * 리뷰가 없으면 avgRating 은 null 이다.
 */
public class RestaurantDTO {

    private int restaurantId;
    private int memberId;
    private String name;
    private String address;
    private String region;
    private String category;
    private LocalDateTime createdAt;

    private Double avgRating;
    private int reviewCount;

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }
}
