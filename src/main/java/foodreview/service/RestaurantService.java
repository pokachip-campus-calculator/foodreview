package foodreview.service;

import foodreview.dao.RestaurantDAO;
import foodreview.dto.RestaurantDTO;

import java.sql.SQLException;

/** 맛집 업무 규칙. 입력 검증을 하고 DAO 를 호출한다. */
public class RestaurantService {

    private final RestaurantDAO restaurantDAO = new RestaurantDAO();

    /**
     * 맛집 등록 (F-04).
     *
     * @throws IllegalArgumentException 이름·주소·지역·카테고리 중 빈 값이 있을 때
     * @throws SQLException             DB 오류
     */
    public void register(int memberId, String name, String address, String region, String category)
            throws SQLException {
        if (isBlank(name) || isBlank(address) || isBlank(region) || isBlank(category)) {
            throw new IllegalArgumentException("입력값을 확인해 주세요");
        }

        RestaurantDTO restaurant = new RestaurantDTO();
        restaurant.setMemberId(memberId);
        restaurant.setName(name.trim());
        restaurant.setAddress(address.trim());
        restaurant.setRegion(region.trim());
        restaurant.setCategory(category.trim());
        restaurantDAO.insert(restaurant);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
