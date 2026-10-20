package foodreview.controller;

import foodreview.dto.ReviewDTO;
import foodreview.service.ReviewService;
import foodreview.service.ReviewService.WriteResult;
import foodreview.util.LoginSession;
import foodreview.view.ReviewInputView;
import foodreview.view.ReviewOutputView;

/** 리뷰 기능의 흐름 제어. View 와 Service 를 연결한다. */
public class ReviewController {

    private final ReviewService reviewService = new ReviewService();
    private final ReviewInputView inputView = new ReviewInputView();
    private final ReviewOutputView outputView = new ReviewOutputView();

    /** F-08 맛집 상세 조회 */
    public void detail() {
        int restaurantId = inputView.readRestaurantId();
        outputView.printDetail(reviewService.getDetail(restaurantId));
    }

    /** F-07 리뷰 작성 */
    public void write() {
        ReviewDTO review = inputView.readNewReview();
        review.setMemberId(LoginSession.getMemberId());
        WriteResult result = reviewService.writeReview(review);
        outputView.printWriteResult(result);
    }
}
