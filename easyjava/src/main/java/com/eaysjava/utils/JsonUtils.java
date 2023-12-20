package com.eaysjava.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonUtils {

    public static String convertObj2Json(Object o){
        if (null==o){
            return null;
        }
        return JSON.toJSONString(o, SerializerFeature.DisableCircularReferenceDetect);
        //设置禁止循环引用检查报错 直接将循环中的对象定为null了
    }
}
