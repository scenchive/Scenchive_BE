package com.example.scenchive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//
@EnableJpaAuditing
@SpringBootApplication
public class ScenchiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScenchiveApplication.class, args);

//        Connection con = null;
//
//        String server = "localhost"; // MySQL 서버 주소
//        String database = "scenchive"; // MySQL DATABASE 이름
//        String user_name = "root"; //  MySQL 서버 아이디
//        String password = "0128"; // MySQL 서버 비밀번호
//
//        // 1.드라이버 로딩
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//            System.err.println(" !! <JDBC 오류> Driver load 오류: " + e.getMessage());
//            e.printStackTrace();
//        }
//
//        // 2.연결
//        try {
//            con = DriverManager.getConnection("jdbc:mysql://" + server + "/" + database + "?useSSL=false", user_name, password);
//            System.out.println("정상적으로 연결되었습니다.");
//        } catch (
//                SQLException e) {
//            System.err.println("con 오류:" + e.getMessage());
//            e.printStackTrace();
//        }
//
//        // 3.해제
//        try {
//            if (con != null)
//                con.close();
//        } catch (SQLException e) {
//        }
    }
}

