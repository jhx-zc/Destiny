package com.main.services.login;

import com.main.module.rpc.Rpc;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.function.*;

@WebServlet(urlPatterns = {"/login/*"})
public class Login extends HttpServlet {
    private Map<String, BiConsumer<HttpServletRequest, HttpServletResponse>> routerMap = new HashMap<>();

    @Override
    public void init() throws ServletException {
        super.init();
        routerMap.put("Manager", this::Manager);
        routerMap.put("Seller", this::Seller);
        routerMap.put("Consumer", this::Consumer);
        routerMap.put("HttpTest", this::HttpTest);
    }

    private void errorRouteResp(HttpServletResponse resp, String route) {
        System.out.println("no route to" + route);
        try {
            resp.sendError(404, "no route to" + route);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        //路由转发
        String route = req.getHttpServletMapping().getMatchValue();
        BiConsumer<HttpServletRequest, HttpServletResponse> router = routerMap.get(route);

        if (router == null) {
            errorRouteResp(resp, route);
            return;
        }

        router.accept(req, resp);
    }

    //后台管理登录
    protected void Manager(HttpServletRequest req, HttpServletResponse resp) {
        int len = req.getContentLength();
        ServletInputStream iii = null;
        try {
            iii = req.getInputStream();
            byte[] buffer = new byte[len];
            iii.read(buffer, 0, len);

            Rpc.LoginReq loginReq = Rpc.LoginReq.parseFrom(buffer);
            PrintWriter writer = resp.getWriter();
            writer.write("<p>username:"+loginReq.getUsername()+"</p>");
            writer.write("<p>password:"+loginReq.getPassword()+"</p>");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //店家登录
    protected void Seller(HttpServletRequest req, HttpServletResponse resp) {

    }

    //游客登录
    protected void Consumer(HttpServletRequest req, HttpServletResponse resp) {

    }

    //HttpTest
    protected void HttpTest(HttpServletRequest req, HttpServletResponse resp) {
        Rpc.LoginReq.Builder builder = Rpc.LoginReq.newBuilder();
        builder.setUsername("HttpTest");
        builder.setPassword("HttpTest");
        Rpc.LoginReq loginReq = builder.build();

        StringBuffer buffer = null;

        try {
            URL url = new URL("http://localhost:8080/service_war_exploded/login/Manager");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.connect();
            //往服务器端写内容 也就是发起http请求需要带的参数
            OutputStream os = conn.getOutputStream();
            os.write(loginReq.toByteArray());
            os.close();

            //读取服务器端返回的内容
            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            buffer = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }

            PrintWriter writer = resp.getWriter();
            writer.write(buffer.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        HttpTest(req, resp);
    }
}
