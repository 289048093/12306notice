package lizhao;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import lizhao.entity.UserEntity;
import lizhao.util.ConnectionUtil;

public class Scheduler {
    
    private static Map<String, UserEntity> users;

    private static Map<String, UserOperator> userOperators;

    public static Map<String, UserOperator> getUserOperators() {
        return userOperators;
    }

    public static void setUserOperators(Map<String, UserOperator> userOperators) {
        Scheduler.userOperators = userOperators;
    }

    public static Map<String, UserEntity> getUsers() {
        return users;
    }

    public static void setUsers(Map<String, UserEntity> users) {
        Scheduler.users = users;
    }

    public void addUser(String username, String password, String email) throws IOException {
        String[] res = null;
        try {
            res = ConnectionUtil.getRandCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (res != null) {
            UserEntity user = new UserEntity();
            user.setUsername(username);
            user.setPassword(password);
            user.setRandCode(res[0]);
            user.setSessionId(res[1]);
            user.setEmail(email);
            user.setStatus(Constant.USER_STATUS_NO_LOGIN);
            users.put(username, user);
        }
        FileOperater.writeToFile(users.values());
    }

    public void addUser(String username, String password) throws IOException {
        addUser(username, password, username);
    }

    public void addQueryer(UserOperator e) {
        userOperators.put(e.getUsername(), e);
    }
    
    public static void restartUserOperators(){
        Iterator<UserOperator> it = userOperators.values().iterator();
        while(it.hasNext()){
            it.next().restart();
        }
    }
}
