package com.example.studentapp.util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailUtil {

    private static final String SMTP_USER = "yourgmail@gmail.com";
    private static final String SMTP_PASS = "your-app-password";

    public static void sendStudentWelcomeEmail(String toEmail, String studentName) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SMTP_USER, SMTP_PASS);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SMTP_USER));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Student Registration Successful");
            message.setText("Hello " + studentName + ",\n\nYour record has been added successfully.\n\nRegards,\nStudent Management System");

            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}