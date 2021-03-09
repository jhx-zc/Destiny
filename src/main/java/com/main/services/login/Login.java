package com.main.services.login;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.util.function.*;

@WebServlet(urlPatterns = {"/login/*"})
public class Login extends HttpServlet {
    private Map<String, BiConsumer<HttpServletRequest, HttpServletResponse>> routerMap = new HashMap<>();

    @Override
    public void init() throws ServletException {
        super.init();
        routerMap.put("Manager", this::Manager);
        routerMap.put("Consumer", this::Consumer);
        routerMap.put("Seller", this::Seller);
    }

    private void errorRouteResp(HttpServletResponse resp,String route){
        System.out.println("no route to" + route);
        try {
            resp.sendError(404,"no route to" + route);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String route = req.getHttpServletMapping().getMatchValue();

        BiConsumer<HttpServletRequest, HttpServletResponse> router = routerMap.get(route);

        if (router == null){
            errorRouteResp(resp, route);
            return;
        }

        router.accept(req, resp);
    }

    //后台管理登录
    protected void Manager(HttpServletRequest req, HttpServletResponse resp) {

    }

    //店家登录
    protected void Seller(HttpServletRequest req, HttpServletResponse resp) {

    }

    //游客登录
    protected void Consumer(HttpServletRequest req, HttpServletResponse resp) {

    }
}
