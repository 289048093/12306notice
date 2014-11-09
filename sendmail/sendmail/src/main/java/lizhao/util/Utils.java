package lizhao.util;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.net.URL;
import java.net.URLConnection;

public class Utils {
    private static SSLSocketFactory ssf;
    static{
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            ssf = sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SSLSocketFactory getSsf() {
        return ssf;
    }

    public static String getWebRoot() {
        return getClassPath().split("WEB-INF")[0];
    }

    public static String getClassPath() {
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        if (url == null) {
            url = Utils.class.getClassLoader().getResource("");
        }
        if (url != null) {
            return url.getFile();
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(Utils.getWebRoot());
    }

    public static String getSessionId(URLConnection con) {
        StringBuffer s = new StringBuffer();
        for (String data : con.getHeaderFields().get("Set-Cookie")) {
            if (!StringUtil.isBlank(s.toString()))
                s.append(",");
            s.append(data);
        }
        return s.toString();
    }
}
