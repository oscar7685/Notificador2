/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sanet.application;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Web application lifecycle listener.
 *
 * @author Ususario
 */

public class NewServletListener implements ServletContextListener {

    private static int ultimoPost = 0;
    private static String ANDROID_DEVICEO = "cbSJ_Aoxbz8:APA91bE-UAP9o5JJbC0hQLj9kj34sWzVWtU7dP9i2idFFh0E2C9jHkr0vB15MAGHpj3Jq4M27rMvARTSW8G3iRH_sAPk1yJ7MtjeMSgxzgLuPwWDwhCNd1ON9A0avkrR-ZlfUeJXe2BS";

    public static String getANDROID_DEVICEO() {
        return ANDROID_DEVICEO;
    }

    public static void setANDROID_DEVICEO(String ANDROID_DEVICEO) {
        NewServletListener.ANDROID_DEVICEO = ANDROID_DEVICEO;
    }

    public static int getUltimoPost() {
        return ultimoPost;
    }

    public static void setUltimoPost(int ultimoPost) {
        NewServletListener.ultimoPost = ultimoPost;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ultimoPost = 0;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
