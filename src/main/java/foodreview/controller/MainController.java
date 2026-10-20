package foodreview.controller;

import foodreview.util.ConsoleInput;
import foodreview.util.LoginSession;
import foodreview.view.RestaurantView;
import foodreview.view.MemberView;

/**
 * 콘솔 메뉴를 출력하고 각 담당자의 View 로 넘긴다. (기획서 6. 콘솔 메뉴 구조)
 * 담당자는 자기 기능의 [TODO] 줄만 자기 View 호출로 바꾼다.
 */
public class MainController {

    private final ReviewController reviewController = new ReviewController();
    private final MemberView memberView = new MemberView();

    /** 로그인 상태에 따라 메뉴를 분기한다. false 면 프로그램 종료. */
    public boolean run() {
        return LoginSession.isLoggedIn() ? mainMenu() : guestMenu();
    }

    /** 로그인 전 메뉴 */
    private boolean guestMenu() {
        System.out.println("\n=== 맛집로그 ===");
        System.out.println("1. 회원 가입");
        System.out.println("2. 로그인");
        System.out.println("0. 종료");
        switch (ConsoleInput.readInt("선택")) {
            case 1 -> memberView.join();                                      // F-01
            case 2 -> memberView.login();                                     // F-02
            case 0 -> {
                System.out.println("프로그램을 종료합니다.");
                return false;
            }
            default -> System.out.println("잘못된 선택입니다.");
        }
        return true;
    }

    /** 로그인 후 메인 메뉴 */
    private boolean mainMenu() {
        System.out.println("\n=== 메인 메뉴 === (" + LoginSession.getNickname() + " 님)");
        System.out.println("1. 맛집 관리");
        System.out.println("2. 리뷰 관리");
        System.out.println("3. 내 정보 수정 / 탈퇴");
        System.out.println("4. 로그아웃");
        System.out.println("0. 종료");
        switch (ConsoleInput.readInt("선택")) {
            case 1 -> restaurantMenu();
            case 2 -> reviewMenu();
            case 3 -> memberView.myPage();                                    // F-03
            case 4 -> {
                LoginSession.logout();
                System.out.println("로그아웃되었습니다.");
            }
            case 0 -> {
                System.out.println("프로그램을 종료합니다.");
                return false;
            }
            default -> System.out.println("잘못된 선택입니다.");
        }
        return true;
    }

    private final RestaurantView restaurantView = new RestaurantView();

    /** 1. 맛집 관리 */
    private void restaurantMenu() {
        System.out.println("\n--- 맛집 관리 ---");
        System.out.println("1. 맛집 목록 조회");
        System.out.println("2. 맛집 상세 조회");
        System.out.println("3. 맛집 등록");
        System.out.println("4. 내 맛집 수정 / 삭제");
        System.out.println("0. 뒤로");
        switch (ConsoleInput.readInt("선택")) {
            case 1 -> System.out.println("[TODO:B] RestaurantView.list()");   // F-05
            case 2 -> System.out.println("[TODO:C] ReviewView.detail()");     // F-08
            case 3 -> restaurantView.add();                                   // F-04
            case 4 -> System.out.println("[TODO:B] RestaurantView.manage()"); // F-06
            case 0 -> { }
            default -> System.out.println("잘못된 선택입니다.");
        }
    }

    /** 2. 리뷰 관리 */
    private void reviewMenu() {
        System.out.println("\n--- 리뷰 관리 ---");
        System.out.println("1. 리뷰 작성");
        System.out.println("2. 내 리뷰 수정 / 삭제");
        System.out.println("0. 뒤로");
        switch (ConsoleInput.readInt("선택")) {
            case 1 -> reviewController.write();                               // F-07
            case 2 -> System.out.println("[TODO:C] ReviewView.manage()");     // F-09
            case 0 -> { }
            default -> System.out.println("잘못된 선택입니다.");
        }
    }
}
