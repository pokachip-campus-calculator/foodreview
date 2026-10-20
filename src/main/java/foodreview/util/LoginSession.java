package foodreview.util;

/**
 * 로그인한 회원 정보를 보관한다. (F-02 로그인 / 로그아웃)
 * 닉네임 수정(F-03) 시 updateNickname 으로 갱신한다.
 */
public class LoginSession {

    private static int memberId;
    private static String nickname;

    public static void login(int id, String name) {
        memberId = id;
        nickname = name;
    }

    public static void logout() {
        memberId = 0;
        nickname = null;
    }

    public static void updateNickname(String name) {
        nickname = name;
    }

    public static boolean isLoggedIn() {
        return memberId != 0;
    }

    public static int getMemberId() {
        return memberId;
    }

    public static String getNickname() {
        return nickname;
    }
}
