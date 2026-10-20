package foodreview.view;

import foodreview.dao.MemberDAO;
import foodreview.util.ConsoleInput;

import java.sql.SQLException;

/** 회원 기능 화면. (담당: A) */
public class MemberView {

    private final MemberDAO dao = new MemberDAO();

    /** F-01 회원 가입 */
    public void join() {
        System.out.println("\n--- 회원 가입 ---");
        String loginId = ConsoleInput.readLine("아이디");
        String password = ConsoleInput.readLine("비밀번호");
        String nickname = ConsoleInput.readLine("닉네임");

        if (loginId.isEmpty() || password.isEmpty() || nickname.isEmpty()) {
            System.out.println("입력값을 확인해 주세요.");
            return;
        }

        try {
            if (dao.existsLoginId(loginId)) {
                System.out.println("이미 존재하는 아이디입니다.");
                return;
            }
            if (dao.existsNickname(nickname)) {
                System.out.println("이미 사용 중인 닉네임입니다.");
                return;
            }
            dao.insert(loginId, password, nickname);
            System.out.println("가입 완료되었습니다.");
        } catch (SQLException e) {
            // 중복 확인과 저장 사이에 같은 값이 들어온 경우도 중복으로 안내한다.
            if (e.getErrorCode() == MemberDAO.DUPLICATE_ENTRY) {
                System.out.println("이미 존재하는 아이디 또는 닉네임입니다.");
            } else {
                System.out.println("DB 오류가 발생했습니다.");
            }
        }
    }
}
