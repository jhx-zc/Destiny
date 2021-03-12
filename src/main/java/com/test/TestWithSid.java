package com.test;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(urlPatterns = {"/test_sid"})
public class TestWithSid extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html,charset=utf-8");
        PrintWriter out = resp.getWriter();
        out.println("<html>\n" +
                "<head>\n" +
                "    <title>post_sid!</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\t<h1>post_sid:" + req.getHeader("sid")+
                "</h1>\n" +
                "</body>\n" +
                "</html>");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html,charset=utf-8");
        PrintWriter out = resp.getWriter();
        out.println("<html>\n" +
                "<head>\n" +
                "    <title>post_sid!</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\t<h1>post_sid: get" +
                "</h1>\n" +
                "</body>\n" +
                "</html>");
    }
}
