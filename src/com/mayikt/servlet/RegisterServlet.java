package com.mayikt.servlet;

import com.mayikt.utils.MayiktJdbcUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        String userName = req.getParameter("userName");
        String userPwd = req.getParameter("userPwd");
        System.out.println(userName+" "+userPwd);
        PrintWriter writer = resp.getWriter();
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        try {
            connection = MayiktJdbcUtils.getConnection();
            MayiktJdbcUtils.beginTransaction(connection);
            String sql="insert into mayikt_user(id,userName,userPwd) values (null,?,?);";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,userName);
            preparedStatement.setString(2,userPwd);
            int result = preparedStatement.executeUpdate();
            String resultStr=result>0? "注册成功" : "注册失败";
            writer.println(resultStr);
            MayiktJdbcUtils.commitTransaction(connection);
        }catch (Exception e){
            e.printStackTrace();
            writer.println("error");
            MayiktJdbcUtils.rollBackTransaction(connection);
        }finally {
            if (writer != null) {
                writer.close();
            }
            MayiktJdbcUtils.closeConnection(null,preparedStatement,connection);
        }
    }
}
