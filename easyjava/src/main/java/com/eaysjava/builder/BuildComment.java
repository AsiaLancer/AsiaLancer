package com.eaysjava.builder;

import com.eaysjava.bean.Constants;
import com.eaysjava.bean.FieldInfo;
import com.eaysjava.bean.TableInfo;
import com.eaysjava.enums.Sql2JavaTypeEnum;
import com.eaysjava.utils.DateUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;


public class BuildComment {
    public static final Logger logger = LoggerFactory.getLogger(BuildComment.class);

    public static void createClassComment(BufferedWriter bw, TableInfo tableInfo, String str) {

        try {
            bw.write("/**");
            bw.newLine();
            bw.write(" * @Description:" + tableInfo.getComment() + str);
            bw.newLine();
            bw.write(" * @date:" + DateUtils.format(new Date(), DateUtils.YYYYMMdd));
            bw.newLine();
            bw.write(" * @author:" + Constants.PROJECT_AUTHOR);
            bw.newLine();
            bw.write(" */");
            bw.newLine();
        } catch (Exception e) {
            logger.error("类注解写入失败");
        }

    }

    public static void createFieldComment(BufferedWriter bw, TableInfo tableInfo) throws IOException {
        for (FieldInfo fieldInfo : tableInfo.getFieldList()) {

            if (fieldInfo.getComment() != null || !fieldInfo.getComment().equals("")) {
                bw.write("\t/**");
                bw.newLine();
                bw.write("\t * " + fieldInfo.getComment() + " " + fieldInfo.getPropertyName());
                bw.newLine();
                bw.write("\t */");
                bw.newLine();
            }
            if (ArrayUtils.contains(Sql2JavaTypeEnum.DATE_TIME.getSqlType(), fieldInfo.getSqlType())) {
                bw.write("\t" + String.format(Constants.BEAN_DATE_FORMAT, DateUtils.YYYY_MM_DD_HH_MM_SS));
                bw.newLine();
                bw.write("\t" + String.format(Constants.BEAN_DATE_EXPRESSION, DateUtils.YYYY_MM_DD_HH_MM_SS));
                bw.newLine();
            }
            if (ArrayUtils.contains(Sql2JavaTypeEnum.DATE.getSqlType(), fieldInfo.getSqlType())) {
                bw.write("\t" + String.format(Constants.BEAN_DATE_FORMAT, DateUtils.YYYY_MM_DD));
                bw.newLine();
                bw.write("\t" + String.format(Constants.BEAN_DATE_EXPRESSION, DateUtils.YYYY_MM_DD));
                bw.newLine();
            }
            if (ArrayUtils.contains(Constants.IGNORE_TABLE_ToJASON_FIELD.split(","), fieldInfo.getPropertyName())) {
                bw.write("\t" + Constants.IGNORE_TABLE_ToJASON_EXPRESSION);
                bw.newLine();
            }
            bw.write("\tprivate " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ";");
            bw.newLine();
        }
    }

    public static void createMethodComment(BufferedWriter bw, List<FieldInfo> keyFieldInfoList, String addStr) {
        try {
            bw.write("\t/**");
            bw.newLine();
            bw.write("\t * 根据");
            Integer index = 0;
            if (keyFieldInfoList.size() > 0) {
                for (FieldInfo fieldInfo : keyFieldInfoList) {
                    index++;
                    bw.write(fieldInfo.getPropertyName());
                    if (index < keyFieldInfoList.size()) {
                        bw.write("和");
                    }
                }
            }
            bw.write(addStr);
            bw.newLine();
            bw.write("\t */");
            bw.newLine();
        } catch (IOException e) {
            logger.error("写入方法注解失败");
        }
    }

}
