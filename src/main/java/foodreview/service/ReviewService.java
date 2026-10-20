package foodreview.service;

import foodreview.dao.ReviewDAO;
import foodreview.dto.RestaurantDTO;
import foodreview.dto.ReviewDTO;

import java.sql.SQLException;
import java.util.List;

/** 리뷰 업무 규칙. 입출력은 하지 않는다. */
public class ReviewService {

    /** 리뷰 작성 결과 */
    public enum WriteResult {
        SUCCESS, INVALID_INPUT, INVALID_RATING, ALREADY_REVIEWED, RESTAURANT_NOT_FOUND, DB_ERROR
    }

    /** 상세 조회 결과 상태 */
    public enum DetailStatus {
        SUCCESS, NOT_FOUND, DB_ERROR
    }

    /** 상세 조회 결과. SUCCESS 일 때만 restaurant, reviews 를 쓴다. */
    public record DetailResult(DetailStatus status, RestaurantDTO restaurant, List<ReviewDTO> reviews) {
    }

    /** 내 리뷰 목록 조회 결과. dbError 가 true 면 reviews 는 비어 있다. */
    public record MyReviewsResult(boolean dbError, List<ReviewDTO> reviews) {
    }

    /** 리뷰 수정 결과 */
    public enum UpdateResult {
        SUCCESS, INVALID_INPUT, INVALID_RATING, NOT_FOUND, DB_ERROR
    }

    /** 리뷰 삭제 결과 */
    public enum DeleteResult {
        SUCCESS, NOT_FOUND, DB_ERROR
    }

    private static final int DETAIL_REVIEW_LIMIT = 10;
    private static final int ERR_DUPLICATE = 1062;
    private static final int ERR_NO_PARENT = 1452;

    private final ReviewDAO reviewDAO = new ReviewDAO();

    /** F-07 리뷰 작성 */
    public WriteResult writeReview(ReviewDTO review) {
        if (review.getRating() < 1 || review.getRating() > 5) {
            return WriteResult.INVALID_RATING;
        }
        if (review.getContent() == null || review.getContent().isBlank()) {
            return WriteResult.INVALID_INPUT;
        }
        try {
            if (reviewDAO.existsByMemberAndRestaurant(review.getMemberId(), review.getRestaurantId())) {
                return WriteResult.ALREADY_REVIEWED;
            }
            reviewDAO.insert(review);
            return WriteResult.SUCCESS;
        } catch (SQLException e) {
            return switch (e.getErrorCode()) {
                case ERR_NO_PARENT -> WriteResult.RESTAURANT_NOT_FOUND;
                case ERR_DUPLICATE -> WriteResult.ALREADY_REVIEWED;
                default -> WriteResult.DB_ERROR;
            };
        }
    }

    /** F-08 맛집 상세 조회: 가게 정보 + 평균 별점·리뷰 수 + 최신 리뷰 10개 */
    public DetailResult getDetail(int restaurantId) {
        try {
            RestaurantDTO restaurant = reviewDAO.findRestaurantWithStats(restaurantId);
            if (restaurant == null) {
                return new DetailResult(DetailStatus.NOT_FOUND, null, null);
            }
            List<ReviewDTO> reviews = reviewDAO.findRecentByRestaurant(restaurantId, DETAIL_REVIEW_LIMIT);
            return new DetailResult(DetailStatus.SUCCESS, restaurant, reviews);
        } catch (SQLException e) {
            return new DetailResult(DetailStatus.DB_ERROR, null, null);
        }
    }

    /** F-09 내가 쓴 리뷰 목록 조회 */
    public MyReviewsResult getMyReviews(int memberId) {
        try {
            return new MyReviewsResult(false, reviewDAO.findByMember(memberId));
        } catch (SQLException e) {
            return new MyReviewsResult(true, List.of());
        }
    }

    /** 입력한 번호가 내 리뷰 목록에 있으면 그 리뷰를, 없으면 null 을 반환한다. */
    public ReviewDTO findInMyReviews(List<ReviewDTO> myReviews, int reviewId) {
        for (ReviewDTO review : myReviews) {
            if (review.getReviewId() == reviewId) {
                return review;
            }
        }
        return null;
    }

    /** F-09 내 리뷰 수정 */
    public UpdateResult updateReview(int memberId, int reviewId, int rating, String content) {
        if (rating < 1 || rating > 5) {
            return UpdateResult.INVALID_RATING;
        }
        if (content == null || content.isBlank()) {
            return UpdateResult.INVALID_INPUT;
        }
        try {
            return reviewDAO.update(reviewId, memberId, rating, content) > 0
                    ? UpdateResult.SUCCESS : UpdateResult.NOT_FOUND;
        } catch (SQLException e) {
            return UpdateResult.DB_ERROR;
        }
    }

    /** F-09 내 리뷰 삭제 */
    public DeleteResult deleteReview(int memberId, int reviewId) {
        try {
            return reviewDAO.delete(reviewId, memberId) > 0
                    ? DeleteResult.SUCCESS : DeleteResult.NOT_FOUND;
        } catch (SQLException e) {
            return DeleteResult.DB_ERROR;
        }
    }
}
