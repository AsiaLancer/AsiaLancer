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

public class BuildController {
    private static final Logger logger = LoggerFactory.getLogger(BuildController.class);

    public static void execute(TableInfo tableInfo) {
        File controllerPath = new File(Constants.PATH_CONTROLLER);
        if (!controllerPath.exists()) {
            controllerPath.mkdirs();
        }
        String className = tableInfo.getBeanName() + StringUtils.processField(Constants.BEAN_CONTROLLER_SUFFIX, true);
        File paramFile = new File(controllerPath, className + ".java");

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;

        String serviceName = tableInfo.getBeanName() + StringUtils.processField(Constants.BEAN_SERVICE_SUFFIX, true);
        String lowerServiceName = StringUtils.lowerStr(serviceName, true);
        String beanName = tableInfo.getBeanName();
        String beanParamName = tableInfo.getBeanParamName();
        try {
            out = new FileOutputStream(paramFile);
            outw = new OutputStreamWriter(out, "utf8");
            bw = new BufferedWriter(outw);

            bw.write("package " + Constants.PACKAGE_CONTROLLER + ";");
            bw.newLine();
            bw.newLine();
            bw.write("import java.util.List;");
            bw.newLine();
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_SERVICE + "." + serviceName + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_PO + "." + beanName + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_QUERY + "." + beanParamName + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_VO + ".PaginationResultVO;");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_VO + ".ResponseVO;");
            bw.newLine();
            bw.write("import org.springframework.web.bind.annotation.RequestBody;");
            bw.newLine();
            bw.write("import org.springframework.web.bind.annotation.RequestMapping;");
            bw.newLine();
            bw.write("import org.springframework.web.bind.annotation.RestController;");
            bw.newLine();
            bw.write("import javax.annotation.Resource;");
            bw.newLine();
            bw.newLine();

            BuildComment.createClassComment(bw, tableInfo, "Controller");
            bw.write("@RestController(\"" + StringUtils.lowerStr(className, true) + "\")");
            bw.newLine();
            bw.write("@RequestMapping(\"/" + StringUtils.lowerStr(beanName, true) + "\")");
            bw.newLine();
            bw.write("public class " + className + " extends ABaseController {");
            bw.newLine();
            bw.newLine();

            bw.write("\t@Resource");
            bw.newLine();
            bw.write("\tprivate " + serviceName + " " + lowerServiceName + ";");
            bw.newLine();
            bw.newLine();

            BuildComment.createMethodComment(bw, new ArrayList<>(), "条件分页查询");
            bw.write("\t@RequestMapping(\"/loadDataList\")");
            bw.newLine();
            bw.write("\tpublic ResponseVO loadDataList(" + beanParamName + " query) {");
            bw.newLine();
            bw.write("\t\treturn getSuccessResponseVO(" + lowerServiceName + ".findListByPage(query));");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();

            BuildComment.createMethodComment(bw, new ArrayList<>(), "新增");
            bw.write("\t@RequestMapping(\"/add\")");
            bw.newLine();
            bw.write("\tpublic ResponseVO add(" + beanName + " bean) {");
            bw.newLine();
            bw.write("\t\t" + lowerServiceName + ".add(bean);");
            bw.newLine();
            bw.write("\t\treturn getSuccessResponseVO(null);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();

            BuildComment.createMethodComment(bw, new ArrayList<>(), "批量新增");
            bw.write("\t@RequestMapping(\"/addBatch\")");
            bw.newLine();
            bw.write("\tpublic ResponseVO addBatch(@RequestBody List<" + beanName + "> listBean) {");
            bw.newLine();
            bw.write("\t\t" + lowerServiceName + ".addBatch(listBean);");
            bw.newLine();
            bw.write("\t\treturn getSuccessResponseVO(null);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();

            BuildComment.createMethodComment(bw, new ArrayList<>(), "批量新增或修改");
            bw.write("\t@RequestMapping(\"/addOrUpdateBatch\")");
            bw.newLine();
            bw.write("\tpublic ResponseVO addOrUpdateBatch(@RequestBody List<" + beanName + "> listBean) {");
            bw.newLine();
            bw.write("\t\t" + lowerServiceName + ".addOrUpdateBatch(listBean);");
            bw.newLine();
            bw.write("\t\treturn getSuccessResponseVO(null);");
            bw.newLine();
            bw.write("\t}");
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
                StringBuffer str2SUDLower = new StringBuffer();
                index = 0;
                for (FieldInfo fieldInfo : fields) {
                    index++;
                    str2SUDArgs.append(fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName());
                    str2SUDLower.append(StringUtils.lowerStr(fieldInfo.getPropertyName(), true));
                    if (index < fields.size()) {
                        str2SUDArgs.append(",");
                        str2SUDLower.append(",");
                    }
                }
                //查询
                BuildComment.createMethodComment(bw, new ArrayList<>(), str2SUD + "查询对象");
                bw.write("\t@RequestMapping(\"/get"+beanName+"By"+str2SUD+"\")");
                bw.newLine();
                bw.write("\tpublic ResponseVO get" + beanName + "By" + str2SUD + "(" + str2SUDArgs + ") {");
                bw.newLine();
                bw.write("\t\treturn getSuccessResponseVO("+lowerServiceName+".get"+beanName+"By"+str2SUD+"("+str2SUDLower+"));");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
                bw.newLine();

                //修改
                BuildComment.createMethodComment(bw, new ArrayList<>(), str2SUD + "修改更新对象");
                bw.write("\t@RequestMapping(\"/update"+beanName+"By"+str2SUD+"\")");
                bw.newLine();
                bw.write("\tpublic ResponseVO update" + beanName + "By" + str2SUD + "(" + beanName + " bean," + str2SUDArgs + ") {");
                bw.newLine();
                bw.write("\t\t"+lowerServiceName+".update"+beanName+"By"+str2SUD+"(bean, "+str2SUDLower+");");
                bw.newLine();
                bw.write("\t\treturn getSuccessResponseVO(null);");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
                bw.newLine();

                //删除
                BuildComment.createMethodComment(bw, new ArrayList<>(), str2SUD + "删除对象");
                bw.write("\t@RequestMapping(\"/delete"+beanName+"By"+str2SUD+"\")");
                bw.newLine();
                bw.write("\tpublic ResponseVO delete" + beanName + "By" +str2SUD+ "(" + str2SUDArgs + ") {");
                bw.newLine();
                bw.write("\t\t"+lowerServiceName+".delete"+beanName+"By"+str2SUD+"("+str2SUDLower+");");
                bw.newLine();
                bw.write("\t\treturn getSuccessResponseVO(null);");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
                bw.newLine();
            }



            bw.write("}");
            bw.flush();

        } catch (
                Exception e) {
            logger.error("生成Controller文件失败");
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
