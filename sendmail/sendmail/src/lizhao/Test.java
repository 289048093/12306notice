package lizhao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Test {
    public static void main(String[] args) {
//    MailUtil.sendMailSynchron("test", "this is test", "289048093@qq.com", true);

    }

    public void Login() throws IOException {
        String urlString = "";
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

//  client.setCookies(connection, cookieJar);

        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setInstanceFollowRedirects(true);

        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection
                .setRequestProperty(
                        "Accept",
                        "image/jpeg, application/x-ms-application, image/gif, application/xaml+xml, image/pjpeg, application/x-ms-xbap, application/x-shockwave-flash, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
        connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
        connection.setRequestProperty("Referer", "***");
        connection
                .setRequestProperty(
                        "User-Agent",
                        "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.3)");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Cache-Control", "no-cache");
        connection.setRequestProperty("Content-Length", "64");
        connection.setRequestProperty("Host", "***");
        connection.setRequestProperty("Cookie", "");

        String message = "number=***&passwd=***&select=cert_no&returnUrl=";
        System.out.println(message);
        OutputStream out = connection.getOutputStream();
        out.write(message.getBytes());
        out.close();
        connection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
        String line = br.readLine();
        while (line != null) {
            System.out.println(line);
            line = br.readLine();
        }
    }
}
