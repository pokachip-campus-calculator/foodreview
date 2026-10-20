package foodreview.dao;

import foodreview.dto.RestaurantDTO;
import foodreview.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** restaurant 테이블 SQL 만 담당한다. 화면 출력과 업무 규칙은 두지 않는다. */
public class RestaurantDAO {

    /** 맛집 등록 (F-04). 등록자는 dto 의 memberId. */
    public void insert(RestaurantDTO restaurant) throws SQLException {
        String sql = "INSERT INTO restaurant (member_id, name, address, region, category) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, restaurant.getMemberId());
            pstmt.setString(2, restaurant.getName());
            pstmt.setString(3, restaurant.getAddress());
            pstmt.setString(4, restaurant.getRegion());
            pstmt.setString(5, restaurant.getCategory());
            pstmt.executeUpdate();
        }
    }

    /** 전체 맛집을 등록순으로 조회 (F-05) */
    public List<RestaurantDTO> findAll() throws SQLException {
        String sql = "SELECT restaurant_id, member_id, name, address, region, category, created_at "
                + "FROM restaurant ORDER BY restaurant_id";
        List<RestaurantDTO> restaurantList = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                RestaurantDTO restaurant = new RestaurantDTO();
                restaurant.setRestaurantId(rs.getInt("restaurant_id"));
                restaurant.setMemberId(rs.getInt("member_id"));
                restaurant.setName(rs.getString("name"));
                restaurant.setAddress(rs.getString("address"));
                restaurant.setRegion(rs.getString("region"));
                restaurant.setCategory(rs.getString("category"));
                restaurant.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
                restaurantList.add(restaurant);
            }
        }
        return restaurantList;
    }
}
