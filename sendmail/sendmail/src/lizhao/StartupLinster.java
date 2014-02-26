package lizhao;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import lizhao.entity.UserEntity;

public class StartupLinster implements ServletContextListener {

    public void contextDestroyed(ServletContextEvent sce) {

    }

    public void contextInitialized(ServletContextEvent sce) {
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        try {
            FileOperater.loadFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        startAllQuery();
    }

    /**
     * 开启所有查询
     */
    private void startAllQuery() {
        Map<String, UserEntity> map = Scheduler.getUsers();
        Iterator<UserEntity> it = map.values().iterator();
        Map<String, UserOperator> queryers = new HashMap<String, UserOperator>();
        UserEntity user = null;
        while (it.hasNext()) {
            user = it.next();
            queryers.put(user.getUsername(), new UserOperator(user.getUsername()).run());
        }
        Scheduler.setUserOperators(queryers);
    }

}
