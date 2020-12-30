package utils;

import org.apache.log4j.Logger;
import org.testng.Assert;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.search.FlagTerm;
import java.util.Arrays;
import java.util.Properties;

public class EmailChecker
{
    private static Logger logger = Logger.getLogger(EmailChecker.class);

    /**
     * Helper method that allows to get emails from Gmail.
     * Checks that email has emailSubject.
     * @param host
     * @param user
     * @param password
     * @param emailSubject subject of email that need to be verified
     */
    public static void checkEmailSubject(String host, String user, String password, String emailSubject)
    {
        try
        {
            Properties properties = new Properties();

            properties.put("mail.imap.host", host);
            properties.put("mail.imap.port", "993");
            properties.put("mail.imap.starttls.enable", "true");
            properties.put("mail.imap.ssl.trust", host);

            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore("imaps");

            store.connect(host, user, password);

            Folder inbox = store.getFolder("Inbox");
            inbox.open(Folder.READ_WRITE);

            // Fetch unseen messages from inbox folder
            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            logger.info("Amount of emails : " + messages.length);

            logger.info("Sort messages from recent to oldest...");
            Arrays.sort( messages, (m1, m2 ) -> {
                try {
                    return m2.getSentDate().compareTo( m1.getSentDate() );
                } catch ( MessagingException e ) {
                    throw new RuntimeException( e );
                }
            } );

            boolean found = false;
            for ( Message message : messages )
            {
                if( message.getSubject().equals(emailSubject) )
                {
                    found = true;
                    logger.info("Email was found: " + message.getSubject() + " , " + message.getReceivedDate());
                    Address[] froms = message.getFrom();
                    String email = froms == null ? null : ((InternetAddress) froms[0]).getAddress();

                    Assert.assertEquals(email, "notification@parleypro.com");

                    logger.info("Delete this email...");
                    message.setFlag(Flags.Flag.DELETED, true);
                }
            }

            if( !found ) // Email was not found !
            {
                Assert.fail("Email with subject " + emailSubject + " was not found !!!");
            }

            inbox.close(false);
            store.close();
        } catch (NoSuchProviderException e) {
            logger.error("NoSuchProviderException", e);
        } catch (MessagingException e) {
            logger.error("MessagingException", e);
        } catch (Exception e) {
            logger.error("Exception", e);
        }
    }
}
