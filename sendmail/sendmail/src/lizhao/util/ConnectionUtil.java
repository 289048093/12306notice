package lizhao.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import sun.misc.BASE64Encoder;

public class ConnectionUtil {
    public static String getSessionId(URLConnection con) {
        for (String data : con.getHeaderFields().get("Set-Cookie")) {
            if (data.indexOf("JSESSIONID") != -1) {
                return data.substring(0, data.indexOf(";"));
            }
        }
        return null;
    }
    public static void setSessionId(URLConnection con,String value){
        con.setRequestProperty("Cookie", value);
    }
    
    /**
     * 
     * @return [0]datauri [1]sessionid
     * @throws IOException
     */
    public static String[] getRandCode() throws IOException {
        //验证码
        URL url = new URL("https://kyfw.12306.cn/otn/passcodeNew/getPassCodeNew.do?module=login&rand=sjrand&"
                + new Date().getTime());
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        InputStream is = con.getInputStream();
        byte[] buf = new byte[1024];
        int len = 0;
        int count = 0;
        byte[] total = new byte[] {};
        try {
            while ((len = is.read(buf)) != -1) {
                count = total.length + len;
                byte[] tmp = new byte[count];
                System.arraycopy(total, 0, tmp, 0, total.length);
                System.arraycopy(buf, 0, tmp, total.length, len);
                total = tmp;
            }
        } finally {
            is.close();
        }
        BASE64Encoder be = new BASE64Encoder();
        String dataURI = "data:image/png;base64," + be.encode(total);
        String[] res = { dataURI, Utils.getSessionId(con) };
        return res;
    }
}
