package foodreview.dao;

import foodreview.dto.RestaurantDTO;
import foodreview.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
