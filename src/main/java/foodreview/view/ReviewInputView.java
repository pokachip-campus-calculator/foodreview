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

    /** 수정·삭제할 리뷰 번호 입력 */
    public int readReviewId() {
        return ConsoleInput.readInt("수정/삭제할 리뷰 번호");
    }

    /** 1. 수정 2. 삭제 0. 취소 */
    public int readManageAction() {
        System.out.println("1. 수정  2. 삭제  0. 취소");
        return ConsoleInput.readInt("선택", 0, 2);
    }

    /** 수정할 별점과 내용을 입력받아 DTO 로 반환한다. */
    public ReviewDTO readEditedReview() {
        ReviewDTO review = new ReviewDTO();
        review.setRating(ConsoleInput.readInt("새 별점(1~5)"));
        review.setContent(ConsoleInput.readLine("새 리뷰 내용"));
        return review;
    }

    /** 삭제 확인. Y/y 만 true. */
    public boolean confirmDelete() {
        return ConsoleInput.confirm("정말 삭제할까요?");
    }

    /** 조회할 가게 번호 입력. 숫자가 아니면 ConsoleInput 이 다시 묻는다. */
    public int readRestaurantId() {
        return ConsoleInput.readInt("가게 번호");
    }
}
