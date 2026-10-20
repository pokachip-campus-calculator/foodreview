package foodreview;

import foodreview.controller.MainController;

/** 프로그램 시작점. */
public class Main {

    public static void main(String[] args) {
        MainController controller = new MainController();
        while (controller.run()) {
            // 0번(종료)을 고를 때까지 메뉴를 반복한다.
        }
    }
}
