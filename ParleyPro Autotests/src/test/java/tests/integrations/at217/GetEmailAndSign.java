package tests.integrations.at217;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import utils.EmailChecker;
import utils.ScreenShotOnFailListener;

@Listeners({ScreenShotOnFailListener.class})
public class GetEmailAndSign
{
    private String host     = "pop.gmail.com";
    private String username = "arthur.khasanov@parleypro.com";
    private String password = "ParGd881";

    private static Logger logger = Logger.getLogger(GetEmailAndSign.class);


    @Test
    public void getEmailAndSign() throws InterruptedException
    {
        logger.info("Waiting for 60 seconds to make sure that email has been delivered...");
        Thread.sleep(30_000);

        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, "Sign contract AT-217: DocuSign basics CTR"),
                "Email with subject 'Sign contract AT-217: DocuSign basics CTR' was not found !!!");

        String bodyText = EmailChecker.assertEmailBodyText("Please sign contract AT-217");

        //String URL_REVIEW_DOCUMENT = bodyText.s
        int urlStart = bodyText.lastIndexOf("https://demo.docusign.net");

        System.out.println("--------------");
        String sub_String = bodyText.substring(urlStart);

        System.out.println(sub_String.substring(0, (bodyText.substring(urlStart)).indexOf("\n")));

        /*String URLFromButton = bodyText.substring(start + 1, end);
        URLFromButton = URLFromButton.replaceAll("amp;", ""); // remove amp; symbol

        logger.info("Opening URL from REVIEW REQUEST button...");
        Selenide.open(URLFromButton);*/
    }
}
