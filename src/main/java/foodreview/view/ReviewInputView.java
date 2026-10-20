package foodreview.view;

import foodreview.dto.ReviewDTO;
import foodreview.util.ConsoleInput;

/** 리뷰 관련 입력. SQL 과 업무 판단은 하지 않는다. */
public class ReviewInputView {

    /** 가게 번호, 별점, 내용을 입력받아 DTO 로 반환한다. (작성자는 Controller 가 채운다) */
    public ReviewDTO readNewReview() {
        ReviewDTO review = new ReviewDTO();
        review.setRestaurantId(ConsoleInput.readInt("가게 번호"));
        review.setRating(ConsoleInput.readInt("별점(1~5)"));
        review.setContent(ConsoleInput.readLine("리뷰 내용"));
        return review;
    }

    /** 조회할 가게 번호 입력. 숫자가 아니면 ConsoleInput 이 다시 묻는다. */
    public int readRestaurantId() {
        return ConsoleInput.readInt("가게 번호");
    }
}
