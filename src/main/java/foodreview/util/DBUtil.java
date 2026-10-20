package foodreview.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/** db.properties 를 읽어 MySQL Connection 을 발급한다. */
public class DBUtil {

    private static final Properties props = new Properties();

    static {
        try (FileInputStream in = new FileInputStream("db.properties")) {
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException(
                    "db.properties 를 읽을 수 없습니다. db.properties.example 을 복사해 만드세요.", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password"));
    }

    /** 연결 확인용. 단독 실행하면 DB 접속 여부를 알려준다. */
    public static void main(String[] args) {
        try (Connection con = getConnection()) {
            System.out.println("DB 연결 성공: " + con.getCatalog());
        } catch (SQLException e) {
            System.out.println("DB 연결 실패: " + e.getMessage());
        }
    }
}
