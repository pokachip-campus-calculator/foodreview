package foodreview.view;

import foodreview.service.ReviewService.WriteResult;

/** 리뷰 관련 출력. DB 접근은 하지 않는다. */
public class ReviewOutputView {

    public void printWriteResult(WriteResult result) {
        switch (result) {
            case SUCCESS -> System.out.println("리뷰 등록 완료");
            case INVALID_INPUT -> System.out.println("입력값을 확인해 주세요");
            case INVALID_RATING -> System.out.println("별점은 1~5의 정수만 입력할 수 있습니다");
            case ALREADY_REVIEWED -> System.out.println("이미 리뷰를 작성한 가게입니다");
            case RESTAURANT_NOT_FOUND -> System.out.println("존재하지 않는 가게입니다");
            case DB_ERROR -> System.out.println("DB 오류가 발생했습니다");
        }
    }
}
