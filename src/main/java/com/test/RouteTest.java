package com.test;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.util.function.*;

@WebServlet(urlPatterns = {"/route/*"})
public class RouteTest extends HttpServlet {
    private Map<String, BiConsumer<HttpServletRequest, HttpServletResponse>> routerMap = new HashMap<>();

    @Override
    public void init() throws ServletException {
        super.init();
        routerMap.put("route1", this::Route1);
        routerMap.put("route2", this::Route2);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html,charset=utf-8");

        String route = req.getHttpServletMapping().getMatchValue();

        BiConsumer<HttpServletRequest, HttpServletResponse> router = routerMap.get(route);

        if (router == null) router = (request, response) -> System.out.println("no route to" + route);

        router.accept(req, resp);
    }

    protected void Route1(HttpServletRequest req, HttpServletResponse resp)  {
        resp.setContentType("text/html,charset=utf-8");
        PrintWriter out = null;
        try {
            out = resp.getWriter();
            out.println("<html>\n" +
                    "<head>\n" +
                    "    <title>Route1!</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "\t<h1>Route1</h1>\n" +
                    "</body>\n" +
                    "</html>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void Route2(HttpServletRequest req, HttpServletResponse resp)  {
        resp.setContentType("text/html,charset=utf-8");
        PrintWriter out = null;
        try {
            out = resp.getWriter();
            out.println("<html>\n" +
                    "<head>\n" +
                    "    <title>Route2!</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "\t<h1>Route2</h1>\n" +
                    "</body>\n" +
                    "</html>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
