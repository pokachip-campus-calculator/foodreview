package foodreview.service;

import foodreview.dao.RestaurantDAO;
import foodreview.dto.RestaurantDTO;

import java.sql.SQLException;
import java.util.List;

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

    /** 맛집 목록 조회 (F-05). 등록순. */
    public List<RestaurantDTO> getRestaurantList() throws SQLException {
        return restaurantDAO.findAll();
    }

    /** 내가 등록한 맛집 목록 (F-06). */
    public List<RestaurantDTO> getMyRestaurantList(int memberId) throws SQLException {
        return restaurantDAO.findByMemberId(memberId);
    }

    /**
     * 내 맛집 수정 (F-06).
     *
     * @throws IllegalArgumentException 내 맛집이 아니거나 빈 값이 있을 때
     * @throws SQLException             DB 오류
     */
    public void updateMine(int memberId, int restaurantId, String name, String address,
                           String region, String category) throws SQLException {
        checkMine(memberId, restaurantId);
        if (isBlank(name) || isBlank(address) || isBlank(region) || isBlank(category)) {
            throw new IllegalArgumentException("입력값을 확인해 주세요");
        }

        RestaurantDTO restaurant = new RestaurantDTO();
        restaurant.setRestaurantId(restaurantId);
        restaurant.setMemberId(memberId);
        restaurant.setName(name.trim());
        restaurant.setAddress(address.trim());
        restaurant.setRegion(region.trim());
        restaurant.setCategory(category.trim());
        restaurantDAO.update(restaurant);
    }

    /**
     * 내 맛집 삭제 (F-06). 달린 리뷰도 함께 삭제된다.
     *
     * @throws IllegalArgumentException 내 맛집이 아닐 때
     * @throws SQLException             DB 오류
     */
    public void deleteMine(int memberId, int restaurantId) throws SQLException {
        checkMine(memberId, restaurantId);
        restaurantDAO.delete(restaurantId, memberId);
    }

    /**
     * 입력한 번호가 내가 등록한 맛집인지 확인한다.
     *
     * @throws IllegalArgumentException 내 맛집이 아닐 때
     */
    public void checkMine(int memberId, int restaurantId) throws SQLException {
        for (RestaurantDTO restaurant : restaurantDAO.findByMemberId(memberId)) {
            if (restaurant.getRestaurantId() == restaurantId) {
                return;
            }
        }
        throw new IllegalArgumentException("본인이 등록한 가게만 선택할 수 있습니다");
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
