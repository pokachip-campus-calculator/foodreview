package foodreview.dao;

import foodreview.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** member 테이블 접근. (담당: A) */
public class MemberDAO {

    /** MySQL 오류 코드: UNIQUE 제약 위반 */
    public static final int DUPLICATE_ENTRY = 1062;

    /** 회원 저장. UNIQUE 위반이면 SQLException(1062) 이 그대로 올라간다. (F-01) */
    public void insert(String loginId, String password, String nickname) throws SQLException {
        String sql = "INSERT INTO member (login_id, password_hash, nickname) VALUES (?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, loginId);
            pstmt.setString(2, password);
            pstmt.setString(3, nickname);
            pstmt.executeUpdate();
        }
    }

    public boolean existsLoginId(String loginId) throws SQLException {
        return exists("SELECT 1 FROM member WHERE login_id = ?", loginId);
    }

    public boolean existsNickname(String nickname) throws SQLException {
        return exists("SELECT 1 FROM member WHERE nickname = ?", nickname);
    }

    private boolean exists(String sql, String value) throws SQLException {
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, value);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}
