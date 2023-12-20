package com.eaysjava.builder;

import com.eaysjava.bean.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BuildBase {
    private static final Logger logger = LoggerFactory.getLogger(BuildBase.class);

    public static void execute() {
        //生成DateUtils
        List<String> headPackageList = new ArrayList<>();
//        List<String> headImportList = new ArrayList<>();
        headPackageList.add("package " + Constants.PACKAGE_UTILS);
        build(headPackageList, "DateUtils", Constants.PATH_UTILS);
        //生成BaseMapper
        headPackageList.clear();
        headPackageList.add("package " + Constants.PACKAGE_BASE_MAPPERS);
        build(headPackageList, "BaseMapper", Constants.PATH_BASE_MAPPERS);
        //生成BaseQuery
        headPackageList.clear();
        headPackageList.add("package " + Constants.PACKAGE_QUERY);
        build(headPackageList, "BaseQuery", Constants.PATH_QUERY);
        //生成SimplePage
        headPackageList.clear();
        headPackageList.add("package " + Constants.PACKAGE_QUERY);
        headPackageList.add("import " + Constants.PACKAGE_ENUMS + ".PageSize");
        build(headPackageList, "SimplePage", Constants.PATH_QUERY);
        //生成PageSize
        headPackageList.clear();
        headPackageList.add("package " + Constants.PACKAGE_ENUMS);
        build(headPackageList, "PageSize", Constants.PATH_ENUMS);
        //生成PaginationResultVO
        headPackageList.clear();
        headPackageList.add("package " + Constants.PACKAGE_VO);
        headPackageList.add("import java.util.ArrayList");
        headPackageList.add("import java.util.List");
        build(headPackageList, "PaginationResultVO", Constants.PATH_VO);
        //生成ResponseVO
        headPackageList.clear();
        headPackageList.add("package " + Constants.PACKAGE_VO);
        build(headPackageList, "ResponseVO", Constants.PATH_VO);
        //生成ResponseCodeEnum
        headPackageList.clear();
        headPackageList.add("package " + Constants.PACKAGE_ENUMS);
        build(headPackageList, "ResponseCodeEnum", Constants.PATH_ENUMS);
        //生成ABaseController
        headPackageList.clear();
        headPackageList.add("package " + Constants.PACKAGE_CONTROLLER);
        headPackageList.add("import "+Constants.PACKAGE_ENUMS+".ResponseCodeEnum");
        headPackageList.add("import "+Constants.PACKAGE_VO+".ResponseVO");
        headPackageList.add("import "+Constants.PACKAGE_EXCEPTION+".BusinessException");
        build(headPackageList, "ABaseController", Constants.PATH_CONTROLLER);
        //生成BusinessException
        headPackageList.clear();
        headPackageList.add("package " + Constants.PACKAGE_EXCEPTION);
        headPackageList.add("import "+Constants.PACKAGE_ENUMS+".ResponseCodeEnum");
        build(headPackageList, "BusinessException", Constants.PATH_EXCEPTION);
        //生成AGlobalExceptionHandlerController
        headPackageList.clear();
        headPackageList.add("package " + Constants.PACKAGE_CONTROLLER);
        headPackageList.add("import "+Constants.PACKAGE_ENUMS+".ResponseCodeEnum");
        headPackageList.add("import "+Constants.PACKAGE_VO+".ResponseVO");
        headPackageList.add("import "+Constants.PACKAGE_EXCEPTION+".BusinessException");
        build(headPackageList, "AGlobalExceptionHandlerController", Constants.PATH_CONTROLLER);

    }

    private static void build(List<String> headPackageList, String fileName, String outPutPath) {
        File outPutFolder = new File(outPutPath);
        if (!outPutFolder.exists()) {
            outPutFolder.mkdirs();
        }
        File file = new File(outPutPath, fileName + ".java");
        OutputStream out = null;
        OutputStreamWriter outW = null;
        BufferedWriter bw = null;

        InputStream in = null;
        InputStreamReader inR = null;
        BufferedReader br = null;

        try {

            out = new FileOutputStream(file);
            outW = new OutputStreamWriter(out);
            bw = new BufferedWriter(outW);

            in = BuildBase.class.getClassLoader().getResourceAsStream("templates/" + fileName + ".txt");
            inR = new InputStreamReader(in);
            br = new BufferedReader(inR);
            String brRead = null;
            for (String head : headPackageList) {
                bw.write(head+";");
                bw.newLine();
                bw.newLine();
            }
            while ((brRead = br.readLine()) != null) {
                bw.write(brRead);
                bw.newLine();
            }
            bw.flush();
        } catch (Exception e) {
            logger.error("基础模板类写入失败");
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outW != null) {
                try {
                    outW.close();
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

            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inR != null) {
                try {
                    inR.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
