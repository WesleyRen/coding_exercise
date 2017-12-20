import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil {

    public static final String SMTP_SERVER = "smtp-gw1.wal-mart.com";
    public static final String SMTP_SENDPARTIAL = "true";
    private Session session;
    private List<MimeMessage> messages = new ArrayList<MimeMessage>();

    public EmailUtil() {
        Properties mailProps = new Properties();
        mailProps.setProperty("mail.smtp.host", SMTP_SERVER);
        mailProps.setProperty("mail.smtp.sendpartial", SMTP_SENDPARTIAL);
        session = Session.getInstance(mailProps, null);
    }

    public EmailUtil(String smtpServer, int port) {
        Properties mailProps = new Properties();
        mailProps.setProperty("mail.smtp.host", smtpServer);
        mailProps.setProperty("mail.smtp.port", "" + port);
        mailProps.setProperty("mail.smtp.sendpartial", SMTP_SENDPARTIAL);
        session = Session.getInstance(mailProps, null);
    }

    /**
     * Utility method to send simple HTML email
     * @param from
     * @param subject
     * @param body
     * @param to
     *
     * @throws MessagingException
     */
    public void sendMessage() {
        // this.session.setDebug(true);

        this.messages.forEach(msg -> {
            try {
                Transport.send(msg);
            } catch (MessagingException e) {
                e.printStackTrace();
                throw new AsyncException(e);
            }
        });
    }

    public void addMessage(String from, String fromName, List<String> recipients, String subject, String body)
    {
        try {
            MimeMessage msg = new MimeMessage(session);
            // set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress(from, fromName));
            msg.setReplyTo(InternetAddress.parse(from, false));
            msg.setSubject(subject, "UTF-8");
            msg.setSentDate(new Date());
            msg.setText(body, "UTF-8");
            recipients
                    .forEach(recipient -> {
                        try {
                            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
                        } catch (AddressException e) {
                            e.printStackTrace();
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                    });
            this.messages.add(msg);
        } catch (MessagingException e) {
            throw new AsyncException(e);
        } catch (UnsupportedEncodingException e) {
            throw new AsyncException(e);
        }

    }

    public static void main(String[] args) {

        System.out.println("SimpleEmail Start");
        if (args.length < 5) {
            System.out.println("Please run this with folllowing arguments:");
            System.out.println("<from_email>, <from_name>, <to_emails>, <subject>, <message_body>");
            throw new IllegalArgumentException();
        }

        String from = args[0];
        String fromName = args[1];
        List<String> recipients = Arrays.asList(args[2].split(","));
        String subject = args[3];
        String body = args[4];

        EmailUtil emailUtil = new EmailUtil();
        emailUtil.addMessage(from, fromName, recipients, subject, body);
        emailUtil.sendMessage();

    }

}
