package com.eaysjava.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class PropertiesUtils {
    private static Properties properties = new Properties();
    private static Map<String,String> PROPER_MAP = new ConcurrentHashMap<String,String>();


    static {
        InputStream in = null;

        try {
            in = PropertiesUtils.class.getClassLoader().getResourceAsStream("application.properties");
            properties.load(new InputStreamReader(in,"utf-8"));
            Iterator<Object> iterator = properties.keySet().iterator();
            while (iterator.hasNext()){
                String key = (String) iterator.next();
                String value = properties.getProperty(key);
                PROPER_MAP.put(key,value);
            }

        }catch (Exception e){

        }finally {
            if (in!=null){
                try {
                    in.close();
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        }
    }

    public static String getString(String key){
        return PROPER_MAP.get(key);
    }
}
