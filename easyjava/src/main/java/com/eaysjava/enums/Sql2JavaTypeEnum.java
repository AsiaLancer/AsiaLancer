package com.eaysjava.enums;

import org.apache.commons.lang3.ArrayUtils;


public enum Sql2JavaTypeEnum {
    STRING(new String[]{"char","varchar","text","mediumtext","longtext"},"String","字符串"),
    DATE_TIME(new String[]{"timestamp","datetime"},"Date","时间"),
    DATE(new String[]{"date"},"Date","日期"),
    BIG_DECIMAL(new String[]{"decimal","float","double"},"BigDecimal","小数"),
    INTEGER(new String[]{"int","tinyint"},"Integer","整数"),
    LONG(new String[]{"bigint"},"Long","大整数");
    private String[] sqlType;

    private String javaType;

    private String desc;


    public static Sql2JavaTypeEnum getBySqlType(String sqlType){
        for (Sql2JavaTypeEnum s:Sql2JavaTypeEnum.values()){
            if (ArrayUtils.contains(s.sqlType,sqlType)){
                return s;
            }
        }
        return null;
    }
    public static Sql2JavaTypeEnum getByJavaType(String javaType){
        for (Sql2JavaTypeEnum s:Sql2JavaTypeEnum.values()){
            if (s.javaType.equals(javaType)){
                return s;
            }
        }
        return null;
    }

    Sql2JavaTypeEnum(String[] sqlType, String javaType, String desc) {
        this.sqlType = sqlType;
        this.javaType = javaType;
        this.desc = desc;
    }

    public String[] getSqlType() {
        return sqlType;
    }

    public String getJavaType() {
        return javaType;
    }

    public String getDesc() {
        return desc;
    }
}
