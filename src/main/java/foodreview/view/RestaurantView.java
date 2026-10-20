package foodreview.view;

import foodreview.dto.RestaurantDTO;
import foodreview.service.RestaurantService;
import foodreview.util.ConsoleInput;
import foodreview.util.LoginSession;

import java.sql.SQLException;
import java.util.List;

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

    /** 맛집 목록 조회 (F-05) */
    public void list() {
        System.out.println("\n--- 맛집 목록 ---");
        try {
            List<RestaurantDTO> restaurantList = restaurantService.getRestaurantList();
            if (restaurantList.isEmpty()) {
                System.out.println("등록된 맛집이 없습니다");
                return;
            }
            for (RestaurantDTO restaurant : restaurantList) {
                System.out.println(restaurant.getRestaurantId() + ". " + restaurant.getName()
                        + " | " + restaurant.getCategory() + " | " + orDash(restaurant.getRegion()));
            }
        } catch (SQLException e) {
            System.out.println("DB 오류가 발생했습니다");
        }
    }

    /** region 은 NULL 일 수 있어 비어 있으면 "-" 로 표시한다. */
    private String orDash(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }

    /** 카테고리를 번호로 고르게 한다. (O-08) */
    private String selectCategory() {
        for (int i = 0; i < CATEGORIES.length; i++) {
            System.out.println((i + 1) + ". " + CATEGORIES[i]);
        }
        return CATEGORIES[ConsoleInput.readInt("카테고리 번호", 1, CATEGORIES.length) - 1];
    }
}
