package forms;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DocuSign
{
    private static Logger logger = Logger.getLogger(DocuSign.class);


    public DocuSign()
    {
        $(".integrations-docusign-login__title").shouldBe(Condition.visible).shouldHave(Condition.exactText("Your permission is required"));
        $$(".integrations-docusign-login__form input").shouldHave(CollectionCondition.size(2));
        $(".integrations-docusign-login__form button").shouldBe(Condition.visible);
    }

    public DocuSign setEmail(String email)
    {
        $(".integrations-docusign-login__form input[data-label=\"Email\"]").sendKeys(email);

        return this;
    }

    public DocuSign setPassword(String password)
    {
        $(".integrations-docusign-login__form input[data-label=\"Password\"]").sendKeys(password);

        return this;
    }

    public void clickSignIn()
    {
        $(".integrations-docusign-login__form button").click();

        logger.info("SIGN IN was clicked");
    }
}
