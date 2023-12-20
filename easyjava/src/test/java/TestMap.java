import com.eaysjava.bean.FieldInfo;
import com.eaysjava.bean.TableInfo;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TestMap {

//    Map<String,List<FieldInfo>>

//    @Test
//    public void Test3(){
//        TableInfo tableInfo = new TableInfo();
//
//
////        List<FieldInfo> fieldInfoList = new ArrayList<>();
////        tableInfo.getKeyIndexMap().put("key",fieldInfos);
//
//        List<FieldInfo> fieldInfoList = tableInfo.getKeyIndexMap().get("key");
//        if (fieldInfoList==null){
//            fieldInfoList = new ArrayList<>();
//            tableInfo.getKeyIndexMap().put("key",fieldInfoList);
//        }
//
//        FieldInfo fieldInfo = new FieldInfo();
//        fieldInfo.setJavaType("sadas");
//
//        fieldInfoList.add(fieldInfo);
//
//

//    }


    @Test
    public void Test2(){
        String s = "a";

        System.out.println(s.hashCode());
        s += "b";
        System.out.println(s.hashCode());

    }
    @Test
    public void Test3(){
        Date a = new Date();

        String format = new SimpleDateFormat("yyyy-MM-dd").format(a);


        String dateString = "2023-06-16 10:30:00";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = formatter.parse(dateString);
            System.out.println("Parsed date: " + date.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void test4(){
        String s = "dasdsa%sniubi";

        System.out.println(String.format(s,"wode"));
    }

    @Test
    public void test5(){
        System.out.println("public static String "+toUpAndReplace("bean.mapper.suffix"));
        System.out.println(toLowAndReplace("BEAN_MAPPER_XML_SUFFIX"));
    }

    public String toUpAndReplace(String s){
        return s.toUpperCase().replace(".","_")+";";
    }

    public String toLowAndReplace(String s){
        return s.toLowerCase().replace("_",".")+"=";
    }

    @Test
    public void test6(){
        System.out.println(Arrays.toString(("a我d").getBytes(StandardCharsets.UTF_8)));
    }


}
