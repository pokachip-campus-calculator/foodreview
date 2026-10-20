package foodreview.dao;

import foodreview.dto.MemberDTO;
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

    /** 로그인 성공 시 회원 정보, 실패하면 null. 탈퇴한 회원은 로그인할 수 없다. (F-02) */
    public MemberDTO login(String loginId, String password) throws SQLException {
        String sql = "SELECT member_id, login_id, nickname, created_at FROM member"
                + " WHERE login_id = ? AND password_hash = ? AND withdrawn_at IS NULL";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, loginId);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                MemberDTO member = new MemberDTO();
                member.setMemberId(rs.getInt("member_id"));
                member.setLoginId(rs.getString("login_id"));
                member.setNickname(rs.getString("nickname"));
                member.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return member;
            }
        }
    }

    /** 닉네임 변경. (F-03) */
    public void updateNickname(int memberId, String nickname) throws SQLException {
        update("UPDATE member SET nickname = ? WHERE member_id = ?", nickname, memberId);
    }

    /** 비밀번호 변경. (F-03) */
    public void updatePassword(int memberId, String password) throws SQLException {
        update("UPDATE member SET password_hash = ? WHERE member_id = ?", password, memberId);
    }

    /** 현재 비밀번호 확인. 비밀번호 변경·탈퇴 전에 본인인지 묻는다. (F-03) */
    public boolean matchesPassword(int memberId, String password) throws SQLException {
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pstmt = con.prepareStatement(
                     "SELECT 1 FROM member WHERE member_id = ? AND password_hash = ?")) {
            pstmt.setInt(1, memberId);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /** 탈퇴. 등록한 맛집·리뷰가 FK 로 묶여 있어 행을 지우지 않고 탈퇴 시각만 남긴다. (F-03) */
    public void withdraw(int memberId) throws SQLException {
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pstmt = con.prepareStatement(
                     "UPDATE member SET withdrawn_at = NOW() WHERE member_id = ?")) {
            pstmt.setInt(1, memberId);
            pstmt.executeUpdate();
        }
    }

    private void update(String sql, String value, int memberId) throws SQLException {
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, value);
            pstmt.setInt(2, memberId);
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
