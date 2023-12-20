package com.eaysjava.bean;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TableInfo {
    /**
     *表名
     */
    private String tableName;

    /**
     * bean名
     */
    private String beanName;

    /**
     * bean参数名
     */
    private String beanParamName;

    /**
     * 表注释
     */
    private String comment;

    /**
     * 字段信息
     */
    private List<FieldInfo> fieldList;

    /**
     * 扩展字段信息
     */
    private List<FieldInfo> fieldFuzzyList;

    /**
     * 唯一索引集合
     */
    private Map<String,List<FieldInfo>> keyIndexMap = new LinkedHashMap<>();
    /**
     * 是否有Date类型
     */
    private Boolean haveDate;
    /**
     * 是否有DateTime类型
     */
    private Boolean havaDateTime;
    /**
     * 是否有BigDecimal类型
     */
    private Boolean haveBigDecimal;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanParamName() {
        return beanParamName;
    }

    public void setBeanParamName(String beanParamName) {
        this.beanParamName = beanParamName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<FieldInfo> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<FieldInfo> fieldList) {
        this.fieldList = fieldList;
    }

    public Map<String, List<FieldInfo>> getKeyIndexMap() {
        return keyIndexMap;
    }

    public void setKeyIndexMap(Map<String, List<FieldInfo>> keyIndexMap) {
        this.keyIndexMap = keyIndexMap;
    }

    public Boolean getHaveDate() {
        return haveDate;
    }

    public void setHaveDate(Boolean haveDate) {
        this.haveDate = haveDate;
    }

    public Boolean getHavaDateTime() {
        return havaDateTime;
    }

    public void setHavaDateTime(Boolean havaDateTime) {
        this.havaDateTime = havaDateTime;
    }

    public Boolean getHaveBigDecimal() {
        return haveBigDecimal;
    }

    public void setHaveBigDecimal(Boolean haveBigDecimal) {
        this.haveBigDecimal = haveBigDecimal;
    }

    public List<FieldInfo> getFieldFuzzyList() {
        return fieldFuzzyList;
    }

    public void setFieldFuzzyList(List<FieldInfo> fieldFuzzyList) {
        this.fieldFuzzyList = fieldFuzzyList;
    }

    @Override
    public String toString() {
        return "TableInfo{" +
                "tableName='" + tableName + '\'' +
                ", beanName='" + beanName + '\'' +
                ", beanParamName='" + beanParamName + '\'' +
                ", comment='" + comment + '\'' +
                ", fieldList=" + fieldList +
                ", fieldFuzzyList=" + fieldFuzzyList +
                ", keyIndexMap=" + keyIndexMap +
                ", haveDate=" + haveDate +
                ", havaDateTime=" + havaDateTime +
                ", haveBigDecimal=" + haveBigDecimal +
                '}';
    }
}
