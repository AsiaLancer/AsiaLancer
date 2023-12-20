package com.eaysjava.builder;

import com.eaysjava.bean.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class BuildStarter {
    private static final Logger logger = LoggerFactory.getLogger(BuildStarter.class);

    public static void execute() {


        File starterPath = new File(Constants.PATH_BASE_JAVA);
        if (!starterPath.exists()) {
            starterPath.mkdirs();
        }
        String className = Constants.STARTER_APPLICATION;
        File paramFile = new File(starterPath, className + ".java");

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(paramFile);
            outw = new OutputStreamWriter(out, "utf8");
            bw = new BufferedWriter(outw);
            bw.write("package " + Constants.PACKAGE_BASE + ";");
            bw.newLine();
            bw.newLine();

            bw.write("import org.mybatis.spring.annotation.MapperScan;");
            bw.newLine();
            bw.write("import org.springframework.boot.SpringApplication;");
            bw.newLine();
            bw.write("import org.springframework.boot.autoconfigure.SpringBootApplication;");
            bw.newLine();
            bw.newLine();
            bw.write("@SpringBootApplication");
            bw.newLine();
            bw.write("@MapperScan(basePackages = {\"" + Constants.PACKAGE_BASE_MAPPERS + "\"})");
            bw.newLine();
            bw.write("public class " + className + " {");
            bw.newLine();
            bw.write("\tpublic static void main(String[] args) {");
            bw.newLine();
            bw.write("\t\tSpringApplication.run(" + className + ".class,args);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.write("}");
            bw.flush();
        } catch (
                Exception e) {
            logger.error("生成Starter文件失败");
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
