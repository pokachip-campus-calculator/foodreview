package foodreview.dao;

import foodreview.dto.RestaurantDTO;
import foodreview.dto.ReviewDTO;
import foodreview.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    /** 가게 정보와 평균 별점·리뷰 수를 한 번에 조회한다. 가게가 없으면 null. */
    public RestaurantDTO findRestaurantWithStats(int restaurantId) throws SQLException {
        String sql = "SELECT r.restaurant_id, r.member_id, r.name, r.address, r.region, r.category, r.created_at, "
                + "AVG(v.rating) AS avg_rating, COUNT(v.review_id) AS review_count "
                + "FROM restaurant r LEFT JOIN review v ON r.restaurant_id = v.restaurant_id "
                + "WHERE r.restaurant_id = ? "
                + "GROUP BY r.restaurant_id, r.member_id, r.name, r.address, r.region, r.category, r.created_at";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, restaurantId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                RestaurantDTO restaurant = new RestaurantDTO();
                restaurant.setRestaurantId(rs.getInt("restaurant_id"));
                restaurant.setMemberId(rs.getInt("member_id"));
                restaurant.setName(rs.getString("name"));
                restaurant.setAddress(rs.getString("address"));
                restaurant.setRegion(rs.getString("region"));
                restaurant.setCategory(rs.getString("category"));
                restaurant.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                restaurant.setReviewCount(rs.getInt("review_count"));
                // 리뷰가 없으면 AVG 가 NULL 이므로 avgRating 도 null 로 둔다
                restaurant.setAvgRating(rs.getObject("avg_rating") == null ? null : rs.getDouble("avg_rating"));
                return restaurant;
            }
        }
    }

    /** 가게의 리뷰를 최신순으로 조회한다. 작성자 닉네임은 member 와 JOIN 해서 가져온다. */
    public List<ReviewDTO> findRecentByRestaurant(int restaurantId, int limit) throws SQLException {
        String sql = "SELECT v.review_id, v.restaurant_id, v.member_id, v.rating, v.content, "
                + "v.created_at, v.updated_at, m.nickname "
                + "FROM review v JOIN member m ON v.member_id = m.member_id "
                + "WHERE v.restaurant_id = ? "
                + "ORDER BY v.created_at DESC, v.review_id DESC LIMIT ?";
        List<ReviewDTO> reviewList = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, restaurantId);
            pstmt.setInt(2, limit);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ReviewDTO review = new ReviewDTO();
                    review.setReviewId(rs.getInt("review_id"));
                    review.setRestaurantId(rs.getInt("restaurant_id"));
                    review.setMemberId(rs.getInt("member_id"));
                    review.setRating(rs.getInt("rating"));
                    review.setContent(rs.getString("content"));
                    review.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    review.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    review.setNickname(rs.getString("nickname"));
                    reviewList.add(review);
                }
            }
        }
        return reviewList;
    }
}
