package lizhao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import lizhao.entity.UserEntity;
import lizhao.util.StringUtil;
import lizhao.util.Utils;

public class FileOperater {
    public static String loadFromFile() throws IOException {
        Map<String, UserEntity> map = new HashMap<String, UserEntity>();
        String webRoot = Utils.getWebRoot();//this.getServletConfig().getServletContext().getRealPath("/");
        File file = new File(webRoot + "account.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line = null;
        StringBuffer content = new StringBuffer();
        String[] data = null;
        UserEntity user = null;
        String[] dataTmp = null;
        while ((line = reader.readLine()) != null) {
            if (StringUtil.isBlank(line) || line.startsWith("#"))
                continue;
            content.append("\r\n" + line);
            user = new UserEntity();
            data = line.split("=");
            user.setUsername(data[0]);
            dataTmp = data[1].split(":");
            user.setPassword(dataTmp[0]);
            user.setEmail(dataTmp.length > 1 ? dataTmp[1] : user.getUsername());
//            user.setStatus(dataTmp.length > 2 ? Byte.valueOf(dataTmp[2] ): null);
            map.put(user.getUsername(), user);
        }
        Scheduler.setUsers(map);
        return content.toString();
    }

    public static void writeToFile(Collection<UserEntity> col) throws IOException {
        String webRoot = Utils.getWebRoot();//this.getServletConfig().getServletContext().getRealPath("/");
        File file = new File(webRoot + "account.txt");
        PrintWriter writer = new PrintWriter(new FileOutputStream(file));
        try {
            UserEntity user = null;
            String line = null;
            Iterator<UserEntity> it = col.iterator();
            writer.write("#username=password:receiveMsgEmail\r\n");
            while (it.hasNext()) {
                user = it.next();
                line = String.format("%1$s=%2$s:%3$s", user.getUsername(), user.getPassword(), user.getEmail());
                writer.write(line + "\r\n");
            }
        } finally{
            writer.close();
        }
    }

    public static void main(String[] args) throws IOException {
        loadFromFile();
        writeToFile(Scheduler.getUsers().values());
    }
}
