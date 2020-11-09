package com;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.jdbc.AtomikosDataSourceBean;

import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.sql.*;
import java.util.Properties;

/**
 * @description：atomikos事务测试
 * @author: caiwx
 * @createDate ： 2020年11月03日 10:52:00
 */
public class AtomikosExample {

    private static AtomikosDataSourceBean createAtomikosDataSourceBean(String dbName) {
        Properties properties = new Properties();
        properties.setProperty("url", "jdbc:mysql://192.168.165.129:3306/" + dbName);
        properties.setProperty("user", "root");
        properties.setProperty("password", "root");

        //封装com.mysql.jdbc.jdbc2.optional.MysqlXADataSource
        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        ds.setUniqueResourceName(dbName);
        ds.setXaDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlXADataSource");
        ds.setXaProperties(properties);
        return ds;
    }

    public static void main(String[] args) {
        AtomikosDataSourceBean ds1 = createAtomikosDataSourceBean("db_user");
        AtomikosDataSourceBean ds2 = createAtomikosDataSourceBean("db_account");

        Connection connection1 = null;
        Connection connection2 = null;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;

        UserTransaction userTransaction = new UserTransactionImp();
        try {
            //开启事务
            userTransaction.begin();

            connection1 = ds1.getConnection();
            preparedStatement1 = connection1.prepareStatement("insert into user(name) values (?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement1.setString(1, "name1");
            preparedStatement1.executeUpdate();
            ResultSet keys = preparedStatement1.getGeneratedKeys();
            int userId = -1;
            while (keys.next()) {
                userId = keys.getInt(1);
            }

            connection2 = ds2.getConnection();
            preparedStatement2 = connection2.prepareStatement("insert into account(user_id, money) values (?, ?)");
            preparedStatement2.setInt(1, userId);
            preparedStatement2.setDouble(2, 10);
            preparedStatement2.executeUpdate();

            //两阶段提交
            userTransaction.commit();
        } catch (Exception e) {
            try {
                e.printStackTrace();
                userTransaction.rollback();
            } catch (SystemException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                preparedStatement1.close();
                preparedStatement2.close();
                connection1.close();
                connection2.close();
                ds1.close();
                ds2.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
