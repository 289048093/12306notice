package lizhao.util;

import java.net.URL;
import java.net.URLConnection;

public class Utils {
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
