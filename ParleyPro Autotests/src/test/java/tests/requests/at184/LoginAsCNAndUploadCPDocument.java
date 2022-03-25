package tests.requests.at184;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import io.qameta.allure.Issue;
import io.qameta.allure.Issues;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.ContractInfo;
import pages.InProgressContractsPage;
import pages.LoginPage;
import pages.OpenedContract;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class LoginAsCNAndUploadCPDocument
{
    private SideBar sideBar;
    private static Logger logger = Logger.getLogger(LoginAsCNAndUploadCPDocument.class);


    @Test(priority = 1)
    public void loginAsCNAndUploadCPDocument()
    {
        // Login as CN
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail(Const.PREDEFINED_USER_CN_ROLE.getEmail());
        loginPage.setPassword(Const.PREDEFINED_USER_CN_ROLE.getPassword());

        sideBar = loginPage.clickSignIn().getSideBar();
        InProgressContractsPage inProgressContractsPage = sideBar.clickInProgressContracts(false);

        inProgressContractsPage.selectContract("Request_CP_at184");
        new OpenedContract(true).clickNewDocument().clickUploadCounterpartyDocuments(Const.DOCUMENT_DISCUSSIONS_SAMPLE);

        logger.info("Making sure that it is still request in DRAFT stage...");
        $(".contract-header__right .label").shouldBe(Condition.visible).shouldHave(Condition.exactText("REQUEST"));
        $(".lifecycle").shouldBe(Condition.visible).shouldHave(Condition.exactText("DRAFT"));
    }

    @Test(priority = 2)
    public void saveRequestAndCheckNegotiateState()
    {
        ContractInfo contractInfo = new ContractInfo(true);

        logger.info("Filling all Contract request fields...");
        contractInfo.setCounterpartyOrganization("CounterpartyAT");
        contractInfo.setCounterpartyChiefNegotiator("arthur.khasanov+autotestcn@parleypro.com");
        contractInfo.setContractingRegion("region1");
        contractInfo.setContractingCountry("country1");
        contractInfo.setContractEntity("entity1");
        contractInfo.setContractingDepartment("department1");
        contractInfo.setContractCategory("category1");
        contractInfo.setContractType("type1");
        contractInfo.clickSave();

        logger.info("Making sure that it is no longer a request...");
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)", "NEGOTIATE"));
        $(".contract-header__right .label").should(Condition.disappear); // no REQUEST label

        Screenshoter.makeScreenshot();
    }

    // Additional case for opening Contract Info. See: PAR-15561
    @Test(priority = 3)
    @Issues(@Issue("PAR-15561"))
    public void openContractInfoOfConvertedContract()
    {
        sideBar.clickInProgressContracts(false).selectContract("Request_CP_at184");

        new OpenedContract().clickContractInfo();
        ContractInfo contractInfo = new ContractInfo(true);

        logger.info("Check that 'Show contract request' toggle is present...");
        $(".contract-create-request-fields__tumbler").shouldBe(Condition.visible);

        contractInfo.setContractValue("1000");
        contractInfo.clickSave();

        logger.info("Check that 'USD 1,000.00' appears in the contract header...");
        $(".contract-header__company-price").shouldBe(Condition.visible).shouldHave(Condition.text("USD 1,000.00"));
    }
}
