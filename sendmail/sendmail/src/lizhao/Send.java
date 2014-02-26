package lizhao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lizhao.util.Utils;

import sun.misc.BASE64Encoder;
import sun.org.mozilla.javascript.internal.json.JsonParser;

public class Send extends HttpServlet {

    /**
     * Constructor of the object.
     */
    public Send(){
        super();
    }

    /**
     * The doGet method of the servlet. <br>
     * 
     * This method is called when a form has its tag value method equals to get.
     * 
     * @param request
     *            the request send by the client to the server
     * @param response
     *            the response send by the server to the client
     * @throws ServletException
     *             if an error occurred
     * @throws IOException
     *             if an error occurred
     */
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        out.println("<HTML>");
        out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
        out.println("  <BODY>");
        out.print("    This is ");
        out.print(this.getClass());
        out.println(", using the GET method");
        out.println("  </BODY>");
        out.println("</HTML>");
        out.flush();
        out.close();

        String webRoot = Utils.getWebRoot();//this.getServletConfig().getServletContext().getRealPath("/");
        System.getProperty("user.dir");
        File file = new File(webRoot + "account.txt");
        System.out.println(file.getAbsolutePath());
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line = null;
        String[] data = null;
        String username = null;
        String password = null;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("#"))
                continue;
            data = line.split("=");
            username = data[0];
            password = data[1];
            System.out.println(username + ";" + password);
        }
        try {
            connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sendto = request.getParameter("sendto");
        String content = request.getParameter("content");
        String title = request.getParameter("title");
        // MailUtil.sendMail("买到票拉", "this is test", "289048093@qq.com", true);

    }
    private void connect() throws Exception {
//     // 创建SSLContext对象，并使用我们指定的信任管理器初始化
//        TrustManager[] tm = { new MyX509TrustManager() };
//        SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
//        sslContext.init(null, tm, new java.security.SecureRandom());
//        // 从上述SSLContext对象中得到SSLSocketFactory对象
//        SSLSocketFactory ssf = sslContext.getSocketFactory();

        URL url2 = new URL("https://kyfw.12306.cn/otn/queryOrder/queryMyOrderNoComplete");
        HttpsURLConnection con2 = (HttpsURLConnection) url2.openConnection();
//        con2.setSSLSocketFactory(ssf);
        InputStream is2 = con2.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is2, "UTF-8"));
        String line = null;
        while ((line = reader.readLine()) != null) {
            if (line.indexOf("用户未登录") != -1)
                System.out.println(line);
        }
        reader.close();
login("289048093@qq.com", "lichao258", "test");
    }
    
    private String login(String username,String password,String randCode) throws IOException{
        URL url = new URL("https://kyfw.12306.cn/otn/login/loginAysnSuggest?loginUserDTO.user_name="+username+"&userDTO.password="+password+"&randCode="+randCode);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        try {
            InputStream is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            String res = br.readLine();
            if(res==null)return null;
            String tmp = res.split("\"messages\":")[1];
            String message = tmp.substring(0,tmp.lastIndexOf(",\"validateMessages\":"));
            if(message!=null && !message.equals("")){
                return "0:"+message;
            }
            return getSessionId(con);
        } finally{
            con.disconnect();
        }
    }

    private String getSessionId(URLConnection con){
        for(String data:con.getHeaderFields().get("Set-Cookie")){
            if(data.indexOf("JSESSIONID")!=-1){
                return data.substring(0,data.indexOf(";"));
            }
        }
        return null;
    }
   
    public static void main(String[] args) {
        System.out.println("abcdefg".split(":")[0]);
        String string = "{\"validateMessagesShowId\":\"_validatorMessage\",\"status\":true,\"httpstatus\":200,\"data\":{\"orderCacheDTO\":{\"requestId\":5833520282124486629,\"userId\":3500042946533,\"number\":0,\"tourFlag\":\"dc\",\"requestTime\":\"2014-01-27 18:46:56\",\"queueOffset\":10796108861,\"queueName\":\"75_ORDER_Q6_3\",\"trainDate\":\"2014-02-06 00:00:00\",\"startTime\":\"1970-01-01 22:34:00\",\"stationTrainCode\":\"K9017\",\"fromStationCode\":\"ZZQ\",\"fromStationName\":\"株洲\",\"toStationCode\":\"SZQ\",\"toStationName\":\"深圳\",\"status\":3,\"message\":{\"message\":\"没有足够的票!\",\"code\":\"0\"},\"modifyTime\":\"2014-01-27 18:46:58\",\"tickets\":[{\"seatTypeName\":\"硬座\",\"seatTypeCode\":\"1\",\"ticketTypeName\":\"成人票\",\"passengerName\":\"李朝\",\"passengerIdTypeName\":\"二代身份证\"}],\"waitTime\":-2,\"waitCount\":0,\"ticketCount\":1,\"startTimeString\":\"2014-02-06 22:34\",\"array_passser_name_page\":[\"李朝\"]},\"to_page\":\"cache\"},\"messages\":[],\"validateMessages\":{}}";
        Pattern pattern = Pattern.compile(Constant.MESSAGE_PATTER);
        
        Matcher matcher = pattern.matcher(string);
        if(matcher.find()){
          System.out.println(matcher.group());   
        }
    }
}
