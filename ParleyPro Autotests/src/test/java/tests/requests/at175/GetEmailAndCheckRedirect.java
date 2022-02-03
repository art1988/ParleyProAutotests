package tests.requests.at175;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import utils.EmailChecker;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;


public class GetEmailAndCheckRedirect
{
    private String host     = "pop.gmail.com";
    private String username = "arthur.khasanov@parleypro.com";
    private String password = "ParGd881";
    private static Logger logger = Logger.getLogger(GetEmailAndCheckRedirect.class);


    @Test
    @Description("Final assertion of scenario. This test checks that URL from button REVIEW REQUEST leads to request.")
    public void getEmailAndCheckRedirect() throws InterruptedException
    {
        logger.info("Waiting for 60 seconds to make sure that email has been delivered...");
        Thread.sleep(60_000);

        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, "Contract request \"AT-175 request\" created"),
                "Contract request \"AT-175 request\" created" + " was not found !!!");

        String bodyText = EmailChecker.assertEmailBodyText("New contract request \"AT-175 request\" was created");

        int start = bodyText.indexOf("<"),
            end   = bodyText.indexOf(">");

        String URLFromButton = bodyText.substring(start + 1, end);
        URLFromButton = URLFromButton.replaceAll("amp;", ""); // remove amp; symbol

        logger.info("Opening URL from REVIEW REQUEST button...");
        Selenide.open(URLFromButton);

        logger.info("Login as CN...");
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );

        DashboardPage dashboardPage = loginPage.clickSignIn();

        logger.info("Assert that request was opened...");
        $(".contract-header__name").shouldBe(Condition.visible).shouldHave(Condition.text("AT-175 request"));
        $(".label_theme_purple").shouldBe(Condition.visible).shouldHave(Condition.text("REQUEST"));

        Screenshoter.makeScreenshot();
    }
}
