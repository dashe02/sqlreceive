package com.jdbc;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
/**
 * Created with IntelliJ IDEA.
 * User: hadoop
 * Date: 14-11-7
 * Time: 下午5:23
 * To change this template use File | Settings | File Templates.
 */
public final class JDBCUtil {
    private static DataSource myDataSource=null;
    public JDBCUtil(){

    }
    static {
        try{
            Properties pro=new Properties();
            InputStream is= new FileInputStream("conf/dbcpconfig.properties");
            pro.load(is);
            myDataSource= BasicDataSourceFactory.createDataSource(pro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static Connection getConnection(){
         Connection connection=null;
         try{
             connection=myDataSource.getConnection();
         }catch (Exception e){
             e.printStackTrace();
         }
         return connection;
    }
    public static void main(String[] args) throws Exception{
        System.out.println(getConnection());
    }
}
