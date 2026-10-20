package foodreview.view;

import foodreview.service.RestaurantService;
import foodreview.util.ConsoleInput;
import foodreview.util.LoginSession;

import java.sql.SQLException;

/** 맛집 관련 입력과 출력을 담당한다. (F-04 ~ F-06) */
public class RestaurantView {

    private static final String[] CATEGORIES = {"한식", "중식", "일식", "양식", "기타"};

    private final RestaurantService restaurantService = new RestaurantService();

    /** 맛집 등록 (F-04) */
    public void add() {
        System.out.println("\n--- 맛집 등록 ---");
        String name = ConsoleInput.readLine("가게 이름");
        String address = ConsoleInput.readLine("주소");
        String region = ConsoleInput.readLine("지역");
        String category = selectCategory();

        try {
            restaurantService.register(LoginSession.getMemberId(), name, address, region, category);
            System.out.println("맛집 등록 완료");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println("DB 오류가 발생했습니다");
        }
    }

    /** 카테고리를 번호로 고르게 한다. (O-08) */
    private String selectCategory() {
        for (int i = 0; i < CATEGORIES.length; i++) {
            System.out.println((i + 1) + ". " + CATEGORIES[i]);
        }
        return CATEGORIES[ConsoleInput.readInt("카테고리 번호", 1, CATEGORIES.length) - 1];
    }
}
