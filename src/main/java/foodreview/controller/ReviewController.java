package foodreview.controller;

import foodreview.dto.ReviewDTO;
import foodreview.service.ReviewService;
import foodreview.service.ReviewService.MyReviewsResult;
import foodreview.service.ReviewService.WriteResult;
import foodreview.util.LoginSession;
import foodreview.view.ReviewInputView;
import foodreview.view.ReviewOutputView;

import java.util.List;

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

    /** F-09 내 리뷰 수정 / 삭제 */
    public void manage() {
        int memberId = LoginSession.getMemberId();
        MyReviewsResult myResult = reviewService.getMyReviews(memberId);
        if (myResult.dbError()) {
            outputView.printDbError();
            return;
        }
        List<ReviewDTO> myReviews = myResult.reviews();
        if (myReviews.isEmpty()) {
            outputView.printNoMyReviews();
            return;
        }
        outputView.printMyReviews(myReviews);

        int reviewId = inputView.readReviewId();
        if (reviewService.findInMyReviews(myReviews, reviewId) == null) {
            outputView.printNotMyReview();
            return;
        }

        switch (inputView.readManageAction()) {
            case 1 -> {
                ReviewDTO edited = inputView.readEditedReview();
                outputView.printUpdateResult(
                        reviewService.updateReview(memberId, reviewId, edited.getRating(), edited.getContent()));
            }
            case 2 -> {
                if (inputView.confirmDelete()) {
                    outputView.printDeleteResult(reviewService.deleteReview(memberId, reviewId));
                } else {
                    outputView.printCancelled();
                }
            }
            default -> outputView.printCancelled();
        }
    }

    /** F-07 리뷰 작성 */
    public void write() {
        ReviewDTO review = inputView.readNewReview();
        review.setMemberId(LoginSession.getMemberId());
        WriteResult result = reviewService.writeReview(review);
        outputView.printWriteResult(result);
    }
}
