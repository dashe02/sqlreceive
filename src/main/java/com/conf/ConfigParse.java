package com.conf;

import com.util.Constant;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: hadoop
 * Date: 14-11-7
 * Time: 下午3:46
 * To change this template use File | Settings | File Templates.
 */
public class ConfigParse {
     public Properties parseConfig(){
         Properties pros=new Properties();
         try {
         InputStream in=new FileInputStream(Constant.configPath);
         pros.load(in);
         }catch(IOException e){
            e.printStackTrace();
         }
         return pros;
     }
}
