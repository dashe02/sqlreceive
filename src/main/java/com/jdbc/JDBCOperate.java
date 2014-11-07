package com.jdbc;

import com.jdbc.api.JDBCMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: hadoop
 * Date: 14-11-7
 * Time: 下午5:56
 * To change this template use File | Settings | File Templates.
 */
public class JDBCOperate implements JDBCMapper{
    @Override
    public List<LinkedHashMap<String, String>> executeSelect(String sql) {
        List<LinkedHashMap<String,String>> mList=new ArrayList<LinkedHashMap<String, String>>();
        Connection connection=JDBCUtil.getConnection();
        PreparedStatement statement=null;
        ResultSet rs=null;
        try{
        statement=connection.prepareStatement(sql);
        rs=statement.executeQuery();
        ResultSetMetaData rsmd=rs.getMetaData();
        int colCount=rsmd.getColumnCount();
        while(rs.next()){
        for(int i=1;i<=colCount;i++){
            String name=rsmd.getColumnName(i);
            String value=rs.getString(name);
            LinkedHashMap<String,String> m=new LinkedHashMap<String, String>();
            m.put(name,value);
            mList.add(m);
        }
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        return mList;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean executeSaveOrUpdate(String sql,LinkedHashMap<String, String> m) {
        boolean flag=false;
        Connection connection=JDBCUtil.getConnection();
        PreparedStatement statement=null;
        try{
        statement=connection.prepareStatement(sql);
        int i=1;
        for(Map.Entry e:m.entrySet()){
        statement.setString(i,e.getValue().toString());
        i++;
        }
        int f=statement.executeUpdate();
        if(f==1){
            flag=true;
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        return flag;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean executeDelete(String sql,long id) {
        Connection connection=JDBCUtil.getConnection();
        PreparedStatement statement=null;
        boolean flag=false;
        try{
        statement=connection.prepareStatement(sql);
        statement.setLong(1,id);
        int f=statement.executeUpdate();
        if(f==1){
        flag=true;
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        return flag;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public static void main(String[] args){
        String sql="delete from student where id=?";
        System.out.println(new JDBCOperate().executeDelete(sql,4));
    }
}
