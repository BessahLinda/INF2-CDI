package dev.miage.inf2.course.cdi.service.impl;

import dev.miage.inf2.course.cdi.model.Customer;
import dev.miage.inf2.course.cdi.model.Receipt;
import dev.miage.inf2.course.cdi.service.ReceiptTransmissionService;
import jakarta.enterprise.context.Dependent;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.util.Properties;

@Dependent
public class ReceiptTransmissionServiceMail<T> implements ReceiptTransmissionService<T> {

        @Override
    public void sendReceipt(Customer customer, Receipt<T> receipt) {
        try {
            Properties prop = new Properties();
            prop.put("mail.smtp.auth", true);
            prop.put("mail.smtp.starttls.enable", "true");
            prop.put("mail.smtp.host", "smtp.miage.dev");
            prop.put("mail.smtp.port", "587");
            prop.put("mail.smtp.ssl.trust", "smtp.miage.dev");

            Session session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(System.getenv("MIAGE_MAIL_USER"), System.getenv("MIAGE_MAIL_PASSWORD"));
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("pos@miage.dev"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(customer.email()));
            message.setSubject("Your Receipt");

            String msg = "You bough item with id" + receipt.item().toString() + " with price" + receipt.price();

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }


}
