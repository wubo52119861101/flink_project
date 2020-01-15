package org.wubo.flinkproject.test;

import org.wubo.flinkproject.jdbc.JDBCHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wubo
 * @Date: 2020/1/15 16:46
 */
public class JDBCHelperTest {
    public static void main(String[] args) {
        JDBCHelper jdbcHelper=JDBCHelper.getInstance();
        //jdbcHelper.executeUpdate("insert into user(name,age)values(?,?)",new Object[]{"王五",18});
        //测试查询语句
        final Map<String,Object> testUser=new HashMap<>();
        jdbcHelper.executeQuery("select name,age from user", new Object[]{}, rs -> {
            while(rs.next()){
                String name=rs.getString(1);
                int age=rs.getInt(2);
                testUser.put("name",name);
                testUser.put("age",age);
            }
        });
        System.out.println(testUser.get("name")+""+testUser.get("age"));
    }
}
