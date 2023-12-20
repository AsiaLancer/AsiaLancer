package com.eaysjava.builder;

import com.eaysjava.bean.Constants;
import com.eaysjava.bean.FieldInfo;
import com.eaysjava.bean.TableInfo;
import com.eaysjava.enums.Sql2JavaTypeEnum;
import com.eaysjava.utils.JsonUtils;
import com.eaysjava.utils.PropertiesUtils;
import com.eaysjava.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildTable {
    private static Connection conn = null;
    private static Logger logger = LoggerFactory.getLogger(BuildTable.class);

    private static String SHOW_TABLE_STATUS_SQL = "show table status";
    private static String SHOW_FULL_FIELDS_FROM_SQL = "show full fields from %s";
    private static String SHOW_INDEX_FROM_SQL = "show index from %s";

    static {

        String driverName = PropertiesUtils.getString("db.driver.name");
        String url = PropertiesUtils.getString("db.url");
        String username = PropertiesUtils.getString("db.username");
        String password = PropertiesUtils.getString("db.password");
        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            logger.error("数据库连接失败", e);
        }
    }

    public static List<TableInfo> getTables() {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<TableInfo> tableInfoList = new ArrayList<>();

        try {
            statement = conn.prepareStatement(SHOW_TABLE_STATUS_SQL);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String tableName = resultSet.getString("name");
                String comment = resultSet.getString("comment");
                TableInfo tableInfo = new TableInfo();

                String beanName = tableName;
                if (Constants.IGNORE_TABLE_PREFIX) {
                    beanName = beanName.substring(beanName.indexOf("_") + 1);
                }
                beanName = StringUtils.processField(beanName, true);
                tableInfo.setTableName(tableName);
                tableInfo.setComment(comment);
                tableInfo.setBeanName(beanName);
                tableInfo.setBeanParamName(beanName + StringUtils.processField(Constants.BEAN_QUERY_SUFFIX, true));
//                logger.info(tableInfo.toString());
                List<FieldInfo> fieldInfoList = readFieldInfo(tableInfo);
                tableInfo.setFieldList(fieldInfoList);
                readIndexMap(tableInfo);
//                logger.info(JsonUtils.convertObj2Json(tableInfo));
                tableInfoList.add(tableInfo);
            }
//            logger.info(JsonUtils.convertObj2Json(tableInfoList));
        } catch (Exception e) {
            logger.error("读取表失败", e);
        } finally {
            if (resultSet != null) {
                try {

                    resultSet.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

        }
        return tableInfoList;
    }

    private static List<FieldInfo> readIndexMap(TableInfo tableInfo) {
        PreparedStatement statement = null;
        ResultSet indexResultSet = null;
        try {
            Map<String, FieldInfo> map = new HashMap<>();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                map.put(fieldInfo.getFieldName(), fieldInfo);
            }

            statement = conn.prepareStatement(String.format(SHOW_INDEX_FROM_SQL, tableInfo.getTableName()));
            indexResultSet = statement.executeQuery();
            while (indexResultSet.next()) {

                Integer nonUnique = indexResultSet.getInt("non_unique");
                String keyName = indexResultSet.getString("key_name");
                String columnName = indexResultSet.getString("column_name");
//                String table = indexResultSet.getString("table");
                if (nonUnique == 1) {
                    continue;
                }
//                List<FieldInfo> fieldInfos = new ArrayList<FieldInfo>();

                List<FieldInfo> fieldInfos = tableInfo.getKeyIndexMap().get(keyName);
                if (fieldInfos == null) {
                    fieldInfos = new ArrayList<FieldInfo>();
                    tableInfo.getKeyIndexMap().put(keyName, fieldInfos);
                }

                fieldInfos.add(map.get(columnName));

//                for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
//                    if (fieldInfo.getFieldName().equals(columnName)) {
//                        fieldInfos.add(fieldInfo);
//                    }
//                }
            }
        } catch (Exception e) {
            logger.error("读取字段索引失败", e);
        } finally {
            if (indexResultSet != null) {
                try {

                    indexResultSet.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    private static List<FieldInfo> readFieldInfo(TableInfo tableInfo) {
        PreparedStatement statement = null;
        ResultSet filedResultSet = null;
        List<FieldInfo> fieldInfoList = new ArrayList<>();
        List<FieldInfo> fieldFuzzyList = new ArrayList<>();
        try {
            statement = conn.prepareStatement(String.format(SHOW_FULL_FIELDS_FROM_SQL, tableInfo.getTableName()));
            filedResultSet = statement.executeQuery();

            Boolean haveDateTime = false;
            Boolean haveBigDecimal = false;
            Boolean havaDate = false;
            while (filedResultSet.next()) {
                String fieldName = filedResultSet.getString("field");
                String sqlType = filedResultSet.getString("type");
                String isAutoIncrement = filedResultSet.getString("extra");
                String comment = filedResultSet.getString("comment");
                FieldInfo fieldInfo = new FieldInfo();
                fieldInfo.setFieldName(fieldName);
                fieldInfo.setPropertyName(StringUtils.processField(fieldName, false));
                if (sqlType.indexOf("(") > 0) {
                    sqlType = sqlType.substring(0, sqlType.indexOf("("));
                }
                fieldInfo.setSqlType(sqlType);
                fieldInfo.setJavaType(Sql2JavaTypeEnum.getBySqlType(sqlType).getJavaType());
                fieldInfo.setAutoIncrement((isAutoIncrement.equals("auto_increment") && isAutoIncrement != null ? true : false));
                fieldInfo.setComment(comment);
//                logger.info(fieldInfo.toString());
                fieldInfoList.add(fieldInfo);

                if (ArrayUtils.contains(Sql2JavaTypeEnum.DATE_TIME.getSqlType(), sqlType)) {
                    haveDateTime = true;
                }
                if (ArrayUtils.contains(Sql2JavaTypeEnum.DATE.getSqlType(), sqlType)) {
                    havaDate = true;
                }
                if (ArrayUtils.contains(Sql2JavaTypeEnum.BIG_DECIMAL.getSqlType(), sqlType)) {
                    haveBigDecimal = true;
                }
                if (ArrayUtils.contains(Sql2JavaTypeEnum.STRING.getSqlType(),sqlType)){
                    FieldInfo fuzzyFiledInfo = new FieldInfo();
                    fuzzyFiledInfo.setJavaType(fieldInfo.getJavaType());
                    fuzzyFiledInfo.setPropertyName(fieldInfo.getPropertyName()+StringUtils.processField(Constants.BEAN_QUERY_FUZZY_SUFFIX,true));
                    fuzzyFiledInfo.setFieldName(fieldInfo.getFieldName());
                    fuzzyFiledInfo.setSqlType(sqlType);
                    fieldFuzzyList.add(fuzzyFiledInfo);
                }
                if (ArrayUtils.contains(Sql2JavaTypeEnum.DATE.getSqlType(),sqlType)||ArrayUtils.contains(Sql2JavaTypeEnum.DATE_TIME.getSqlType(),sqlType)){
                    FieldInfo fuzzyFiledInfo1 = new FieldInfo();
                    fuzzyFiledInfo1.setJavaType(fieldInfo.getJavaType());
                    fuzzyFiledInfo1.setPropertyName(fieldInfo.getPropertyName()+StringUtils.processField(Constants.BEAN_QUERY_TIME_START_SUFFIX,true));
                    fuzzyFiledInfo1.setFieldName(fieldInfo.getFieldName());
                    fuzzyFiledInfo1.setSqlType(sqlType);
                    fieldFuzzyList.add(fuzzyFiledInfo1);

                    FieldInfo fuzzyFiledInfo2 = new FieldInfo();
                    fuzzyFiledInfo2.setJavaType(fieldInfo.getJavaType());
                    fuzzyFiledInfo2.setPropertyName(fieldInfo.getPropertyName()+StringUtils.processField(Constants.BEAN_QUERY_TIME_END_SUFFIX ,true));
                    fuzzyFiledInfo2.setFieldName(fieldInfo.getFieldName());
                    fuzzyFiledInfo2.setSqlType(sqlType);
                    fieldFuzzyList.add(fuzzyFiledInfo2);
                }
            }
            tableInfo.setFieldFuzzyList(fieldFuzzyList);
            tableInfo.setHaveDate(havaDate);
            tableInfo.setHavaDateTime(haveDateTime);
            tableInfo.setHaveBigDecimal(haveBigDecimal);
        } catch (Exception e) {
            logger.error("读取表字段失败", e);

        } finally {
            if (filedResultSet != null) {
                try {

                    filedResultSet.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return fieldInfoList;
    }

}
