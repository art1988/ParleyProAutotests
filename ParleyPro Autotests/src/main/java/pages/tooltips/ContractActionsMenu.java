package pages.tooltips;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import forms.*;
import forms.delete.DeleteContract;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Class that represents menu with 'Cancel' and 'Delete' contract options.
 * Also, applicable for [requests] with it's 'Reassign Chief Negotiator' menu item, etc.
 * In executed status additional menu items were added: 'Amend contract' and 'Terminate contract'.
 */
public class ContractActionsMenu
{
    private String contractName;


    private static Logger logger = Logger.getLogger(ContractActionsMenu.class);

    public ContractActionsMenu(String contractName)
    {
        this.contractName = contractName;

        $(".contract-header__menu .dropdown-menu.dropdown-menu-right").waitUntil(Condition.visible, 10_000);
        try { Thread.sleep(1_000); } catch (InterruptedException e) { logger.error("InterruptedException", e); }
    }

    /**
     * Match by _EXACT_ contractName.
     * @return
     */
    public DeleteContract clickDeleteContract()
    {
        Selenide.executeJavaScript("$('.contract-header__menu .dropdown-menu.dropdown-menu-right a:contains(\"Delete\")')[0].click()");

        logger.info("Delete contract was clicked...");

        return new DeleteContract(contractName);
    }

    public CancelContract clickCancelContract()
    {
        Selenide.executeJavaScript("$('.contract-header__menu .dropdown-menu.dropdown-menu-right a:contains(\"Cancel\")')[0].click()");

        logger.info("Cancel contract was clicked...");

        return new CancelContract();
    }

    public RestartContract clickRestartContract()
    {
        Selenide.executeJavaScript("$('.contract-header__menu .dropdown-menu.dropdown-menu-right a:contains(\"Restart\")')[0].click()");

        logger.info("Restart contract was clicked...");

        return new RestartContract();
    }

    /**
     * In case of enabled "key": "CONTRACT_NAME_TEMPLATE", contract name may be dynamic => we can't know in advance contract title => do not match by contractName in popup
     * Just match to "Are you sure you want to delete contract" text
     * @param doNotMatchByContractName just flag to indicate
     * @return
     */
    public DeleteContract clickDeleteContract(boolean doNotMatchByContractName)
    {
        Selenide.executeJavaScript("$('.contract-header__menu .dropdown-menu.dropdown-menu-right a:contains(\"Delete\")')[0].click()");

        logger.info("Delete contract was clicked...");

        return new DeleteContract(doNotMatchByContractName);
    }

    public ContractInformation clickAmendContract()
    {
        Selenide.executeJavaScript("$('.contract-header__menu .dropdown-menu.dropdown-menu-right a:contains(\"Amend\")')[0].click()");

        logger.info("Amend contract was clicked...");

        return new ContractInformation();
    }

    /**
     * This option is available only for requests
     */
    public ReassignChiefNegotiator clickReassignChiefNegotiator()
    {
        Selenide.executeJavaScript("$('.contract-header__menu .dropdown-menu.dropdown-menu-right a:contains(\"Reassign\")')[0].click()");

        logger.info("Reassign Chief Negotiator was clicked...");

        return new ReassignChiefNegotiator();
    }

    /**
     * This option is available only for managed contracts
     */
    public TerminateContract clickTerminateContract()
    {
        Selenide.executeJavaScript("$('.contract-header__menu .dropdown-menu.dropdown-menu-right a:contains(\"Terminate\")')[0].click()");

        logger.info("Terminate contract was clicked...");

        return new TerminateContract(contractName);
    }

    public ReinstateContract clickReinstateContract()
    {
        Selenide.executeJavaScript("$('.contract-header__menu .dropdown-menu.dropdown-menu-right a:contains(\"Reinstate\")')[0].click()");

        logger.info("Reinstate contract was clicked...");

        return new ReinstateContract(contractName);
    }
}
