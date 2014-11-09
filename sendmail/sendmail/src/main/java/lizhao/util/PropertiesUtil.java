package lizhao.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
    private static Properties properties = new Properties();
    static{
        try {
            properties.load(new FileInputStream(Utils.getWebRoot()+"cfg.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getStringValue(String key){
        return properties.getProperty(key);
    }
    
    public static Integer getIntegerValue(String key){
        return Integer.valueOf(getStringValue(key));
    }
    
    public static Long getLongValue(String key){
        return Long.valueOf(getStringValue(key));
    }
    public static void main(String[] args) {
       System.out.println(getLongValue("queryTime"));
    }
}
