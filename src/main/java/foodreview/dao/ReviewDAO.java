package foodreview.dao;

import foodreview.dto.ReviewDTO;
import foodreview.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** review 테이블 SQL 실행. 출력과 업무 판단은 하지 않는다. */
public class ReviewDAO {

    /** 해당 회원이 해당 가게에 쓴 리뷰가 있는지 확인한다. */
    public boolean existsByMemberAndRestaurant(int memberId, int restaurantId) throws SQLException {
        String sql = "SELECT 1 FROM review WHERE member_id = ? AND restaurant_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            pstmt.setInt(2, restaurantId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /** 리뷰를 저장한다. FK·UNIQUE 위반은 SQLException 으로 올린다. */
    public void insert(ReviewDTO review) throws SQLException {
        String sql = "INSERT INTO review (restaurant_id, member_id, rating, content) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, review.getRestaurantId());
            pstmt.setInt(2, review.getMemberId());
            pstmt.setInt(3, review.getRating());
            pstmt.setString(4, review.getContent());
            pstmt.executeUpdate();
        }
    }
}
