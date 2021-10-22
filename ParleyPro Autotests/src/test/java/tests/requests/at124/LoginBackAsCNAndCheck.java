package tests.requests.at124;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.ex.ElementNotFound;
import constants.Const;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class LoginBackAsCNAndCheck
{
    private static Logger logger = Logger.getLogger(LoginBackAsCNAndCheck.class);

    @Test(priority = 1)
    public void loginBackAsCNAndCheck() throws InterruptedException
    {
        LoginPage loginPage = new LoginPage();

        // Login as my team CN
        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );

        DashboardPage dashboardPage = loginPage.clickSignIn();

        logger.info("Making sure that contract with name 'Contract request' was added in In-progress contracts list...");
        $$(".contracts-list__contract-name").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("Contract request"));

        logger.info("Assert that REQUEST label appeared...");
        $(".label.label_theme_purple").shouldBe(Condition.visible).shouldHave(Condition.exactText("REQUEST"));

        logger.info("Opening this request...");
        dashboardPage.getSideBar().clickInProgressContracts(false).selectContract("Contract request");

        new OpenedContract();
        try
        {
            $(".contract-create__form .spinner").waitUntil(Condition.appear, 10_000);
        } catch( ElementNotFound e ) { } // ignore absence of spinner in case if it wasn't shown here
        $(".contract-create__form .spinner").waitUntil(Condition.disappear, 25_000);

        logger.info("Making sure that 2 word documents become docs in contract and 1 jpeg img become attachment...");
        $$(".document__header-rename").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("pramata", "AT-14"));
        $(".supporting-documents__document-name").shouldBe(Condition.visible).shouldHave(Condition.exactText("IMG_JPEG.jpeg"));

        logger.info("Checking request fields...");
        Assert.assertEquals($("#contractingregion").val(), "region1", "[Request field] Contracting region is wrong !!!");
        Assert.assertEquals($("#contractingcountry").val(), "country1", "[Request field] Contracting country is wrong !!!");
        Assert.assertEquals($("#contractentity").val(), "entity1", "[Request field] Contract entity is wrong !!!");
        Assert.assertEquals($("#contractingdepartment").val(), "department2", "[Request field] Contracting department is wrong !!!");
        Assert.assertEquals($("#contractcategory").val(), "category2", "[Request field] Contract category is wrong !!!");
        Assert.assertEquals($("input[data-id=\"contracttype\"]").val(), "type3", "[Request field] Contract type is wrong !!!");
        Assert.assertEquals(Selenide.executeJavaScript("return $('span:contains(\"Contract value\")').parent().next().find(\"button\").text().trim()"), "EUR EUR", "Currency is wrong !!!");
        Assert.assertEquals($("#contractvalue").val(), "4,500", "[Request field] Contract value is wrong !!!");

        logger.info("Checking that summary fields values were populated from request fields...");
        Assert.assertEquals(Selenide.executeJavaScript("return $('#contractingRegion').parent().prev().find(\"span\").text()"), "region1", "[Summary field] Contracting region is wrong !!!");
        Assert.assertEquals(Selenide.executeJavaScript("return $('#contractingCountry').parent().prev().find(\"span\").text()"), "country1", "[Summary field] Contracting country is wrong !!!");
        Assert.assertEquals(Selenide.executeJavaScript("return $('#contractEntity').parent().prev().find(\"span\").text()"), "entity1", "[Summary field] Contract entity is wrong !!!");
        Assert.assertEquals(Selenide.executeJavaScript("return $('#ContractingDepartment').parent().prev().find(\"span\").text()"), "department2", "[Summary field] Contracting department is wrong !!!");
        Assert.assertEquals(Selenide.executeJavaScript("return $('#contractCategory').parent().prev().find(\"span\").text()"), "category2", "[Summary field] Contract category is wrong !!!");
        Assert.assertEquals($("input[data-id=\"contractType\"]").val(), "type3", "[Summary field] Contract type is wrong !!!");
        Assert.assertEquals($("#contractValue").val(), "4,500", "[Summary field] Contract value is wrong !!!");

        Screenshoter.makeScreenshot();

        logger.info("Clicking 'Show contract request' to hide...");
        $(".contract-create-request-fields__tumbler div").click();
        Thread.sleep(500);
        Assert.assertFalse(Selenide.executeJavaScript("return $('.contract-create-request-fields__body').is(\":visible\")"));

        logger.info("Clicking 'Show contract request' to show...");
        $(".contract-create-request-fields__tumbler div").click();
        Thread.sleep(500);
        Assert.assertTrue(Selenide.executeJavaScript("return $('.contract-create-request-fields__body').is(\":visible\")"));
    }

    @Test(priority = 2)
    public void fillFieldsAndSaveContract()
    {
        logger.info("Fill in CP and CCN fields and Save...");
        $("#counterpartyOrganization").sendKeys("CounterpartyAT");
        $(".Select-loading").waitUntil(Condition.disappear, 7_000); // wait until spinner for Counterparty organization will disappear
        $("#counterpartyOrganization").pressEnter();
        $(".select__loading").waitUntil(Condition.disappear, 7_000); // wait until spinner for CCN field will disappear

        $("#counterpartyChiefNegotiator").sendKeys("arthur.khasanov+autotestcn@parleypro.com");
        $(".button.btn.btn-common.btn-blue.btn.btn-default").click(); // click Save

        logger.info("Checking that status is still in DRAFT..."); // since both docs were uploaded via my team button
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(3));
        $(".contract-header__status .lifecycle__item.active").shouldHave(Condition.exactText("DRAFT\n(2)"));
        $$(".documents__content .lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("DRAFT", "DRAFT"));

        logger.info("Making sure that attachment was saved too...");
        $(".supporting-documents__document-name").shouldBe(Condition.visible).shouldHave(Condition.exactText("IMG_JPEG.jpeg"));

        Screenshoter.makeScreenshot();
    }
}
