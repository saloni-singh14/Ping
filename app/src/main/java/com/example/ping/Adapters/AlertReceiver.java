package com.example.ping.Adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String username=intent.getStringExtra("username");
        String title=intent.getStringExtra("title");
        String content=intent.getStringExtra("content");


        /*Properties properties = new Properties();
        //properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");
       // properties.put("mail.smtp.host", "smtp.gmail.com");
       // properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        //properties.put("mail.smtp.auth", "true");
        //properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        String semail = "tracktrigger2020@gmail.com";

        String spassword = "oopProject";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(semail, spassword);
            }
        });

        try {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(semail));
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(email));

            message.setSubject("Task Reminder ");
            message.setText("Dear " + username + ", you have a pending task: " + title+"  \nTask Description :"+content+"\nTrack Trigger Team");

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();

        }*/
       // EmailUtil e=new EmailUtil();
        //if(!email.isEmpty()){
        //e.sendEmail(email,"Task Reminder","Task "+title+"\nDescription "+content);}

        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(username, title,content);
        notificationHelper.getManager().notify(1, nb.build());
    }

}