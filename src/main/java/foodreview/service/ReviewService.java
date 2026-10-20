package foodreview.service;

import foodreview.dao.ReviewDAO;
import foodreview.dto.ReviewDTO;

import java.sql.SQLException;

/** 리뷰 업무 규칙. 입출력은 하지 않는다. */
public class ReviewService {

    /** 리뷰 작성 결과 */
    public enum WriteResult {
        SUCCESS, INVALID_INPUT, INVALID_RATING, ALREADY_REVIEWED, RESTAURANT_NOT_FOUND, DB_ERROR
    }

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
}
