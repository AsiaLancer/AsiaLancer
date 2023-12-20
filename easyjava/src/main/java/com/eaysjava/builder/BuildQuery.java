package com.eaysjava.builder;

import com.eaysjava.bean.Constants;
import com.eaysjava.bean.FieldInfo;
import com.eaysjava.bean.TableInfo;
import com.eaysjava.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class BuildQuery {

    private static final Logger logger = LoggerFactory.getLogger(BuildQuery.class);

    /**
     * @param tableInfo
     */
    public static void execute(TableInfo tableInfo) {
        File paramPath = new File(Constants.PATH_QUERY);
        if (!paramPath.exists()) {
            paramPath.mkdirs();
        }
        String className = tableInfo.getBeanParamName();
        File paramFile = new File(paramPath, className + ".java");

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(paramFile);
            outw = new OutputStreamWriter(out, "utf8");
            bw = new BufferedWriter(outw);
            bw.write("package " + Constants.PACKAGE_QUERY + ";");
            bw.newLine();
            bw.newLine();


            if (tableInfo.getHavaDateTime() || tableInfo.getHaveDate()) {
                bw.write("import java.util.Date;");
                bw.newLine();

            }
            if (tableInfo.getHaveBigDecimal()) {
                bw.write("import java.math.BigDecimal;");
            }
            bw.newLine();
            bw.newLine();
            BuildComment.createClassComment(bw, tableInfo,"查询对象");
            bw.write("public class " + className + " extends BaseQuery {");
            bw.newLine();
//            List<FieldInfo> extendList = new ArrayList<>();第二种
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {

                if (fieldInfo.getComment() != null || !fieldInfo.getComment().equals("")) {
                    bw.write("\t/**");
                    bw.newLine();
                    bw.write("\t * " + fieldInfo.getComment() + " " + fieldInfo.getPropertyName());
                    bw.newLine();
                    bw.write("\t */");
                    bw.newLine();
                }
                bw.write("\tprivate " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ";");
                bw.newLine();
                if (fieldInfo.getJavaType().equals("String")) {
                    bw.newLine();
                    bw.write("\tprivate " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + StringUtils.processField(Constants.BEAN_QUERY_FUZZY_SUFFIX,true)+";");
                    bw.newLine();

//                    FieldInfo fieldInfoFuzzy = new FieldInfo(); //第二种添加set/get的方法
//                    fieldInfoFuzzy.setJavaType(Sql2JavaTypeEnum.STRING.getJavaType());
//                    fieldInfoFuzzy.setPropertyName(fieldInfo.getPropertyName()+StringUtils.processField(Constants.BEAN_QUERY_FUZZY_SUFFIX,true));
//                    extendList.add(fieldInfoFuzzy);
                }
                if (fieldInfo.getJavaType().equals("Date")){
                    bw.newLine();
                    bw.write("\tprivate String" + " " + fieldInfo.getPropertyName() + StringUtils.processField(Constants.BEAN_QUERY_TIME_START_SUFFIX,true)+";");
                    bw.newLine();
                    bw.newLine();
                    bw.write("\tprivate String" + " " + fieldInfo.getPropertyName() + StringUtils.processField(Constants.BEAN_QUERY_TIME_END_SUFFIX,true)+";");
                    bw.newLine();
                }
            }
            bw.newLine();
//            List<FieldInfo> fieldList = tableInfo.getFieldList();第二种
//            fieldList.addAll(extendList);
//            BuildSetGet.createGetsSets(bw,tableInfo);
            BuildSetGet.createQueryGetsSets(bw,tableInfo);
            bw.write("}");
            bw.flush();
        } catch (
                Exception e) {
            logger.error("生成Query文件失败");
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
}
