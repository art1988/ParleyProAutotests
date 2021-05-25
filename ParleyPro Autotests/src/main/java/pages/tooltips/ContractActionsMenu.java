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

    public DeleteContract clickDeleteContract()
    {
        Selenide.executeJavaScript("$('.contract-header__menu .dropdown-menu.dropdown-menu-right a:contains(\"Delete\")')[0].click()");

        logger.info("Delete contract was clicked...");

        return new DeleteContract(contractName);
    }

    public ContractInformation clickAmendContract()
    {
        Selenide.executeJavaScript("$('.contract-header__menu .dropdown-menu.dropdown-menu-right a:contains(\"Amend\")')[0].click()");

        logger.info("Amend contract was clicked...");

        return new ContractInformation();
    }
}
