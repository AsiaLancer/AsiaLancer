package com.eaysjava.bean;

import com.eaysjava.utils.PropertiesUtils;

public class Constants {


    public static String PROJECT_AUTHOR;
    public static String STARTER_APPLICATION;

    public static String PATH_BASE;
    public static String PATH_PO;
    public static String PATH_UTILS;
    public static String PATH_BASE_MAPPERS;
    public static String PATH_QUERY;
    public static String PATH_ENUMS;
    public static String PATH_VO;
    public static String PATH_MAPPER_XML;
    public static String PATH_JAVA = "java/";
    public static String PATH_BASE_JAVA;
    public static String PATH_RESOURCES = "resources/";
    public static String PATH_BASE_RESOURCES;
    public static String PATH_SERVICE;
    public static String PATH_SERVICE_IMPL;
    public static String PATH_CONTROLLER;
    public static String PATH_EXCEPTION;

    public static String PACKAGE_BASE;
    public static String PACKAGE_PO;
    public static String PACKAGE_QUERY;
    public static String PACKAGE_ENUMS;
    public static String PACKAGE_VO;
    public static String PACKAGE_UTILS;
    public static String PACKAGE_BASE_MAPPERS;
    public static String PACKAGE_MAPPER_XML;
    public static String PACKAGE_SERVICE;
    public static String PACKAGE_SERVICE_IMPL;
    public static String PACKAGE_CONTROLLER;
    public static String PACKAGE_EXCEPTION;

    public static String IGNORE_TABLE_ToJASON_FIELD;
    public static String IGNORE_TABLE_ToJASON_EXPRESSION;
    public static String IGNORE_TABLE_ToJASON_CLASS;

    public static String BEAN_DATE_FORMAT;
    public static String BEAN_DATE_FORMAT_CLASS;
    public static String BEAN_DATE_EXPRESSION;
    public static String BEAN_DATE_EXPRESSION_CLASS;

    public static Boolean IGNORE_TABLE_PREFIX;
    public static String BEAN_QUERY_SUFFIX;
    public static String BEAN_QUERY_FUZZY_SUFFIX;
    public static String BEAN_QUERY_TIME_START_SUFFIX;
    public static String BEAN_QUERY_TIME_END_SUFFIX;
    public static String BEAN_MAPPER_SUFFIX;
    public static String BEAN_MAPPER_XML_SUFFIX;
    public static String BEAN_SERVICE_SUFFIX;
    public static String BEAN_SERVICE_IMPL_SUFFIX;
    public static String BEAN_CONTROLLER_SUFFIX;

