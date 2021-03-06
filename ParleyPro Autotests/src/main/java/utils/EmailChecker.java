package utils;

import org.apache.log4j.Logger;
import org.testng.Assert;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class EmailChecker
{
    private static String messageText;

    private static Logger logger = Logger.getLogger(EmailChecker.class);

    /**
     * Helper method that finds email by subject in Gmail Inbox folder and asserts subject.
     * After that email will be deleted.
     * @param host
     * @param user
     * @param password
     * @param emailSubject subject of email that need to be verified
     * @return true in case if email was found, false otherwise
     */
    public static boolean assertEmailBySubject(String host, String user, String password, String emailSubject)
    {
        boolean found = false;

        try
        {
            Properties properties = new Properties();

            properties.put("mail.pop3s.auth", "true");
            properties.put("mail.store.protocol", "pop3s");
            properties.put("mail.pop3.host", host);
            properties.put("mail.pop3.user", user);
            properties.put("mail.pop3.port", 995);
            properties.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore();

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

            for ( Message message : messages )
            {
                if( message.getSubject().trim().equals(emailSubject) )
                {
                    found = true;

                    logger.info("Email was found: " + message.getSubject() + " , " + message.getSentDate());
                    Address[] froms = message.getFrom();
                    String email = froms == null ? null : ((InternetAddress) froms[0]).getAddress();

                    if( email.equals("notification@parleypro.com") )
                    {
                        Assert.assertEquals(email, "notification@parleypro.com");
                    }
                    else if( email.equals("notifications@parleypro.com") )
                    {
                        Assert.assertEquals(email, "notifications@parleypro.com");
                    }
                    else if( email.equals("dse_demo@docusign.net") )
                    {
                        Assert.assertEquals(email, "dse_demo@docusign.net");
                    }
                    else
                    {
                        Assert.fail(email + " wasn't found !!!");
                    }

                    messageText = getTextFromMessage(message); // store email's body text

                    logger.info("Delete this email from INBOX...");
                    message.setFlag(Flags.Flag.DELETED, true);

                    break;
                }
            }

            inbox.close(true);
            store.close();
        } catch (NoSuchProviderException e) {
            logger.error("NoSuchProviderException", e);
        } catch (MessagingException e) {
            logger.error("MessagingException", e);
        } catch (IOException e) {
            logger.error("IOException", e);
        }

        return found;
    }

    /**
     * Helper method that allows to verify email's body text by _contains_.
     * Important: before use, need to invoke assertEmailBySubject first !
     * @param text is text that contains in body
     */
    public static String assertEmailBodyText(String text)
    {
        logger.info("Whole message text: " + messageText);
        Assert.assertTrue(messageText.contains(text));

        return messageText;
    }

    // Below code is copy/paste from https://stackoverflow.com/questions/11240368/how-to-read-text-inside-body-of-mail-using-javax-mail
    // Allows to get body text from email
    private static String getTextFromMessage(Message message) throws MessagingException, IOException
    {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart)  throws MessagingException, IOException
    {
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart){
                result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
            }
        }
        return result;
    }
    //
}
