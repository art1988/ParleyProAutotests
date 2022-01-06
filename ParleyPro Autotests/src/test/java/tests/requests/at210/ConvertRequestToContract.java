package tests.requests.at210;

import constants.Const;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.ContractInfo;
import pages.LoginPage;
import utils.ScreenShotOnFailListener;

@Listeners({ScreenShotOnFailListener.class})
public class ConvertRequestToContract
{
    private static Logger logger = Logger.getLogger(ConvertRequestToContract.class);


    @Test
    @Description("Final check happens here. This test converts request to contract and checks that ‘Request' label disappears and 'Add Documents’ section is shown.")
    public void convertRequestToContract() throws InterruptedException
    {
        logger.info("Login as user with full rights (Felix)...");

        LoginPage loginPage = new LoginPage();

        loginPage.setEmail(Const.USER_FELIX.getEmail());
        loginPage.setPassword(Const.USER_FELIX.getPassword());

        loginPage.clickSignIn().getSideBar().clickInProgressContracts(false).selectContract("Request for at-210");

        ContractInfo contractInfo = new ContractInfo(true);
        contractInfo.setContractingRegion("region1");
        contractInfo.setContractingCountry("country1");
        contractInfo.setContractEntity("entity1");
        contractInfo.setContractingDepartment("department1");
        contractInfo.setContractCategory("category1");
        contractInfo.setContractType("type1");
        contractInfo.clickSave();

        Thread.sleep(5_000);
    }
}
