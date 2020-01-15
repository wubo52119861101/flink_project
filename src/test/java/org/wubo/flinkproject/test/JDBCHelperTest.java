package org.wubo.flinkproject.test;

import org.wubo.flinkproject.jdbc.JDBCHelper;

import java.sql.Connection;

/**
 * @Author: wubo
 * @Date: 2020/1/15 16:46
 */
public class JDBCHelperTest {
    public static void main(String[] args) {
        Connection connection= JDBCHelper.getInstance().getConnection();
        System.out.println(connection);
    }
}
