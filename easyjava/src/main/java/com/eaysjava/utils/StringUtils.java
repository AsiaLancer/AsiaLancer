package com.eaysjava.utils;

public class StringUtils {

    public static String processField(String filed, Boolean isUpCaseFirst) {
        String[] arrs = filed.split("_");
        String newStr = "";
        arrs[0] = isUpCaseFirst ? arrs[0].substring(0, 1).toUpperCase() + arrs[0].substring(1) : arrs[0];
        newStr += arrs[0];
        for (int i = 1; i < arrs.length; i++) {
            newStr += arrs[i].substring(0, 1).toUpperCase() + arrs[i].substring(1);
        }
        return newStr;
    }
    public static String lowerStr(String filed, Boolean isLowerCaseFirst) {
        String[] arrs = filed.split("_");
        String newStr = "";
        arrs[0] = isLowerCaseFirst ? arrs[0].substring(0, 1).toLowerCase() + arrs[0].substring(1) : arrs[0];
        newStr += arrs[0];
        for (int i = 1; i < arrs.length; i++) {
            newStr += arrs[i].substring(0, 1).toUpperCase() + arrs[i].substring(1);
        }
        return newStr;
    }
}
