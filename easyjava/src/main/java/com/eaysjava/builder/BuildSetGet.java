package com.eaysjava.builder;

import com.eaysjava.bean.Constants;
import com.eaysjava.bean.FieldInfo;
import com.eaysjava.bean.TableInfo;
import com.eaysjava.enums.Sql2JavaTypeEnum;
import com.eaysjava.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;

public class BuildSetGet {

    public static final Logger logger = LoggerFactory.getLogger(BuildSetGet.class);

    public static void createGetsSets(BufferedWriter bw, TableInfo tableInfo) {
        for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
            if (fieldInfo != null && fieldInfo.getPropertyName() != null) {
                try {
                    String propertyName = fieldInfo.getPropertyName();
                    String upFirstPropertyName = StringUtils.processField(propertyName, true);
                    bw.write("\t" + "public " + fieldInfo.getJavaType() + " get" + upFirstPropertyName + "() {");
                    bw.newLine();
                    bw.write("\t\treturn this." + propertyName + ";");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();
                    bw.newLine();

                    bw.write("\t" + "public void set" + upFirstPropertyName + "(" + fieldInfo.getJavaType() + " " + propertyName + ") {");
                    bw.newLine();
                    bw.write("\t\tthis." + propertyName + " = " + propertyName + ";");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();
                    bw.newLine();
                } catch (IOException e) {
                    logger.error("写入get/set方法失败");
                }
            }
        }
    }

    public static void createQueryGetsSets(BufferedWriter bw, TableInfo tableInfo) {
        createGetsSets(bw, tableInfo);

        for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
            try {
                if (fieldInfo.getJavaType().equals("String")) {
                    createQueryGetsOrSets(fieldInfo, Constants.BEAN_QUERY_FUZZY_SUFFIX, fieldInfo.getJavaType(), bw);
                }
                if (fieldInfo.getJavaType().equals("Date")) {
                    createQueryGetsOrSets(fieldInfo, Constants.BEAN_QUERY_TIME_START_SUFFIX, fieldInfo.getJavaType(), bw);
                    createQueryGetsOrSets(fieldInfo, Constants.BEAN_QUERY_TIME_END_SUFFIX, fieldInfo.getJavaType(), bw);
                }

            } catch (Exception e) {
                logger.error("Query写入失败");
            }
        }

    }

    public static void createQueryGetsOrSets(FieldInfo fieldInfo, String suffix, String javaType, BufferedWriter bw) throws IOException {
        String propertyName = fieldInfo.getPropertyName() + StringUtils.processField(suffix, true);
        String upFirstPropertyQuery = StringUtils.processField(propertyName, true);
        if (javaType.equals("String")) {
            bw.write("\t" + "public " + fieldInfo.getJavaType() + " get" + upFirstPropertyQuery + "() {");
            bw.newLine();
            bw.write("\t\treturn this." + propertyName + ";");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();
        } else if (javaType.equals("Date")) {
            bw.write("\t" + "public String get" + upFirstPropertyQuery + "() {");
            bw.newLine();
            bw.write("\t\treturn this." + propertyName + ";");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();
        }

        if (javaType.equals("String")) {
            bw.write("\t" + "public void set" + upFirstPropertyQuery + "(" + fieldInfo.getJavaType() + " " + propertyName + ") {");
            bw.newLine();
            bw.write("\t\tthis." + propertyName + " = " + propertyName + ";");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();
        } else if (javaType.equals("Date")) {
            bw.write("\t" + "public void set" + upFirstPropertyQuery + "( String " + propertyName + ") {");
            bw.newLine();
            bw.write("\t\tthis." + propertyName + " = " + propertyName + ";");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();
        }

    }

    public static void createToString(BufferedWriter bw, TableInfo tableInfo) {
        StringBuffer buffer = new StringBuffer();
        for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
            String comment = fieldInfo.getComment() == null ? "" : fieldInfo.getComment();
            if (ArrayUtils.contains(Sql2JavaTypeEnum.DATE_TIME.getSqlType(), fieldInfo.getSqlType())) {
                buffer.append("\"" + comment + ":\"+(" + fieldInfo.getPropertyName() + " == null ? \"空\" : DateUtils.format(" + fieldInfo.getPropertyName() + ",DateUtils.YYYY_MM_DD_HH_MM_SS))+");
            } else if (ArrayUtils.contains(Sql2JavaTypeEnum.DATE.getSqlType(), fieldInfo.getSqlType())) {
                buffer.append("\"" + comment + ":\"+(" + fieldInfo.getPropertyName() + " == null ? \"空\" : DateUtils.format(" + fieldInfo.getPropertyName() + ",DateUtils.YYYY_MM_DD))+");
            } else {
                buffer.append("\"" + comment + ":\"+(" + fieldInfo.getPropertyName() + " == null ? \"空\" : " + fieldInfo.getPropertyName() + ")+");

            }
        }
        try {
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic String toString() {");
            bw.newLine();
            bw.write("\t\treturn " + buffer.deleteCharAt(buffer.length() - 1) + ";");
            bw.newLine();
            bw.write("\t}");
        } catch (IOException e) {
            logger.error("toString方法写入失败");
        }
    }
}
