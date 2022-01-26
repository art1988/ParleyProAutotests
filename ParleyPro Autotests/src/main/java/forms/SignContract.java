package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class SignContract
{
    private SelenideElement title = $(".modal-body-title");

    private static Logger logger = Logger.getLogger(SignContract.class);

    /**
     * Public constructor that triggers after contract moves to SIGN status
     * @param contractName
     * @param integrationEnabled
     */
    public SignContract(String contractName, boolean integrationEnabled)
    {
        if( integrationEnabled )
        {
            title.shouldHave(Condition.exactText("Do you want to sign manually or electronically?"));
        }
        else
        {
            title.shouldHave(Condition.exactText("You have requested to manually sign contract “" + contractName + "”."));
        }
    }

    /**
     * Private constructor in case if integration is enabled
     * @param integrationEnabled just marker. Has no meaning
     * @param type may be 'Docusign' or 'Adobesign'
     */
    private SignContract(boolean integrationEnabled, String type)
    {
        switch (type)
        {
            case "Docusign":
                title.shouldHave(Condition.exactText("You have asked to start the sign process via DocuSign."));
            break;

            case "":
                title.shouldHave(Condition.exactText("You have asked to start the sign process via Adobe Sign."));
            break;
        }
    }

    /**
     * Clicking by Start button will download document
     * @throws FileNotFoundException
     */
    public void clickStart() throws FileNotFoundException
    {
        $("#manual-sign-start-button").download();

        logger.info("Start button was clicked");
    }

    public void clickSign()
    {
        $$(".sign-documents-footer__right button").filterBy(Condition.exactText("Sign")).first().click();

        logger.info("SIGN was clicked");
    }

    public SignContract clickDocusign()
    {
        $$(".modal-footer button").filterBy(Condition.exactText("Docusign")).first().click();

        logger.info("Docusign was clicked");

        return new SignContract(true, "Docusign");
    }

    public SignContract clickAdobeSign()
    {
        $$(".modal-footer button").filterBy(Condition.exactText("Adobe Sign")).first().click();

        logger.info("Adobe Sign was clicked");

        return new SignContract(true, "Adobesign");
    }
}
