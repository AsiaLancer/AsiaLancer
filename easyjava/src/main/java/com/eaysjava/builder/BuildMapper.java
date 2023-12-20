package com.eaysjava.builder;

import com.eaysjava.bean.Constants;
import com.eaysjava.bean.FieldInfo;
import com.eaysjava.bean.TableInfo;
import com.eaysjava.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;

public class BuildMapper {
    private static final Logger logger = LoggerFactory.getLogger(BuildMapper.class);

    public static void execute(TableInfo tableInfo) {
        File mapperPath = new File(Constants.PATH_BASE_MAPPERS);
        if (!mapperPath.exists()) {
            mapperPath.mkdirs();
        }
        String mapperName = tableInfo.getBeanName() + StringUtils.processField(Constants.BEAN_MAPPER_SUFFIX, true);
        File mapperFile = new File(mapperPath, mapperName + ".java");

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(mapperFile);
            outw = new OutputStreamWriter(out, "utf8");
            bw = new BufferedWriter(outw);
            bw.write("package " + Constants.PACKAGE_BASE_MAPPERS + ";");
            bw.newLine();
            bw.newLine();
            bw.write("import org.apache.ibatis.annotations.Param;");
            bw.newLine();
            bw.newLine();
            BuildComment.createClassComment(bw, tableInfo,"Mapper");
            bw.write("public interface " + mapperName + "<T, P> extends BaseMapper<T, P> {");
            bw.newLine();
            bw.newLine();
            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
//            T selectByUserId(@Param("userId") String userId);
            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                StringBuilder methodStr = new StringBuilder();
                List<FieldInfo> keyFieldInfoList = entry.getValue();
                Integer index = 0;
                for (FieldInfo fieldInfo : keyFieldInfoList) {
                    index++;
                    methodStr.append(StringUtils.processField(fieldInfo.getPropertyName(), true));
                    if (index < keyFieldInfoList.size()) {
                        methodStr.append("And");
                    }
                }
                methodStr.append("(");
                index = 0;
                for (FieldInfo fieldInfo : keyFieldInfoList) {
                    index++;
                    methodStr.append("@Param(\"" + fieldInfo.getPropertyName() + "\") " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName());
                    if (index < keyFieldInfoList.size()) {
                        methodStr.append(", ");
                    }
                }

                BuildComment.createMethodComment(bw, keyFieldInfoList, "查询");
                bw.write("\tT selectBy"+methodStr.toString()+");");
                bw.newLine();
                bw.newLine();

                BuildComment.createMethodComment(bw, keyFieldInfoList, "更新");
                bw.write("\tInteger updateBy"+methodStr.toString()+", @Param(\"bean\") T t);");
                bw.newLine();
                bw.newLine();

                BuildComment.createMethodComment(bw, keyFieldInfoList, "删除");
                bw.write("\tInteger deleteBy"+methodStr.toString()+");");
                bw.newLine();
                bw.newLine();



            }
            bw.newLine();
            bw.write("}");
            bw.flush();
        } catch (Exception e) {
            logger.error("生成mapper文件失败");
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
