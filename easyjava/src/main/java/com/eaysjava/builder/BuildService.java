package com.eaysjava.builder;

import com.eaysjava.bean.Constants;
import com.eaysjava.bean.FieldInfo;
import com.eaysjava.bean.TableInfo;
import com.eaysjava.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BuildService {

    private static final Logger logger = LoggerFactory.getLogger(BuildService.class);

    public static void execute(TableInfo tableInfo) {
        File servicePath = new File(Constants.PATH_SERVICE);
        if (!servicePath.exists()) {
            servicePath.mkdirs();
        }
        String className = tableInfo.getBeanName() + StringUtils.processField(Constants.BEAN_SERVICE_SUFFIX, true);
        File paramFile = new File(servicePath, className + ".java");

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(paramFile);
            outw = new OutputStreamWriter(out, "utf8");
            bw = new BufferedWriter(outw);

            bw.write("package " + Constants.PACKAGE_SERVICE + ";");
            bw.newLine();
            bw.newLine();

            bw.write("import " + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_QUERY + "." + tableInfo.getBeanParamName() + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_VO + ".PaginationResultVO;");
            bw.newLine();
            bw.newLine();
            bw.write("import java.util.List;");
            bw.newLine();
            bw.newLine();
            BuildComment.createClassComment(bw, tableInfo, "Service接口");
            bw.write("public interface " + className + "{");
            bw.newLine();
            bw.newLine();

            BuildComment.createMethodComment(bw, new ArrayList<>(), "条件查询列表");
            bw.write("\tList<" + tableInfo.getBeanName() + "> findListByParam(" + tableInfo.getBeanParamName() + " param);");
            bw.newLine();
            bw.newLine();

            BuildComment.createMethodComment(bw, new ArrayList<>(), "条件查询列表");
            bw.write("\tInteger findCountByParam(" + tableInfo.getBeanParamName() + " param);");
            bw.newLine();
            bw.newLine();

            BuildComment.createMethodComment(bw, new ArrayList<>(), "分页查询");
            bw.write("\tPaginationResultVO<" + tableInfo.getBeanName() + "> findListByPage(" + tableInfo.getBeanParamName() + " param);");
            bw.newLine();
            bw.newLine();

            BuildComment.createMethodComment(bw, new ArrayList<>(), "新增");
            bw.write("\tInteger add(" + tableInfo.getBeanName() + " bean);");
            bw.newLine();
            bw.newLine();

            BuildComment.createMethodComment(bw, new ArrayList<>(), "批量新增");
            bw.write("\tInteger addBatch(List<" + tableInfo.getBeanName() + "> listBean);");
            bw.newLine();
            bw.newLine();

            BuildComment.createMethodComment(bw, new ArrayList<>(), "批量新增/修改");
            bw.write("\tInteger addOrUpdateBatch(List<" + tableInfo.getBeanName() + "> listBean);");
            bw.newLine();
            bw.newLine();
            Integer index = null;
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
                StringBuffer str2SUDArgs = new StringBuffer();
                index = 0;
                for (FieldInfo fieldInfo : fields) {
                    index++;
                    str2SUDArgs.append(fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName());
                    if (index < fields.size()) {
                        str2SUDArgs.append(",");
                    }
                }


                //查询
                BuildComment.createMethodComment(bw, new ArrayList<>(), str2SUD + "查询对象");
                bw.write("\t" + tableInfo.getBeanName() + " get" + tableInfo.getBeanName() + "By" + str2SUD + "("+str2SUDArgs+");");
                bw.newLine();
                bw.newLine();
                //修改
                BuildComment.createMethodComment(bw, new ArrayList<>(), str2SUD + "修改");
                bw.write("\tInteger update" + tableInfo.getBeanName() + "By" + str2SUD + "("+tableInfo.getBeanName()+" bean,"+str2SUDArgs+");");
                bw.newLine();
                bw.newLine();
                //删除
                BuildComment.createMethodComment(bw, new ArrayList<>(), str2SUD + "删除");
                bw.write("\tInteger delete" + tableInfo.getBeanName() + "By" + str2SUD + "("+str2SUDArgs+");");
                bw.newLine();
                bw.newLine();
            }


            bw.write("}");
            bw.flush();

        } catch (
                Exception e) {
            logger.error("生成Service文件失败");
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
