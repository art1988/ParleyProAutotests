package pages.tooltips;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import forms.ContractInformation;
import forms.delete.DeleteContract;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Class that represents menu with 'Cancel' and 'Delete' contract options.
 * In executed status additional menu items were added: 'Amend contract' and 'Terminate contract'.
 */
public class ContractActionsMenu
{
    private String contractName;


    private static Logger logger = Logger.getLogger(ContractActionsMenu.class);

    public ContractActionsMenu(String contractName)
    {
        this.contractName = contractName;

        $(".contract-header__menu .dropdown-menu.dropdown-menu-right").
                waitUntil(Condition.visible, 5_000).shouldHave(Condition.text("Delete contract"));
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
}
