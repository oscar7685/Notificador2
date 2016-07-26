/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sanet.application;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ususario
 */
public class Notificador {

    private static final long serialVersionUID = 1L;
    private static final String SENDER_ID = "AIzaSyAFQZ25sI-WpUoyc42uJoSiqE1vfZd9X6k";
    private List<String> androidTargets = new ArrayList<String>();

    public Notificador() {
        androidTargets.add(NewServletListener.getANDROID_DEVICEO());
    }

    public void enviarNotificacion(String collapseKey, String mensaje, String codigo, String url) {
        Sender sender = new Sender(SENDER_ID);

   
        Message message = new Message.Builder()
                .collapseKey(collapseKey)
                .timeToLive(86400)
                .delayWhileIdle(true)
                .addData("message", mensaje)
                .addData("codigo", codigo)
                .addData("url", url)
                .build();

        try {
            MulticastResult result = sender.send(message, androidTargets, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
