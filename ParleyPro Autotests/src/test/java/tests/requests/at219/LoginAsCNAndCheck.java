package tests.requests.at219;

import com.codeborne.selenide.CollectionCondition;
import constants.Const;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.ContractInfo;
import pages.LoginPage;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class LoginAsCNAndCheck
{
    private SideBar sideBar;
    private static Logger logger = Logger.getLogger(LoginAsCNAndCheck.class);


    @Test(priority = 1)
    public void loginAsCNAndConvertRequestToContract()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );

        sideBar = loginPage.clickSignIn().getSideBar();

        sideBar.clickInProgressContracts(false).selectContract("request for at-219");

        ContractInfo contractInfo = new ContractInfo(true);

        contractInfo.setCounterpartyOrganization("CounterpartyAT");
        contractInfo.setCounterpartyChiefNegotiator("arthur.khasanov+cpat@parleypro.com");
        contractInfo.setContractingRegion("region1");
        contractInfo.setContractingCountry("country1");
        contractInfo.setContractEntity("entity1");
        contractInfo.setContractingDepartment("department1");
        contractInfo.setContractCategory("category1");
        contractInfo.setContractType("type1");
        contractInfo.clickSave();

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("DRAFT\n(1)", "DRAFT"));
    }

    @Test(priority = 2)
    public void deleteUserWithRequesterRole()
    {

    }
}
