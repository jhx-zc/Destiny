package com.main.services.login;

import com.main.module.db.database.datasource.DataSourceManager;
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

        DataSourceManager DSM = new DataSourceManager();
        DSM.AddDataSource(1, "192.168.3.1", "root", "root", "3000", "2", "true");
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

    protected boolean JudgePWD(String username, String pwd) {
        //todo query database for count is valid
        //search sql SELECT .e.g. select * from table_name where username = ?
        //sql java param. java sql 防注入 .SQL PARAM

        //了解什么是注入 https://zhuanlan.zhihu.com/p/82389858
        //如何使用 PreparedStatement https://www.cnblogs.com/iyangyuan/p/4809494.html

        //参考 https://blog.csdn.net/chen88358323/article/details/7613257

        //抽空了解 sql语句
        /* 查询数据库
           boolean result = false;//默认账号密码错误
           String sql = "select count(*) from test_preparedstatement where username=? and password=?";
           prepar = conn.prepareStatement(sql);
           prepar.setString(1, name);
           prepar.setString(2, password);
           res = prepar.executeQuery();
           if (res.next())   {
               if (res.getInt(1) > 0){
               //校验发现账号密码匹配正确
                   result = true;
               }
           }
        * */

         /* 更新数据库
         String sql="insert into test_preparedstatement (username,password,mail,registtime) values(?,?,?,?)";
           prepar=conn.prepareStatement(sql);
           prepar.setString(1,person.getUsername());
           prepar.setString(2,person.getPassword());
           prepar.setString(3,person.getMail());
           prepar.setTimestamp(4,person.getRegistTime());
           prepar.executeUpdate();
        * */


        return false;
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
            writer.write("<p>username:" + loginReq.getUsername() + "</p>");
            writer.write("<p>password:" + loginReq.getPassword() + "</p>");

            //校验用户密码账号是否对应
            boolean isValidUser = JudgePWD(loginReq.getUsername(), loginReq.getPassword());
            writer.write("<p>isValidUser:" + isValidUser + "</p>");

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
