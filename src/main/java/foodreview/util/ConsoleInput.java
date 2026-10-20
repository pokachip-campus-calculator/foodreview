package foodreview.util;

import java.util.Scanner;

/**
 * 콘솔 입력 공통 처리. 모든 View 가 이 클래스로 입력을 받는다.
 * 숫자가 아닌 값이 들어와도 프로그램이 끝나지 않도록 재입력을 받는다. (5. 공통 예외 처리)
 */
public class ConsoleInput {

    private static final Scanner sc = new Scanner(System.in);

    /** 한 줄 입력. 앞뒤 공백은 제거한다. */
    public static String readLine(String label) {
        System.out.print(label + " > ");
        return sc.nextLine().trim();
    }

    /** 정수 입력. 숫자가 아니면 다시 묻는다. */
    public static int readInt(String label) {
        while (true) {
            try {
                return Integer.parseInt(readLine(label));
            } catch (NumberFormatException e) {
                System.out.println("숫자만 입력할 수 있습니다.");
            }
        }
    }

    /** 범위를 벗어나면 다시 묻는 정수 입력. 별점(1~5), 메뉴 번호 등에 쓴다. */
    public static int readInt(String label, int min, int max) {
        while (true) {
            int value = readInt(label);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println(min + "~" + max + " 사이의 번호를 입력해 주세요.");
        }
    }

    /** Y/y 만 true. 삭제·탈퇴 확인에 쓴다. */
    public static boolean confirm(String message) {
        return readLine(message + " (Y/N)").equalsIgnoreCase("Y");
    }
}
