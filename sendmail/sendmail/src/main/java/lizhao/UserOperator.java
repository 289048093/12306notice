package lizhao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import lizhao.entity.UserEntity;
import lizhao.util.*;

public class UserOperator {
    private static final String QUERY_PERIOD = "queryPeriod";
    private static final String SEND_MAIL_TIMES = "sendMailTimes";
    private Map<String, Integer> sendMailTimesMap = new HashMap<String, Integer>();
    private String username;

    private Timer thisTimer;

    private boolean run;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserOperator(String username){
        this.username = username;
    }

    public UserOperator run() {
        if (run)
            return this;
        thisTimer = new Timer();
        thisTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    query();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, PropertiesUtil.getLongValue(QUERY_PERIOD));
        return this;
    }

    private void query() throws IOException {
        UserEntity user = Scheduler.getUsers().get(username);
        URL url2 = new URL("https://kyfw.12306.cn/otn/queryOrder/queryMyOrderNoComplete");
        HttpsURLConnection con = (HttpsURLConnection) url2.openConnection();
        con.setSSLSocketFactory(Utils.getSsf());
        ConnectionUtil.setSessionId(con, user.getSessionId());
        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        try {
            String line = null;
            StringBuffer res = new StringBuffer();
            String[] tmp = null;
            while ((line = reader.readLine()) != null) {
                if (line.indexOf("用户未登录") != -1) {
                    user.setStatus(Constant.USER_STATUS_NO_LOGIN);
                    tmp = ConnectionUtil.getRandCode();
                    user.setRandCode(tmp[0]);
                    user.setSessionId(tmp[1]);
                    res.append(line);
                    stop();
                } else {
                    res.append(line);
                }
            }
            if (user.getStatus() != Constant.USER_STATUS_NO_LOGIN) {
                Integer times = sendMailTimesMap.get(username);
                if(times==null)times=0;
                int limit = PropertiesUtil.getIntegerValue(SEND_MAIL_TIMES);
                if (times <= limit) {
                    String title = "您的帐号：" + user.getUsername() + ",购票状态变更。";
//                    MailUtil.sendMail(title, parseRes(res.toString()), StringUtil.isBlank(user.getEmail()) ? user
//                            .getUsername() : user.getEmail());
                    System.out.println(title+ parseRes(res.toString()));
                    times++;
                    sendMailTimesMap.put(username, times);
                }
                if (!res.toString().equals(user.getQueryRes())) {
                    sendMailTimesMap.put(user.getUsername(), 0);
                    user.setQueryRes(res.toString());
                }
            }
            if (user.getStatus() == Constant.USER_STATUS_NO_LOGIN && res.toString().indexOf("用户未登录") == -1) {
                user.setStatus(Constant.USER_STATUS_TICKETIMG);
            }
            Scheduler.getUsers().put(username, user);
        } finally {
            reader.close();
            con.disconnect();
        }
    }

    public UserOperator stop() {
        thisTimer.cancel();
        run = false;
        return this;
    }

    public UserOperator restart() {
        stop();
        run();
        return this;
    }

    private String parseRes(String res) {
        Pattern pattern = Pattern.compile(Constant.MESSAGE_PATTER);
        Matcher matcher = pattern.matcher(res);
        if (matcher.find()) {
            return matcher.group().split(":")[1];
        }
        if (res.matches(Constant.ORDER_SUCCESS_REGEX)) {
            String content = "";
            pattern = Pattern.compile(Constant.ORDER_TRAIN_DATE_PATTERN);
            matcher = pattern.matcher(res);
            if (matcher.find()) {
                content = "恭喜预定到了票，时间：" + matcher.group().split("\":")[1];
            }
            pattern = Pattern.compile(Constant.ORDER_TRAIN_FROM);
            matcher = pattern.matcher(res);
            if(matcher.find()){
                content+= ("，从："+matcher.group().split("\":")[1]);
            }
            pattern = Pattern.compile(Constant.ORDER_TRAIN_TO);
            matcher = pattern.matcher(res);
            if(matcher.find()){
                content+= ("--到："+matcher.group().split("\":")[1]);
            }
            return content;
        }
        return "其他";
    }

    public void login(String randCode) throws IOException {
        UserEntity user = Scheduler.getUsers().get(username);
        if (user == null) {
            return;
        }
//        URL url = new URL("https://kyfw.12306.cn/otn/login/loginAysnSuggest?loginUserDTO.user_name=" + username
//                + "&userDTO.password=" + user.getPassword() + "&randCode=" + randCode);
        URL url = new URL("https://kyfw.12306.cn/otn/login/loginAysnSuggest");
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
//        ConnectionUtil.setSessionId(con, user.getSessionId());
        con.setSSLSocketFactory(Utils.getSsf());
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setInstanceFollowRedirects(true);

        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con
                .setRequestProperty(
                        "Accept",
                        "image/jpeg, application/x-ms-application, image/gif, application/xaml+xml, image/pjpeg, application/x-ms-xbap, application/x-shockwave-flash, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
        con.setRequestProperty("Accept-Encoding", "gzip,deflate");
//        con.setRequestProperty("Referer", "***");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (MSIE 9.0; Windows NT 6.1; Trident/5.0;)");
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Cache-Control", "no-cache");
        con.setRequestProperty("Content-Length", "64");
        con.setRequestProperty("Host", "kyfw.12306.cn");
        con.setRequestProperty("Cookie", user.getSessionId());

        String message1 = "loginUserDTO.user_name=" + user.getUsername() + "&userDTO.password=" + user.getPassword()
                + "&randCode=" + randCode;
        System.out.println(message1);
        OutputStream out = con.getOutputStream();
        out.write(message1.getBytes());
        out.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        try {
            String res = br.readLine();
            System.out.println(res);
            if (res == null)
                return;
            String tmp = res.split("\"messages\":")[1];
            String message = tmp.substring(0, tmp.lastIndexOf(",\"validateMessages\":"));
            if (!StringUtil.isBlank(message)) {
                user.setErrorMsg(message);
            } else {
                user.setErrorMsg(null);
                user.setStatus(Constant.USER_STATUS_TICKETIMG);
            }
        } finally {
            br.close();
//            con.disconnect();
        }
    }

    public static void main(String[] args) throws IOException {
//        System.out.println("\u682A\u6D32\2CZZQ");
//        if(1==1)return;
        UserEntity user = new UserEntity();
        user.setUsername("289048093@qq.com");
        user.setPassword("lichao258");
        String[] data = ConnectionUtil.getRandCode();
        user.setRandCode(data[0]);
        user.setSessionId(data[1]);
        UserOperator o = new UserOperator("289048093@qq.com");
        System.out.println(user.getRandCode());
        String randCode = new Scanner(System.in).next();
        o.login(randCode);
    }
}