    static {
        PROJECT_AUTHOR = PropertiesUtils.getString("project.author");
        STARTER_APPLICATION = PropertiesUtils.getString("starter.application");
        IGNORE_TABLE_PREFIX = Boolean.valueOf(PropertiesUtils.getString("ignore.table.prefix"));
        BEAN_QUERY_SUFFIX = PropertiesUtils.getString("bean.query.suffix");

        IGNORE_TABLE_ToJASON_FIELD = PropertiesUtils.getString("ignore.table.toJason.field");
        IGNORE_TABLE_ToJASON_EXPRESSION = PropertiesUtils.getString("ignore.table.toJason.expression");
        IGNORE_TABLE_ToJASON_CLASS = PropertiesUtils.getString("ignore.table.toJason.class");

        BEAN_DATE_FORMAT = PropertiesUtils.getString("bean.date.format");
        BEAN_DATE_FORMAT_CLASS = PropertiesUtils.getString("bean.date.format.class");
        BEAN_DATE_EXPRESSION = PropertiesUtils.getString("bean.date.expression");
        BEAN_DATE_EXPRESSION_CLASS = PropertiesUtils.getString("bean.date.expression.class");

        PACKAGE_BASE = PropertiesUtils.getString("package.base");
        PACKAGE_PO = PACKAGE_BASE + "." + PropertiesUtils.getString("package.po");
        PACKAGE_QUERY = PACKAGE_BASE + "." + PropertiesUtils.getString("package.query");
        PACKAGE_ENUMS = PACKAGE_BASE + "." + PropertiesUtils.getString("package.enums");
        PACKAGE_VO = PACKAGE_BASE + "." + PropertiesUtils.getString("package.vo");
        PACKAGE_UTILS = PACKAGE_BASE + "." + PropertiesUtils.getString("package.utils");
        PACKAGE_BASE_MAPPERS = PACKAGE_BASE + "." + PropertiesUtils.getString("package.mappers");
        PACKAGE_MAPPER_XML = PACKAGE_BASE + "." + PropertiesUtils.getString("package.mappers.xml");
        PACKAGE_SERVICE = PACKAGE_BASE + "." + PropertiesUtils.getString("package.service");
        PACKAGE_SERVICE_IMPL = PACKAGE_BASE + "." + PropertiesUtils.getString("package.service.impl");
        PACKAGE_CONTROLLER = PACKAGE_BASE + "." + PropertiesUtils.getString("package.controller");
        PACKAGE_EXCEPTION = PACKAGE_BASE + "." + PropertiesUtils.getString("package.exception");

        PATH_BASE = PropertiesUtils.getString("path.base");
        PATH_BASE_JAVA = PATH_BASE + PATH_JAVA + PropertiesUtils.getString("package.base").replace(".", "/");
        PATH_PO = PATH_BASE_JAVA + "/" + PropertiesUtils.getString("package.po").replace(".", "/");
        PATH_UTILS = PATH_BASE_JAVA + "/" + PropertiesUtils.getString("package.utils");
        PATH_BASE_MAPPERS = PATH_BASE_JAVA + "/" + PropertiesUtils.getString("package.mappers");
        PATH_QUERY = PATH_BASE_JAVA + "/" + PropertiesUtils.getString("package.query").replace(".", "/");
        PATH_ENUMS = PATH_BASE_JAVA + "/" + PropertiesUtils.getString("package.enums").replace(".", "/");
        PATH_VO = PATH_BASE_JAVA + "/" + PropertiesUtils.getString("package.vo").replace(".", "/");
        PATH_BASE_RESOURCES = PATH_BASE + PATH_RESOURCES + PropertiesUtils.getString("package.base").replace(".", "/");
        PATH_MAPPER_XML = PATH_BASE_RESOURCES + "/" + PropertiesUtils.getString("package.mappers.xml");
        PATH_SERVICE = PATH_BASE_JAVA + "/" + PropertiesUtils.getString("package.service");
        PATH_SERVICE_IMPL = PATH_BASE_JAVA + "/" + PropertiesUtils.getString("package.service.impl").replace(".", "/");
        PATH_CONTROLLER = PATH_BASE_JAVA + "/" + PropertiesUtils.getString("package.controller");
        PATH_EXCEPTION = PATH_BASE_JAVA + "/" + PropertiesUtils.getString("package.exception");

        BEAN_QUERY_FUZZY_SUFFIX = PropertiesUtils.getString("bean.query.fuzzy.suffix");
        BEAN_QUERY_TIME_START_SUFFIX = PropertiesUtils.getString("bean.query.time.start.suffix");
        BEAN_QUERY_TIME_END_SUFFIX = PropertiesUtils.getString("bean.query.time.end.suffix");
        BEAN_MAPPER_SUFFIX = PropertiesUtils.getString("bean.mapper.suffix");
        BEAN_MAPPER_XML_SUFFIX = PropertiesUtils.getString("bean.mapper.xml.suffix");
        BEAN_SERVICE_SUFFIX = PropertiesUtils.getString("bean.service.suffix");
        BEAN_SERVICE_IMPL_SUFFIX = PropertiesUtils.getString("bean.service.impl.suffix");
        BEAN_CONTROLLER_SUFFIX = PropertiesUtils.getString("bean.controller.suffix");

    }

    public static void main(String[] args) {

        System.out.println(PATH_BASE);
        System.out.println(PATH_UTILS);
        System.out.println(PATH_QUERY);
        System.out.println(PATH_MAPPER_XML);
        System.out.println(PACKAGE_MAPPER_XML);
        System.out.println(PATH_SERVICE_IMPL);
        System.out.println(PATH_BASE_JAVA);
    }
}
