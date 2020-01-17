package org.wubo.flinkproject.jdbc;

import org.wubo.flinkproject.conf.ConfigurationManager;
import org.wubo.flinkproject.constant.Constant;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author: wubo
 * @Date: 2020/1/15 16:26
 */
public class JDBCHelper {
    static {
        try {
            String driver = ConfigurationManager.getProperties(Constant.JDBC_DRIVER);
            Class.forName(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //实现JDBCHelper的单例化
    private static JDBCHelper instance = null;

    /**
     * 获取单例
     *
     * @return
     */
    public static JDBCHelper getInstance() {
        if (instance == null) {
            synchronized (JDBCHelper.class) {
                if (instance == null) {
                    instance = new JDBCHelper();
                }
            }
        }
        return instance;
    }

    //数据库连接池
    private LinkedList<Connection> datasource = new LinkedList<>();

    //实现单例的过程中创建唯一的数据库连接池
    private JDBCHelper() {
        //获取数据库连接池的大小，通过配置文件进行配置
        int datasourceSize = ConfigurationManager.getInteger(Constant.JDBC_DATASOURCE_SIZE);
        //创建指定数量的数据库连接，放入到数据库连接池中
        for (int i = 0; i < datasourceSize; i++) {
            String url = ConfigurationManager.getProperties(Constant.JDBC_URL);
            String user = ConfigurationManager.getProperties(Constant.JDBC_USER);
            String password = ConfigurationManager.getProperties(Constant.JDBC_PASSWORD);
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(url, user, password);
                datasource.push(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    //提供数据库连接的方法
    public synchronized Connection getConnection(){
        while(datasource.size()==0){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return datasource.poll();
    }

    //开发增删改查的方法
    public int executeUpdate(String sql,Object[] params){
        int rtn=0;
        Connection connection=null;
        PreparedStatement pstm=null;
        try {
            connection = getConnection();
            pstm = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                pstm.setObject(i + 1, params[i]);
            }
            rtn = pstm.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(connection!=null){
                datasource.push(connection);
            }
        }
        return rtn;
    }

    public void executeQuery(String sql,Object[] params,QueryCallback callback){

        Connection connection=null;
        PreparedStatement pstm=null;
        ResultSet rs=null;
        try {
            connection=getConnection();
            pstm=connection.prepareStatement(sql);
            for(int i=0;i<params.length;i++){
                pstm.setObject(i+1,params[i]);
            }
            rs=pstm.executeQuery();
            callback.process(rs);
        }catch (Exception e){

        }finally {
            if(connection!=null){
                datasource.push(connection);
            }
        }
    }

    public int[] executeBatch(String sql, List<Object[]> paramsList){
        int[] rtn=null;
        Connection connection=null;
        PreparedStatement pstm=null;
        try {
            connection=getConnection();
            //使用connection对象取消自动提交
            connection.setAutoCommit(false);
            pstm=connection.prepareStatement(sql);
            //使用preparedStatement addBatch方法加入批量sql
            for(Object[] params:paramsList){
                for(int i=0;i<params.length;i++){
                    pstm.setObject(i+1,params[i]);
                }
                pstm.addBatch();
            }
            rtn=pstm.executeBatch();
        }catch (Exception e){
            e.printStackTrace();
        }
        return rtn;
    }
    public static interface QueryCallback{
        /**
         * 处理查询结果
         * @param rs
         * @throws Exception
         */
        void process(ResultSet rs)throws Exception;
    }
}
