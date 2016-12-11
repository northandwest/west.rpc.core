package reflect;

import java.lang.reflect.InvocationTargetException;  
import java.util.ArrayList;  
import java.util.List;  
  
public class ReflectMethod {  
      
  
    public static void main(String[] args) {  
        ConvertFunction convert = new ConvertFunction();  
        List<Func> dst = new ArrayList<Func>();  
        String funcStr = "convert_if_exist(\"field1\",\"field2\"); convert_if_exist(\"field1\",\"field2\",\"field3\");";//假设从数据库中读出出来了  
        Utils.initFunctions(convert, dst, funcStr);  
        for(int i = 0;i<dst.size();i++){  
            try {  
                dst.get(i).call("defaultKey","defaultValue");//调用，默认的两个参数此时传入，和数据库配置中的field1,field2无关，视业务而定  
            } catch (IllegalArgumentException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            } catch (IllegalAccessException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            } catch (InvocationTargetException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        }  
    }  
}  