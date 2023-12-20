package com.eaysjava;

import com.eaysjava.bean.TableInfo;
import com.eaysjava.builder.*;


import java.util.List;

public class RunApplication {
    public static void main(String[] args) {
        List<TableInfo> tableInfos = BuildTable.getTables();

        BuildBase.execute();
        for (TableInfo tableInfo:tableInfos){
            BuildPo.execute(tableInfo);
            BuildQuery.execute(tableInfo);
            BuildMapper.execute(tableInfo);
            BuildMapperXml.execute(tableInfo);
            BuildService.execute(tableInfo);
            BuildServiceImpl.execute(tableInfo);
            BuildController.execute(tableInfo);
            BuildStarter.execute();
        }
        
    }
}
