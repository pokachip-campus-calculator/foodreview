package foodreview.view;

import foodreview.dto.RestaurantDTO;
import foodreview.dto.ReviewDTO;
import foodreview.service.ReviewService.DeleteResult;
import foodreview.service.ReviewService.DetailResult;
import foodreview.service.ReviewService.UpdateResult;
import foodreview.service.ReviewService.WriteResult;

import java.time.format.DateTimeFormatter;
import java.util.List;

/** 리뷰 관련 출력. DB 접근은 하지 않는다. */
public class ReviewOutputView {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /** F-09 내 리뷰 목록 출력 */
    public void printMyReviews(List<ReviewDTO> reviews) {
        System.out.println("\n--- 내 리뷰 ---");
        for (ReviewDTO review : reviews) {
            System.out.println("[" + review.getReviewId() + "] " + review.getRestaurantName()
                    + " ★" + review.getRating() + " (" + review.getCreatedAt().format(DATE_FORMAT) + ")");
            System.out.println("    " + review.getContent());
        }
    }

    public void printNoMyReviews() {
        System.out.println("작성한 리뷰가 없습니다");
    }

    public void printNotMyReview() {
        System.out.println("본인 리뷰만 선택할 수 있습니다");
    }

    public void printCancelled() {
        System.out.println("취소되었습니다");
    }

    public void printDbError() {
        System.out.println("DB 오류가 발생했습니다");
    }

    public void printUpdateResult(UpdateResult result) {
        switch (result) {
            case SUCCESS -> System.out.println("수정 완료");
            case INVALID_INPUT -> System.out.println("입력값을 확인해 주세요");
            case INVALID_RATING -> System.out.println("별점은 1~5의 정수만 입력할 수 있습니다");
            case NOT_FOUND -> System.out.println("본인 리뷰만 선택할 수 있습니다");
            case DB_ERROR -> printDbError();
        }
    }

    public void printDeleteResult(DeleteResult result) {
        switch (result) {
            case SUCCESS -> System.out.println("삭제 완료");
            case NOT_FOUND -> System.out.println("본인 리뷰만 선택할 수 있습니다");
            case DB_ERROR -> printDbError();
        }
    }

    /** F-08 맛집 상세 조회 결과 출력 */
    public void printDetail(DetailResult result) {
        switch (result.status()) {
            case NOT_FOUND -> System.out.println("존재하지 않는 가게입니다");
            case DB_ERROR -> System.out.println("DB 오류가 발생했습니다");
            case SUCCESS -> {
                printRestaurant(result.restaurant());
                printReviews(result.reviews());
            }
        }
    }

    private void printRestaurant(RestaurantDTO restaurant) {
        System.out.println("\n===== " + restaurant.getName() + " =====");
        System.out.println("카테고리: " + orDash(restaurant.getCategory()));
        System.out.println("지역: " + orDash(restaurant.getRegion()));
        System.out.println("주소: " + restaurant.getAddress());
        if (restaurant.getAvgRating() == null) {
            System.out.println("평점 없음");
        } else {
            System.out.printf("평균 ★%.1f (리뷰 %d개)%n", restaurant.getAvgRating(), restaurant.getReviewCount());
        }
    }

    private void printReviews(List<ReviewDTO> reviews) {
        System.out.println("\n--- 리뷰 ---");
        if (reviews.isEmpty()) {
            System.out.println("작성된 리뷰가 없습니다");
            return;
        }
        for (ReviewDTO review : reviews) {
            System.out.println("[" + review.getNickname() + "] ★" + review.getRating()
                    + " (" + review.getCreatedAt().format(DATE_FORMAT) + ")");
            System.out.println("  " + review.getContent());
        }
    }

    private String orDash(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }

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
