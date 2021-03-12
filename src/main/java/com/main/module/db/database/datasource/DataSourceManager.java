package com.main.module.db.database.datasource;

import com.zaxxer.hikari.*;

import javax.sql.*;
import java.sql.Connection;
import java.util.*;
import java.util.concurrent.*;

class MyDataSource {
    //for whole config see:(https://blog.csdn.net/qq_32953079/article/details/81502237)
    private String url;
    private String username;
    private String password;
    private String connTimeOut;
    private String poolSize;
    private String readOnly;


    /**
     * @param url         db url.eg:jdbc:mysql://localhost:3306/test
     * @param username
     * @param password
     * @param connTimeOut 连接超时
     * @param poolSize    最大连接数
     * @param readOnly    池中获取的连接是否默认处于只读模式
     */
    public MyDataSource(String url, String username, String password, String connTimeOut, String poolSize, String readOnly) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.connTimeOut = connTimeOut;
        this.poolSize = poolSize;
        this.readOnly = readOnly;
    }

    public DataSource Init() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty("connectionTimeout", connTimeOut); // 连接超时
        config.addDataSourceProperty("maximumPoolSize", poolSize); // 最大连接数
        config.addDataSourceProperty("readOnly", readOnly); // 从池中获取的连接是否默认处于只读模式
        return new HikariDataSource(config);
    }
}

public class DataSourceManager {
    //sysId -> HikariDataSource
    private Map<Integer, DataSource> m_dataSourceMap = new ConcurrentHashMap<>();

    /**
     * @param sid system id
     */
    public void AddDataSource(int sid, String url, String username, String password, String connTimeOut, String poolSize, String readOnly) {
        m_dataSourceMap.put(sid, new MyDataSource(url, username, password, connTimeOut, poolSize, readOnly).Init());
    }

    /**
     * got a db conn from DataSourceManager by sysId.Need call Func conn.close() after usage
     *
     * @param sid system id
     * @return a dbConn
     * @throws Exception DSManager not save this sysId DS.Or ds get conn err
     */
    public Connection GotDbBySid(int sid) throws Exception {
        DataSource ds = m_dataSourceMap.get(sid);

        if (ds != null) {
            return ds.getConnection();
        }
        throw new Exception("DataSourceManager not have this sysId:" + sid);
    }
}
