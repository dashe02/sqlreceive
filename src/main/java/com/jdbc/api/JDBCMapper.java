package com.jdbc.api;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: hadoop
 * Date: 14-11-7
 * Time: 下午5:57
 * To change this template use File | Settings | File Templates.
 */
public interface JDBCMapper {
    public List<LinkedHashMap<String,String>> executeSelect(String sql);

    public boolean executeSaveOrUpdate(String sql,LinkedHashMap<String,String> m);

    public boolean executeDelete(String sql,long id);

}
