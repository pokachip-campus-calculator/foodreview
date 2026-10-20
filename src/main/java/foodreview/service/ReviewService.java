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
}
