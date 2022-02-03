package tests.integrations.at217;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.EmailChecker;

import static com.codeborne.selenide.Selenide.$;


public class GetEmailAndSign
{
    private String host     = "pop.gmail.com";
    private String username = "arthur.khasanov@parleypro.com";
    private String password = "ParGd881";

    private static Logger logger = Logger.getLogger(GetEmailAndSign.class);


    @Test
    @Description("This test gets email and completes sign process.")
    public void getEmailAndSign() throws InterruptedException
    {
        logger.info("Waiting for 60 seconds to make sure that email has been delivered...");
        Thread.sleep(60_000);

        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, "Sign contract AT-217: DocuSign basics CTR"),
                "Email with subject 'Sign contract AT-217: DocuSign basics CTR' was not found !!!");

        String bodyText = EmailChecker.assertEmailBodyText("Please sign contract AT-217");

        int urlStartIndex = bodyText.lastIndexOf("https://demo.docusign.net");

        String sub_String = bodyText.substring(urlStartIndex);
        String URLFromReviewDocumentButton = sub_String.substring(0, (bodyText.substring(urlStartIndex)).indexOf("\n"));

        logger.info("Opening URL from REVIEW REQUEST button...");
        Selenide.open(URLFromReviewDocumentButton);

        $("#welcome").shouldBe(Condition.visible);
        $("button[data-qa='action-bar-btn-continue']").shouldBe(Condition.visible, Condition.enabled).click();
        logger.info("CONTINUE yellow button was clicked...");

        $(".signature-tab-content").shouldBe(Condition.visible, Condition.enabled).click();
        Thread.sleep(3_000);
        logger.info("Sign yellow box was clicked...");

        if( $("button[data-qa='use-saved-signature']").isDisplayed() )
        {
            $("button[data-qa='use-saved-signature']").click();
            logger.info("USED SAVED yellow button was clicked...");
        }

        $("#action-bar-btn-finish").shouldBe(Condition.visible, Condition.enabled).click();
        logger.info("FINISH yellow button was clicked...");
        
        $("#psContent_title").shouldBe(Condition.visible).shouldHave(Condition.text("You’ve finished signing!"));
        logger.info("'Finished signing' label was shown.");
    }
}
