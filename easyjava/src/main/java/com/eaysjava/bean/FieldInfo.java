package com.eaysjava.bean;

public class FieldInfo {
    /**
     * 字段名
     */
    private String fieldName;
    /**
     * bean属性名
     */
    private String propertyName;
    /**
     * 数据库字段类型
     */
    private String sqlType;

    /**
     * 字段类型
     */
    private String javaType;
    /**
     * 字段注释
     */
    private String comment;
    /**
     * 是否自增长
     */
    private Boolean isAutoIncrement;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getAutoIncrement() {
        return isAutoIncrement;
    }

    public void setAutoIncrement(Boolean autoIncrement) {
        isAutoIncrement = autoIncrement;
    }

    @Override
    public String toString() {
        return "FieldInfo{" +
                "fieldName='" + fieldName + '\'' +
                ", propertyName='" + propertyName + '\'' +
                ", sqlType='" + sqlType + '\'' +
                ", javaType='" + javaType + '\'' +
                ", comment='" + comment + '\'' +
                ", isAutoIncrement=" + isAutoIncrement +
                '}';
    }
}
