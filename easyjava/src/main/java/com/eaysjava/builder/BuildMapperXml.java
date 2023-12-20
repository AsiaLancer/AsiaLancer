package com.eaysjava.builder;

import com.eaysjava.bean.Constants;
import com.eaysjava.bean.FieldInfo;
import com.eaysjava.bean.TableInfo;
import com.eaysjava.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildMapperXml {
    private static final Logger logger = LoggerFactory.getLogger(BuildMapperXml.class);

    private static final String BASE_COLUMN_LIST = "base_column_list";
    private static final String BASE_QUERY_CONDITION = "base_query_condition";
    private static final String BASE_CONDITION = "base_condition";
    private static final String EXTEND_QUERY_CONDITION = "extend_query_condition";
    private static final String QUERY_CONDITION = "query_condition";
    private static final String BASE_RESULT_MAP = "base_result_map";

    public static void execute(TableInfo tableInfo) {
        File xmlPath = new File(Constants.PATH_MAPPER_XML);
        if (!xmlPath.exists()) {
            xmlPath.mkdirs();
        }
        String xmlName = tableInfo.getBeanName() + StringUtils.processField(Constants.BEAN_MAPPER_XML_SUFFIX, true);
        File xmlFile = new File(xmlPath, xmlName + ".xml");

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(xmlFile);
            outw = new OutputStreamWriter(out, "utf8");
            bw = new BufferedWriter(outw);
            //头文件
            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            bw.newLine();
            bw.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"");
            bw.newLine();
            bw.write("\t\t\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
            bw.newLine();
            bw.newLine();
            bw.write("<mapper namespace=\"" + Constants.PACKAGE_MAPPER_XML + "." + xmlName + "\">");
            bw.newLine();
            bw.newLine();
            bw.write("\t<!--实体映射-->");
            bw.newLine();
            //resultMap
            String poClassName = Constants.PACKAGE_PO + "." + tableInfo.getBeanName();
            bw.write("\t<resultMap id=\"" + BASE_RESULT_MAP + "\" type=\"" + poClassName + "\">");
            bw.newLine();
            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
            List<FieldInfo> fieldInfoList = keyIndexMap.get("PRIMARY");
            FieldInfo fieldKeyInfo = fieldInfoList.get(0);
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                if (fieldInfoList.size() == 1 && fieldInfo.getFieldName().equals(fieldKeyInfo.getFieldName()) && fieldKeyInfo.getFieldName() != null) {
                    bw.write("\t\t<!--" + fieldKeyInfo.getComment() + "-->");
                    bw.newLine();
                    bw.write("\t\t<id column=\"" + fieldKeyInfo.getFieldName() + "\" property=\"" + fieldInfo.getPropertyName() + "\"/>");
                    bw.newLine();
                } else {
                    bw.write("\t\t<!--" + fieldInfo.getComment() + "-->");
                    bw.newLine();
                    bw.write("\t\t<result column=\"" + fieldInfo.getFieldName() + "\" property=\"" + fieldInfo.getPropertyName() + "\"/>");
                    bw.newLine();
                }
            }
            bw.write("</resultMap>");
            bw.newLine();
            bw.newLine();
            bw.newLine();

            bw.write("\t<!-- 通用查询结果列-->");
            bw.newLine();
            //通用查询列
            bw.write("\t<sql id=\"" + BASE_COLUMN_LIST + "\">");
            bw.newLine();
            Integer index = 0;
            bw.write("\t\t");
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                index++;
                bw.write(fieldInfo.getFieldName());
                if (index < tableInfo.getFieldList().size()) {
                    bw.write(",");
                }
            }
            bw.newLine();
            bw.write("\t</sql>");
            bw.newLine();
            bw.newLine();

            //基础查询条件
            bw.write("\t<!--基础查询条件-->");
            bw.newLine();
            bw.write("\t<sql id=\"" + BASE_QUERY_CONDITION + "\">");
            bw.newLine();
            createQueryCondition(bw, tableInfo.getFieldList(), false);
            bw.write("\t</sql>");
            bw.newLine();
            bw.newLine();

            //通用条件
            bw.write("\t<!--通用条件-->");
            bw.newLine();
            bw.write("\t<sql id=\"" + BASE_CONDITION + "\">");
            bw.newLine();
            bw.write("\t\t<where>");
            bw.newLine();
            bw.write("\t\t\t<include refid=\"" + BASE_QUERY_CONDITION + "\"/>");
            bw.newLine();
            bw.write("\t\t</where>");
            bw.newLine();
            bw.write("\t</sql>");
            bw.newLine();
            bw.newLine();

            //扩展查询条件
            bw.write("\t<!--扩展查询条件-->");
            bw.newLine();
            bw.write("\t<sql id=\"" + EXTEND_QUERY_CONDITION + "\">");
            bw.newLine();
            createQueryCondition(bw, tableInfo.getFieldFuzzyList(), true);
            bw.write("\t</sql>");
            bw.newLine();

            //通用查询条件
            bw.write("\t<!--通用查询条件-->");
            bw.newLine();
            bw.write("\t<sql id=\"" + QUERY_CONDITION + "\">");
            bw.newLine();
            bw.write("\t\t<where>");
            bw.newLine();
            bw.write("\t\t\t<include refid=\"" + BASE_QUERY_CONDITION + "\"/>");
            bw.newLine();
            bw.write("\t\t\t<include refid=\"" + EXTEND_QUERY_CONDITION + "\"/>");
            bw.newLine();
            bw.write("\t\t</where>");
            bw.newLine();
            bw.write("\t</sql>");
            bw.newLine();
            bw.newLine();

            //查询集合
            bw.write("\t<!-- 查询集合-->");
            bw.newLine();
            bw.write("\t<select id=\"selectList\" resultMap=\"" + BASE_RESULT_MAP + "\" >");
            bw.newLine();
            bw.write("\t\tSELECT");
            bw.newLine();
            bw.write("\t\t<include refid=\"" + BASE_COLUMN_LIST + "\"/>");
            bw.newLine();
            bw.write("\t\tFROM " + tableInfo.getTableName());
            bw.newLine();
            bw.write("\t\t<include refid=\"" + QUERY_CONDITION + "\"/>");
            bw.newLine();
            bw.write("\t\t<if test=\"query.orderBy!=null\">");
            bw.newLine();
            bw.write("\t\t\torder by ${query.orderBy}");
            bw.newLine();
            bw.write("\t\t</if>");
            bw.newLine();
            bw.write("\t\t<if test=\"query.simplePage!=null\">");
            bw.newLine();
            bw.write("\t\t\tlimit #{query.simplePage.start},#{query.simplePage.end}");
            bw.newLine();
            bw.write("\t\t</if>");
            bw.newLine();
            bw.write("\t</select>");
            bw.newLine();
            bw.newLine();
            //查询数量
            bw.write("\t<!-- 查询数量-->");
            bw.newLine();
            bw.write("\t<select id=\"selectCount\" resultType=\"java.lang.Integer\">");
            bw.newLine();
            bw.write("\t\tSELECT count(1) FROM " + tableInfo.getTableName() + " <include refid=\"" + QUERY_CONDITION + "\"/>");
            bw.newLine();
            bw.write("\t</select>");
            bw.newLine();
            bw.newLine();

            //插入
            bw.write("\t<!-- 插入 （匹配有值的字段）-->");
            bw.newLine();
            bw.write("\t<insert id=\"insert\" parameterType=\"" + poClassName + "\">");
            bw.newLine();
            if (fieldKeyInfo.getAutoIncrement()) {
                bw.write("\t\t<selectKey keyProperty=\"bean." + fieldKeyInfo.getPropertyName() + "\" resultType=\"" + fieldKeyInfo.getJavaType() + "\" order=\"AFTER\">");
                bw.newLine();
                bw.write("\t\t\tSELECT LAST_INSERT_ID()");
                bw.newLine();
                bw.write("\t\t</selectKey>");
                bw.newLine();
            }
            bw.write("\t\tINSERT INTO " + tableInfo.getTableName());
            bw.newLine();
            //sql参数
            bw.write("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
            bw.newLine();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                if (fieldInfo.getFieldName().equals(fieldKeyInfo.getFieldName()) && fieldKeyInfo.getAutoIncrement()) {
                    continue;
                }
                bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">");
                bw.newLine();
                bw.write("\t\t\t\t" + fieldInfo.getFieldName() + ",");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
            bw.write("\t\t</trim>");
            bw.newLine();
            //属性参数
            bw.write("\t\t<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">");
            bw.newLine();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                if (fieldInfo.getFieldName().equals(fieldKeyInfo.getFieldName()) && fieldKeyInfo.getAutoIncrement()) {
                    continue;
                }
                bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">");
                bw.newLine();
                bw.write("\t\t\t\t#{bean." + fieldInfo.getPropertyName() + "},");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
            bw.write("\t\t</trim>");
            bw.newLine();
            bw.write("\t</insert>");
            bw.newLine();
            bw.newLine();
            bw.write("\t<!-- 插入或者更新 （匹配有值的字段）-->");
            bw.newLine();
            bw.write("\t<insert id=\"insertOrUpdate\" parameterType=\"" + poClassName + "\">");
            bw.newLine();
            bw.write("\t\tINSERT INTO " + tableInfo.getTableName());
            bw.newLine();
            //sql参数
            bw.write("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
            bw.newLine();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">");
                bw.newLine();
                bw.write("\t\t\t\t" + fieldInfo.getFieldName() + ",");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
            bw.write("\t\t</trim>");
            bw.newLine();
            //属性参数
            bw.write("\t\t<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">");
            bw.newLine();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">");
                bw.newLine();
                bw.write("\t\t\t\t#{bean." + fieldInfo.getPropertyName() + "},");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
            bw.write("\t\t</trim>");
            bw.newLine();
            //ON DUPLICATE KEY UPDATE子句，你可以指定在冲突发生时执行更新操作而不是报错。
            bw.write("\t\ton DUPLICATE key update");
            bw.newLine();
            bw.write("\t\t<trim prefix=\"\" suffix=\"\" suffixOverrides=\",\">");
            bw.newLine();
            HashMap<String, String> keyMap = new HashMap<>();
            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                List<FieldInfo> value = entry.getValue();
                for (FieldInfo item : value) {
                    keyMap.put(item.getFieldName(), item.getFieldName());
                }
            }
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
//                Boolean ifContinue = false;
//                for (FieldInfo fieldInfo1:fieldInfoList){
//                    if (fieldInfo1.getFieldName().equals(fieldInfo.getFieldName())){
//                        ifContinue = true;
//                    }
//                }
//                if (ifContinue){
//                    continue;
//                }
                if (keyMap.get(fieldInfo.getFieldName()) != null) {
                    continue;
                }
                bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + "!=null\">");
                bw.newLine();
                bw.write("\t\t\t\t" + fieldInfo.getFieldName() + " = VALUES(" + fieldInfo.getFieldName() + "),");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }

            bw.write("\t\t</trim>");
            bw.newLine();
            bw.write("\t</insert>");
            bw.newLine();
            bw.newLine();

            //批量插入
            bw.write("\t<!-- 添加 （批量插入）-->");
            bw.newLine();
            bw.write("\t<insert id=\"insertBatch\" parameterType=\"" + poClassName + "\" useGeneratedKeys=\"true\"" + " keyProperty=\"" + fieldKeyInfo.getPropertyName() + "\">");
            bw.newLine();
            bw.write("\t\tINSERT INTO " + tableInfo.getTableName() + "(");
            StringBuffer insertBuffer = new StringBuffer();
            index = 0;
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                index++;
                if (fieldInfo.getFieldName().equals(fieldKeyInfo.getFieldName()) && fieldKeyInfo.getAutoIncrement()) {
                    continue;
                }
                insertBuffer.append(fieldInfo.getFieldName());
                if (index < tableInfo.getFieldList().size()) {
                    insertBuffer.append(",");
                }
            }
            bw.write(insertBuffer.toString());
            bw.write(")values");
            bw.newLine();
            bw.write("\t\t<foreach collection=\"list\" item=\"item\" separator=\",\">");
            bw.newLine();
            bw.write("\t\t\t(");
            StringBuffer insertValueBuffer = new StringBuffer();
            index = 0;
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                index++;
                if (fieldInfo.getFieldName().equals(fieldKeyInfo.getFieldName()) && fieldKeyInfo.getAutoIncrement()) {
                    continue;
                }
                insertValueBuffer.append("#{item." + fieldInfo.getPropertyName() + "}");
                if (index < tableInfo.getFieldList().size()) {
                    insertValueBuffer.append(",");
                }
            }
            bw.write(insertValueBuffer.toString() + ")");
            bw.newLine();
            bw.write("\t\t</foreach>");
            bw.newLine();
            bw.write("\t</insert>");
            bw.newLine();
            bw.newLine();


            //批量插入或修改
            bw.write("\t<!-- 批量新增修改 （批量插入）-->");
            bw.newLine();
            bw.write("\t<insert id=\"insertOrUpdateBatch\" parameterType=\"" + poClassName + "\">");
            bw.newLine();
            bw.write("\t\tINSERT INTO " + tableInfo.getTableName() + "(");
            bw.write(insertBuffer.toString());
            bw.write(")values");
            bw.newLine();
            bw.write("\t\t<foreach collection=\"list\" item=\"item\" separator=\",\">");
            bw.newLine();
            bw.write("\t\t\t(");
            bw.write(insertValueBuffer.toString() + ")");
            bw.newLine();
            bw.write("\t\t</foreach>");
            bw.newLine();
            bw.write("\t\ton DUPLICATE key update");
            bw.newLine();
            index = 0;
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                index++;
                if (fieldInfo.getFieldName().equals(fieldKeyInfo.getFieldName()) && fieldKeyInfo.getAutoIncrement()) {
                    continue;
                }
                bw.write("\t\t" + fieldInfo.getFieldName() + " = VALUES(" + fieldInfo.getFieldName() + ")");
                if (index < tableInfo.getFieldList().size()) {
                    bw.write(",");
                }
                bw.newLine();
            }
            bw.write("\t</insert>");
            bw.newLine();
            bw.newLine();

            //根据唯一索引修改
            for (Map.Entry<String, List<FieldInfo>> entry : tableInfo.getKeyIndexMap().entrySet()) {
                List<FieldInfo> fields = entry.getValue();
                StringBuffer str2SUD = new StringBuffer();
                index = 0;
                for (FieldInfo fieldInfo : fields) {
                    index++;
                    str2SUD.append(StringUtils.processField(fieldInfo.getPropertyName(), true));
                    if (index < fields.size()) {
                        str2SUD.append("And");
                    }
                }
                StringBuffer str2SUDWhere = new StringBuffer();
                index = 0;
                for (FieldInfo fieldInfo : fields) {
                    index++;
                    str2SUDWhere.append(fieldInfo.getFieldName() + " = #{" + fieldInfo.getPropertyName() + "}");
                    if (index < fields.size()) {
                        str2SUDWhere.append(" and ");
                    }
                }
                bw.write("\t<!--根据" + str2SUD.toString() + "查询-->");
                bw.newLine();
                bw.write("\t<select id=\"selectBy");
                bw.write(str2SUD.toString());
                bw.write("\" resultMap=\"" + BASE_RESULT_MAP + "\">");
                bw.newLine();
                bw.write("\t\tselect <include refid=\"base_column_list\"/> from " + tableInfo.getTableName());
                bw.newLine();

                bw.write("\t\twhere ");
                bw.write(str2SUDWhere.toString());
                bw.newLine();
                bw.write("\t</select>");
                bw.newLine();
                bw.newLine();

                bw.write("\t<!--根据" + str2SUD.toString() + "修改-->");
                bw.newLine();
                bw.write("\t<update id=\"updateBy");
                bw.write(str2SUD.toString());
                bw.write("\" parameterType=\"" + poClassName + "\">");
                bw.newLine();
                bw.write("\t\tupdate " + tableInfo.getTableName());
                bw.newLine();
                bw.write("\t\t<set>");
                bw.newLine();
                for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                    if (keyMap.get(fieldInfo.getFieldName()) != null) {
                        continue;
                    }
                    bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">");
                    bw.newLine();
                    bw.write("\t\t\t\t" + fieldInfo.getFieldName() + " = #{bean." + fieldInfo.getPropertyName() + "},");
                    bw.newLine();
                    bw.write("\t\t\t</if>");
                    bw.newLine();
                }
                bw.write("\t\t</set>");
                bw.newLine();
                bw.write("\t\twhere ");
                bw.write(str2SUDWhere.toString());
                bw.newLine();
                bw.write("\t</update>");
                bw.newLine();
                bw.newLine();

                bw.write("\t<!--根据" + str2SUD.toString() + "删除-->");
                bw.newLine();
                bw.write("\t<delete id=\"deleteBy");
                bw.write(str2SUD.toString() + "\">");
                bw.newLine();
                bw.write("\t\tdelete from " + tableInfo.getTableName());
                bw.newLine();
                bw.write("\t\twhere ");
                bw.write(str2SUDWhere.toString());
                bw.newLine();
                bw.write("\t</delete>");
                bw.newLine();
                bw.newLine();
            }
            bw.write("</mapper>");
            bw.flush();
        } catch (Exception e) {
            logger.error("生成mapperXml文件失败");
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outw != null) {
                try {
                    outw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void createQueryCondition(BufferedWriter bw, List<FieldInfo> fieldInfos, Boolean isExtend) throws IOException {
        for (FieldInfo fieldInfo : fieldInfos) {
            if (fieldInfo.getJavaType().equals("String")) {
                bw.write("\t\t<if test=\"query." + fieldInfo.getPropertyName() + " != null and query." + fieldInfo.getPropertyName() + "!=''\">");
                bw.newLine();
            } else {
                bw.write("\t\t<if test=\"query." + fieldInfo.getPropertyName() + " != null\">");
                bw.newLine();
            }
            if (isExtend && fieldInfo.getJavaType().equals("String")) {
                bw.write("\t\t\tand  " + fieldInfo.getFieldName() + " like concat('%', #{query." + fieldInfo.getPropertyName() + "}, '%')");
                bw.newLine();
            } else if (fieldInfo.getJavaType().equals("Date") && fieldInfo.getPropertyName().indexOf("Start") >= 0) {
                bw.write("\t\t\t<![CDATA[ and  " + fieldInfo.getFieldName() + ">=str_to_date(#{query." + fieldInfo.getPropertyName() + "}, '%Y-%m-%d') ]]>");
                bw.newLine();
            } else if (fieldInfo.getJavaType().equals("Date") && fieldInfo.getPropertyName().indexOf("End") >= 0) {
                bw.write("\t\t\t<![CDATA[ and  " + fieldInfo.getFieldName() + "< date_sub(str_to_date(#{query." + fieldInfo.getPropertyName() + "}, '%Y-%m-%d'),interval -1 day) ]]>");
                bw.newLine();
            } else if (fieldInfo.getJavaType().equals("Date") && fieldInfo.getPropertyName().indexOf("End") < 0 && fieldInfo.getPropertyName().indexOf("Start") < 0) {
                bw.write("\t\t\t<![CDATA[ and  " + fieldInfo.getFieldName() + "=str_to_date(#{query." + fieldInfo.getPropertyName() + "}, '%Y-%m-%d') ]]>");
                bw.newLine();
            } else {
                bw.write("\t\t\tand " + fieldInfo.getFieldName() + " = #{query." + fieldInfo.getPropertyName() + "}");
                bw.newLine();
            }
            bw.write("\t\t</if>");
            bw.newLine();
        }
    }
}
