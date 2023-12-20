package com.eaysjava.builder;

import com.eaysjava.bean.Constants;
import com.eaysjava.bean.FieldInfo;
import com.eaysjava.bean.TableInfo;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class BuildPo {

    private static final Logger logger = LoggerFactory.getLogger(BuildPo.class);

    /**
     * @param tableInfo
     */
    public static void execute(TableInfo tableInfo) {
        File poFolder = new File(Constants.PATH_PO);
        if (!poFolder.exists()) {
            poFolder.mkdirs();
        }
        File poFile = new File(poFolder, tableInfo.getBeanName() + ".java");
//        try {
//            poFile.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(poFile);
            outw = new OutputStreamWriter(out, "utf8");
            bw = new BufferedWriter(outw);
            bw.write("package " + Constants.PACKAGE_PO + ";");
            bw.newLine();
            bw.newLine();
            bw.write("import java.io.Serializable;");
            bw.newLine();
            if (tableInfo.getHavaDateTime() || tableInfo.getHaveDate()) {

                bw.write("import "+Constants.PACKAGE_UTILS+".DateUtils;");
                bw.newLine();
                bw.write(Constants.BEAN_DATE_FORMAT_CLASS);
                bw.newLine();
                bw.write(Constants.BEAN_DATE_EXPRESSION_CLASS);
                bw.newLine();
                bw.write("import java.util.Date;");
                bw.newLine();

            }
            Boolean haveIgnoreBean = false;
            for (FieldInfo fieldInfo:tableInfo.getFieldList()){
                if (ArrayUtils.contains(Constants.IGNORE_TABLE_ToJASON_FIELD.split(","),fieldInfo.getPropertyName())){
                  haveIgnoreBean = true;
                  break;
                }
            }
            if (haveIgnoreBean){
                bw.write(Constants.IGNORE_TABLE_ToJASON_CLASS);
                bw.newLine();
            }
            if (tableInfo.getHaveBigDecimal()) {
                bw.write("import java.math.BigDecimal;");
            }
            bw.newLine();
            bw.newLine();

            BuildComment.createClassComment(bw, tableInfo,"实体类");
            bw.write("public class " + tableInfo.getBeanName() + " implements Serializable {");
            bw.newLine();
            bw.newLine();
            BuildComment.createFieldComment(bw, tableInfo);
            bw.newLine();
            BuildSetGet.createGetsSets(bw,tableInfo);
            BuildSetGet.createToString(bw,tableInfo);
            bw.write("}");
            bw.flush();
        } catch (Exception e) {
            logger.error("生成po文件失败");
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
