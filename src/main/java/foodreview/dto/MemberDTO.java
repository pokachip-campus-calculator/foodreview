package foodreview.dto;

import java.time.LocalDateTime;

/**
 * member 테이블 한 행. (탈퇴는 하드 삭제라 withdrawn_at 은 담지 않는다.)
 * 비밀번호는 평문 저장이지만 컬럼명은 password_hash 이다.
 */
public class MemberDTO {

    private int memberId;
    private String loginId;
    private String passwordHash;
    private String nickname;
    private LocalDateTime createdAt;

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
