package foodreview.view;

import foodreview.dao.MemberDAO;
import foodreview.dto.MemberDTO;
import foodreview.util.ConsoleInput;
import foodreview.util.LoginSession;

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

    /** F-02 로그인 */
    public void login() {
        System.out.println("\n--- 로그인 ---");
        String loginId = ConsoleInput.readLine("아이디");
        String password = ConsoleInput.readLine("비밀번호");

        try {
            MemberDTO member = dao.login(loginId, password);
            if (member == null) {
                System.out.println("아이디 또는 비밀번호가 올바르지 않습니다.");
                return;
            }
            LoginSession.login(member.getMemberId(), member.getNickname());
            System.out.println(member.getNickname() + " 님, 환영합니다.");
        } catch (SQLException e) {
            System.out.println("DB 오류가 발생했습니다.");
        }
    }

    /** F-03 내 정보 수정 / 탈퇴 */
    public void myPage() {
        System.out.println("\n--- 내 정보 (" + LoginSession.getNickname() + " 님) ---");
        System.out.println("1. 닉네임 변경");
        System.out.println("2. 비밀번호 변경");
        System.out.println("3. 회원 탈퇴");
        System.out.println("0. 뒤로");
        try {
            switch (ConsoleInput.readInt("선택")) {
                case 1 -> changeNickname();
                case 2 -> changePassword();
                case 3 -> withdraw();
                case 0 -> { }
                default -> System.out.println("잘못된 선택입니다.");
            }
        } catch (SQLException e) {
            System.out.println("DB 오류가 발생했습니다.");
        }
    }

    private void changeNickname() throws SQLException {
        String nickname = ConsoleInput.readLine("새 닉네임");
        if (nickname.isEmpty()) {
            System.out.println("입력값을 확인해 주세요.");
            return;
        }
        if (nickname.equals(LoginSession.getNickname())) {
            System.out.println("현재 닉네임과 같습니다.");
            return;
        }
        if (dao.existsNickname(nickname)) {
            System.out.println("이미 사용 중인 닉네임입니다.");
            return;
        }
        dao.updateNickname(LoginSession.getMemberId(), nickname);
        LoginSession.updateNickname(nickname);
        System.out.println("닉네임이 변경되었습니다.");
    }

    private void changePassword() throws SQLException {
        if (!checkPassword()) {
            return;
        }
        String password = ConsoleInput.readLine("새 비밀번호");
        if (password.isEmpty()) {
            System.out.println("입력값을 확인해 주세요.");
            return;
        }
        dao.updatePassword(LoginSession.getMemberId(), password);
        System.out.println("비밀번호가 변경되었습니다.");
    }

    private void withdraw() throws SQLException {
        if (!checkPassword()) {
            return;
        }
        if (!ConsoleInput.confirm("정말 탈퇴하시겠습니까? 되돌릴 수 없습니다.")) {
            System.out.println("탈퇴를 취소했습니다.");
            return;
        }
        dao.withdraw(LoginSession.getMemberId());
        LoginSession.logout();
        System.out.println("탈퇴 처리되었습니다.");
    }

    /** 본인 확인. 비밀번호가 다르면 false. */
    private boolean checkPassword() throws SQLException {
        if (dao.matchesPassword(LoginSession.getMemberId(), ConsoleInput.readLine("현재 비밀번호"))) {
            return true;
        }
        System.out.println("비밀번호가 일치하지 않습니다.");
        return false;
    }
}
