package com.test;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.*;

@WebServlet(urlPatterns = {"/test"})
public class Test extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html,charset=utf-8");
        PrintWriter out = resp.getWriter();
        out.println("<html>\n" +
                "<head>\n" +
                "    <title>test!</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\t<h1>Test</h1>\n" +
                "</body>\n" +
                "</html>");
    }
}