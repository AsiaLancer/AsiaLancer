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

public class BuildServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(BuildServiceImpl.class);

    public static void execute(TableInfo tableInfo) {
        File serviceImplPath = new File(Constants.PATH_SERVICE_IMPL);
        if (!serviceImplPath.exists()) {
            serviceImplPath.mkdirs();
        }
        String beanName = tableInfo.getBeanName();
        String beanParamName = tableInfo.getBeanParamName();
        String className = beanName + StringUtils.processField(Constants.BEAN_SERVICE_IMPL_SUFFIX, true);
        String implementClassName = beanName + StringUtils.processField(Constants.BEAN_SERVICE_SUFFIX, true);
        String mapperClassName = beanName + StringUtils.processField(Constants.BEAN_MAPPER_SUFFIX, true);
        String lowerMapperName = StringUtils.lowerStr(mapperClassName, true);
        File paramFile = new File(serviceImplPath, className + ".java");

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(paramFile);
            outw = new OutputStreamWriter(out, "utf8");
            bw = new BufferedWriter(outw);

            bw.write("package " + Constants.PACKAGE_SERVICE_IMPL + ";");
            bw.newLine();
            bw.newLine();

            bw.write("import " + Constants.PACKAGE_BASE_MAPPERS + "." + mapperClassName + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_PO + "." + beanName + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_QUERY + "." + beanParamName + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_ENUMS + ".PageSize;");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_QUERY + ".SimplePage;");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_VO + ".PaginationResultVO;");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_SERVICE + "."+implementClassName+";");
            bw.newLine();
            bw.write("import org.springframework.stereotype.Service;");
            bw.newLine();
            bw.newLine();
            bw.write("import javax.annotation.Resource;");
            bw.newLine();
            bw.write("import java.util.List;");
            bw.newLine();
            bw.newLine();
            BuildComment.createClassComment(bw, tableInfo, "ServiceImpl实现类");
            bw.write("@Service(\""+StringUtils.lowerStr(implementClassName,true)+"\")");
            bw.newLine();
            bw.write("public class " + className + " implements " + implementClassName + "{");
            bw.newLine();
            bw.newLine();

            bw.write("\t@Resource");
            bw.newLine();
            bw.write("\tprivate "+mapperClassName+"<"+ beanName +", "+ beanParamName +"> "+ lowerMapperName +";");
            bw.newLine();
            bw.newLine();

            BuildComment.createMethodComment(bw, new ArrayList<>(), "条件查询列表");
            bw.write("\tpublic List<" + beanName + "> findListByParam(" + beanParamName + " param) {");
            bw.newLine();
            bw.write("\t\treturn this."+lowerMapperName+".selectList(param);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            BuildComment.createMethodComment(bw, new ArrayList<>(), "条件查询列表");
            bw.write("\tpublic Integer findCountByParam(" + beanParamName + " param) {");
            bw.newLine();
            bw.write("\t\treturn this."+lowerMapperName+".selectCount(param);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            BuildComment.createMethodComment(bw, new ArrayList<>(), "分页查询");
            bw.write("\tpublic PaginationResultVO<" + beanName + "> findListByPage(" + beanParamName + " param) {");
            bw.newLine();
            bw.write("\t\tint count = this.findCountByParam(param);");
            bw.newLine();
            bw.write("\t\tInteger pageSize = param.getPageSize()==null?PageSize.SIZE15.getSize():param.getPageSize();");
            bw.newLine();
            bw.write("\t\tSimplePage page = new SimplePage(param.getPageNo(), count, pageSize);");
            bw.newLine();
            bw.write("\t\tparam.setSimplePage(page);");
            bw.newLine();
            bw.write("\t\tList<"+beanName+"> list = this.findListByParam(param);");
            bw.newLine();
            bw.write("\t\tPaginationResultVO<"+beanName+"> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(),page.getPageTotal(), list);");
            bw.newLine();
            bw.write("\t\treturn result;");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            BuildComment.createMethodComment(bw, new ArrayList<>(), "新增");
            bw.write("\tpublic Integer add(" + beanName + " bean) {");
            bw.newLine();
            bw.write("\t\treturn this."+lowerMapperName+".insert(bean);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            BuildComment.createMethodComment(bw, new ArrayList<>(), "批量新增");
            bw.write("\tpublic Integer addBatch(List<" + beanName + "> listBean) {");
            bw.newLine();
            bw.write("\t\tif (listBean == null || listBean.isEmpty()) {");
            bw.newLine();
            bw.write("\t\t\treturn 0;");
            bw.newLine();
            bw.write("\t\t}");
            bw.newLine();
            bw.write("\t\treturn this."+lowerMapperName+".insertBatch(listBean);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            BuildComment.createMethodComment(bw, new ArrayList<>(), "批量新增/修改");
            bw.write("\tpublic Integer addOrUpdateBatch(List<" + beanName + "> listBean) {");
            bw.newLine();
            bw.write("\t\tif (listBean == null || listBean.isEmpty()) {");
            bw.newLine();
            bw.write("\t\t\treturn 0;");
            bw.newLine();
            bw.write("\t\t}");
            bw.newLine();
            bw.write("\t\treturn this."+lowerMapperName+".insertOrUpdateBatch(listBean);");
            bw.newLine();
            bw.write("\t}");
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
                StringBuffer str2SUDLower = new StringBuffer();
                index = 0;
                for (FieldInfo fieldInfo : fields) {
                    index++;
                    str2SUDArgs.append(fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName());
                    str2SUDLower.append(StringUtils.lowerStr(fieldInfo.getPropertyName(),true));
                    if (index < fields.size()) {
                        str2SUDArgs.append(",");
                        str2SUDLower.append(",");
                    }
                }


                //查询
                BuildComment.createMethodComment(bw, new ArrayList<>(), str2SUD + "查询对象");
                bw.write("\tpublic " + beanName + " get" + beanName + "By" + str2SUD + "(" + str2SUDArgs + ") {");
                bw.newLine();
                bw.write("\t\treturn this."+lowerMapperName+".selectBy"+str2SUD+"("+str2SUDLower+");");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
                bw.newLine();
                //修改
                BuildComment.createMethodComment(bw, new ArrayList<>(), str2SUD + "修改");
                bw.write("\tpublic Integer update" + beanName + "By" + str2SUD + "(" + beanName + " bean," + str2SUDArgs + ") {");
                bw.newLine();
                bw.write("\t\treturn this."+lowerMapperName+".updateBy"+str2SUD+"("+str2SUDLower+",bean);");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
                bw.newLine();
                //删除
                BuildComment.createMethodComment(bw, new ArrayList<>(), str2SUD + "删除");
                bw.write("\tpublic Integer delete" + beanName + "By" + str2SUD + "(" + str2SUDArgs + ") {");
                bw.newLine();
                bw.write("\t\treturn this."+lowerMapperName+".deleteBy"+str2SUD+"("+str2SUDLower+");");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
                bw.newLine();
            }
            bw.write("}");
            bw.flush();

        } catch (
                Exception e) {
            logger.error("生成ServiceImpl文件失败");
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
