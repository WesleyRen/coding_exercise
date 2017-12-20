import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;

public class EmailUtilTest {

    private SimpleSmtpServer server;

    @Before
    public void setUp() throws Exception {
        server = SimpleSmtpServer.start(SimpleSmtpServer.AUTO_SMTP_PORT);
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void testSend() {
        String from = "from@here.com";
        String fromName = "From Name";
        List<String> recipients = Arrays.asList("to@there.com, to2@there.com".split(","));
        String subject = "Test Subject";
        String body = "Test body";

        EmailUtil emailUtil = new EmailUtil("localhost", server.getPort());
        emailUtil.addMessage(from, fromName, recipients, subject, body);
        emailUtil.sendMessage();

        List<SmtpMessage> emails = server.getReceivedEmails();
        assertThat(emails, hasSize(1));
        SmtpMessage email = emails.get(0);
        assertThat(email.getHeaderValue("Subject"), is(subject));
        assertThat(email.getBody(), is(body));
        assertThat(email.getHeaderNames(), hasItem("Date"));
        assertThat(email.getHeaderNames(), hasItem("From"));
        assertThat(email.getHeaderNames(), hasItem("To"));
        assertThat(email.getHeaderNames(), hasItem("Subject"));
        assertTrue(email.getHeaderValue("From").contains(from));
        assertTrue(email.getHeaderValue("From").contains(fromName));
        assertTrue(email.getHeaderValue("To").contains(recipients.get(0)));
        assertTrue(email.getHeaderValue("To").contains(recipients.get(1)));
    }

    @Test
    public void testSendTwoMessagesOneSend() {
        List<String> recipients = new ArrayList<String>();
        recipients.add("to@there.com");

        EmailUtil emailUtil = new EmailUtil("localhost", server.getPort());
        emailUtil.addMessage("from@here.com", "From Name", recipients, "Test One", "Body one");
        emailUtil.addMessage("from@here.com", "From Name", recipients, "Test Two", "Body two");
        emailUtil.sendMessage();

        assertThat(server.getReceivedEmails(), hasSize(2));
    }
}